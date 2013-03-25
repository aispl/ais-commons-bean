package pl.ais.commons.bean.facade.internal;

import java.lang.reflect.Method;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import net.sf.cglib.proxy.MethodProxy;
import pl.ais.commons.bean.facade.ObservableFacade;

/**
 * Executable factory responsible for handling {@link ObservableFacade} methods.
 *
 * <p>
 *    This factory holds predefined {@link ObservableFacade} instance, to which it will delegate all
 *    {@link ObservableFacade} methods' calls.
 * </p>
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
@Immutable
final class ObservableMethodExecutableFactory extends AbstractChainedExecutableFactory {

    private static Executable createDelegatingExecutable(final Object delegate, final Method method, final Object[] args) {
        return new Executable() {

            /**
             * {@inheritDoc}
             */
            @Override
            public Object execute(final ExecutionContext context) throws Throwable {
                return method.invoke(delegate, args);
            }

        };
    }

    private final ObservableFacade delegate;

    /**
     * Constructs new instance.
     *
     * @param delegate the delegate which will handle all {@link ObservableFacade} method calls
     * @param nextFactory next factory in a chain
     */
    public ObservableMethodExecutableFactory(final ObservableFacade delegate, final ExecutableFactory nextFactory) {
        super(nextFactory);
        this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @CheckForNull
    protected Executable doCreateExecutable(
        @Nonnull final Object proxy, @Nonnull final Method method, @Nullable final MethodProxy methodProxy,
        final Object[] args) {
        Executable result = null;
        if (ObservableFacade.class == method.getDeclaringClass()) {
            result = createDelegatingExecutable(delegate, method, args);
        }
        return result;
    }

}
