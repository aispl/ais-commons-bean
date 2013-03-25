package pl.ais.commons.bean.facade.internal;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.annotation.CheckForNull;
import javax.annotation.concurrent.Immutable;

import net.sf.cglib.proxy.MethodProxy;
import pl.ais.commons.bean.facade.event.PropertyAccessEvent;

/**
 * Executable factory responsible for handling {@link List#get(int)} and {@link Map#get(Object)} methods.
 *
 * <p>
 *     The main reason standing behind intercepting {@link List#get(int)} and {@link Map#get(Object)} methods is
 *     possibility of computing property path for indexed properties.
 * </p>
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
@Immutable
final class CollectionMethodExecutableFactory extends AbstractChainedExecutableFactory {

    private static Executable createElementAccessor(
        final Object proxy, final MethodProxy methodProxy, final Object[] args) {
        return new Executable() {

            /**
             * {@inheritDoc}
             */
            @Override
            public Object execute(final ExecutionContext context) throws Throwable {
                final Object propertyValue = methodProxy.invokeSuper(proxy, args);
                context.multicastEvent(new PropertyAccessEvent(proxy, "[" + args[0] + "]", propertyValue));
                return propertyValue;
            }

        };
    }

    /**
     * Constructs new instance.
     *
     * @param nextFactory factory which should be asked for creating executable if this one doesn't support
     *        parameters provided during creation process
     */
    public CollectionMethodExecutableFactory(final ExecutableFactory nextFactory) {
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
            if ("get".equals(method.getName())) {
                final Class<?> targetClass = proxy.getClass();

                if (List.class.isAssignableFrom(targetClass)) {
                    result = createElementAccessor(proxy, methodProxy, args);
                    break processing;
                }

                if (Map.class.isAssignableFrom(targetClass)) {
                    result = createElementAccessor(proxy, methodProxy, args);
                    break processing;
                }
            }
        }

        return result;
    }
}
