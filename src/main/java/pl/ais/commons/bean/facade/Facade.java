package pl.ais.commons.bean.facade;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import org.objenesis.ObjenesisHelper;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import static pl.ais.commons.bean.facade.ClassPredicates.accessible;
import static pl.ais.commons.bean.facade.ClassPredicates.inheritable;
import static pl.ais.commons.bean.facade.ClassPredicates.is;

/**
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
@Immutable
public final class Facade {

    private static <T> Class<? super T> determineSuperclass(final @Nonnull Class<T> candidate) {
        final Class<? super T> result;
        if (is(candidate, inheritable().and(accessible()))) {
            result = candidate;
        } else {
            result = determineSuperclass(candidate.getSuperclass());
        }
        return result;
    }

    public static <S, T extends S> S over(@Nonnull T instance, final TraverseListener listener) {

        // Create CGLIB Enhancer using the instance class as superclass of the proxy we intend to create, ...
        final Class<? super T> superclass = determineSuperclass((Class<T>) instance.getClass());
        final Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(superclass);

        // ... specify set of interfaces to be implemented by the proxy, ...
        enhancer.setInterfaces(superclass.isInterface() ? new Class[] {superclass} : superclass.getInterfaces());

        // ... define the type of callback to be used, ...
        enhancer.setCallbackType(DelegatingMethodInterceptor.class);

        // ... and created the proxied class (skipping creating the instance for now).
        final Class<T> proxiedClass = enhancer.createClass();

        // Now prepare to create an instance of the proxied class, register callbacks and the class loader, ...
        Enhancer.registerCallbacks(proxiedClass, new Callback[] {new DelegatingMethodInterceptor(instance, listener)});
        enhancer.setClassLoader(superclass.getClassLoader());

        // ... and do the dirty work.
        return ObjenesisHelper.newInstance(proxiedClass);
    }

}
