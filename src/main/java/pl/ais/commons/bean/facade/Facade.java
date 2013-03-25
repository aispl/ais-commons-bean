package pl.ais.commons.bean.facade;

import java.lang.reflect.Proxy;

import net.sf.cglib.proxy.Enhancer;
import pl.ais.commons.bean.facade.internal.ProxyMethodHandler;

/**
 * Provides set of utility methods for operating on facades.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
public final class Facade {

    /**
     * Adjusts the list of interfaces which should be implemented by the facade.
     *
     * <p>
     *     <strong>Note:</strong>
     *     This method adds new interfaces to the list provided by caller. At the moment, in fact only one interface:
     *     {@link ObservableFacade}.
     * </p>
     *
     * @param dropFirst determines if first element should be dropped from the list (for building proxies based
     *        on concrete class)
     * @param interfaces the list of interfaces implemented by the facade
     * @return adjusted list of interfaces which should be implemented by the facade
     */
    private static Class<?>[] adjustInterfaces(final boolean dropFirst, final Class<?>... interfaces) {
        Class<?>[] result;
        if ((0 == interfaces.length) || ((1 == interfaces.length) && dropFirst)) {
            result = new Class<?>[] {ObservableFacade.class};
        } else {
            result = new Class<?>[dropFirst ? interfaces.length : interfaces.length + 1];
            System.arraycopy(interfaces, dropFirst ? 1 : 0, result, 0, dropFirst ? interfaces.length - 1
                : interfaces.length);
            result[result.length - 1] = ObservableFacade.class;
        }
        return result;
    }

    /**
     * Creates new instance of object implementing given interfaces.
     *
     * <p>
     *     Note: There is special case, when first element of {@code interfaces} is a class, in this case
     *     created instance will extend this class, and implement the interfaces given afterward.
     * </p>
     *
     * @param interfaces the interfaces to implement
     * @return newly create instance of object implementing given interfaces
     */
    public static <T> T implementing(final Class<?>... interfaces) {

        // Sanity check first, ...
        if (0 == interfaces.length) {
            throw new IllegalArgumentException("Please, provide at least one class/interface to build proxy.");
        }

        // ... check if first interfaces element is class or interface, build the proxy according to this check ...
        T result;
        final Class<?> targetClass = interfaces[0];
        if (targetClass.isInterface()) {
            result = (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                adjustInterfaces(false, interfaces), new ProxyMethodHandler());
        } else {
            result = (T) Enhancer.create(targetClass, adjustInterfaces(true, interfaces), new ProxyMethodHandler());
        }
        return result;
    }

    private Facade() {
        super();
    }

}
