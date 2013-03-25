package pl.ais.commons.bean.facade.internal;

import java.lang.reflect.Method;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import net.sf.cglib.proxy.MethodProxy;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Factory for creating executables handling basic {@link Object} methods, like: equals, hashCode and toString.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
@Immutable
final class ObjectMethodExecutableFactory extends AbstractChainedExecutableFactory {

    private static Executable createEqualsExecutable(final Object proxy, final Object other) {
        return new Executable() {

            /**
             * {@inheritDoc}
             */
            @Override
            @SuppressWarnings("PMD.CompareObjectsWithEquals")
            public Boolean execute(final ExecutionContext context) {
                return Boolean.valueOf(proxy == other);
            }
        };
    }

    private static Executable createHashCodeExecutable(final Object proxy) {
        return new Executable() {

            /**
             * {@inheritDoc}
             */
            @Override
            public Integer execute(final ExecutionContext context) {
                return Integer.valueOf(System.identityHashCode(proxy));
            }
        };
    }

    private static Executable createToStringExecutable(final Object proxy) {
        return new Executable() {

            /**
             * {@inheritDoc}
             */
            @Override
            public String execute(final ExecutionContext context) {
                return new ToStringBuilder(proxy).build();
            }
        };
    }

    /**
     * Constructs new instance.
     *
     * @param nextFactory factory which should be asked for creating executable if this one doesn't support
     *        parameters provided during creation process
     */
    public ObjectMethodExecutableFactory(@Nonnull final ExecutableFactory nextFactory) {
        super(nextFactory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @CheckForNull
    protected Executable doCreateExecutable(
        final Object proxy, final Method method, final MethodProxy methodProxy, final Object[] args) {
        Executable result = null;
        processing: {
            final String methodName = method.getName();

            // ... hashCode(), ...
            if ("hashCode".equals(methodName)) {
                result = createHashCodeExecutable(proxy);
                break processing;
            }

            // ... equals(), ...
            if ("equals".equals(methodName)) {
                result = createEqualsExecutable(proxy, args[0]);
                break processing;
            }

            // ... toString().
            if ("toString".equals(methodName)) {
                result = createToStringExecutable(proxy);
                break processing;
            }
        }
        return result;
    }
}
