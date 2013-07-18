package pl.ais.commons.bean.facade.internal;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

import javax.annotation.Nonnull;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import pl.ais.commons.bean.facade.Facade;
import pl.ais.commons.bean.facade.FacadeAdvice;
import pl.ais.commons.bean.facade.ObservableFacade;

/**
 * Provides set of utility methods for proxies.
 *
 * @author Warlock, AIS.PL
 * @since
 */
final class ProxyHelper {

    public static Object defaultValueForMethod(final Method method) throws InstantiationException,
        IllegalAccessException {
        Object result = null;
        processing: {
            final Class<?> returnType = method.getReturnType();

            // For primitive types different than void, we are able to determine default value returned by method
            // very precisely ...
            if (returnType.isPrimitive() && (void.class != returnType)) {
                result = Array.get(Array.newInstance(returnType, 1), 0);
                break processing;
            }

            // ... otherwise we rely on the additional info provided with annotations, ...
            if (returnType.isInterface() && method.isAnnotationPresent(Nonnull.class)) {
                final FacadeAdvice implementation = method.getAnnotation(FacadeAdvice.class);
                if (null == implementation) {
                    result = Facade.implementing(returnType);
                } else {
                    final Class<?> targetClass = implementation.targetClass();
                    if (void.class != targetClass) {
                        result = targetClass.newInstance();
                    }
                }
            }
        }

        return result;
    }

    public static Object defaultValueForType(final Class<?> type) {
        Object result = null;
        if (type.isPrimitive() && (void.class != type)) {
            result = Array.get(Array.newInstance(type, 1), 0);
        }
        return result;
    }

    public static boolean isProxyable(final Class<?> targetClass) {
        boolean proxyable = false;
        processing: {
            // Already proxied classes doesn't need any further processing, ...
            if (isProxyClass(targetClass)) {
                break processing;
            }
            // ... interfaces can always be proxied, ...
            if (targetClass.isInterface()) {
                proxyable = true;
                break processing;
            }
            // ... final classes cannot be processed using CGLIB, ...
            if (Modifier.isFinal(targetClass.getModifiers())) {
                break processing;
            }

            // ... all the rest can be proxied, probably ;).
            proxyable = true;
        }
        return proxyable;
    }

    private static boolean isProxyClass(final Class<?> targetClass) {
        return Enhancer.isEnhanced(targetClass) || Proxy.isProxyClass(targetClass);
    }

    public static Object proxyIfNeeded(final Object target) {
        Object result = target;
        processing: {
            // Null values doesn't require any processing at all, ...
            if (null == target) {
                break processing;
            }

            final Class<?> targetClass = target.getClass();

            // ... there are also some types of classes, that shouldn't (or cannot) be proxied (like final classes, etc.)
            if (!isProxyable(targetClass)) {
                break processing;
            }

            boolean copyingPossible = false;
            Class<?> lowerBound = targetClass;
            for (final Constructor<?> constructor : targetClass.getConstructors()) {
                final Class<?>[] parameterTypes = constructor.getParameterTypes();
                if ((1 == parameterTypes.length) && parameterTypes[0].isAssignableFrom(lowerBound)) {
                    copyingPossible = true;
                    lowerBound = parameterTypes[0];
                }
            }

            if (copyingPossible) {
                final Enhancer enhancer = new Enhancer();
                enhancer.setSuperclass(targetClass);
                enhancer.setInterfaces(new Class[] {ObservableFacade.class});
                enhancer.setCallback(new MethodInterceptor() {

                    @Override
                    public Object intercept(
                        final Object obj, final Method method, final Object[] args, final MethodProxy proxy)
                        throws Throwable {
                        return proxy.invokeSuper(obj, args);
                    }
                });
                result = enhancer.create(new Class[] {lowerBound}, new Object[] {target});
                ((Factory) result).setCallbacks(new Callback[] {new ProxyMethodHandler()});
            }
        }
        return result;
    }

    @Nonnull
    public static Object[] proxyIfNeeded(final Object[] args) {
        Object[] result;
        if (null == args) {
            result = new Object[0];
        } else {
            result = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                result[i] = proxyIfNeeded(args[i]);
            }
        }
        return result;
    }

    private ProxyHelper() {
        super();
    }

}
