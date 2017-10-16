package pl.ais.commons.bean.facade;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import org.objenesis.ObjenesisHelper;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Arrays;

import static pl.ais.commons.bean.facade.ClassPredicates.inheritable;
import static pl.ais.commons.bean.facade.ClassPredicates.is;
import static pl.ais.commons.bean.facade.FieldPredicates.staticField;

/**
 * Provides set of methods usable for creating facades.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
@Immutable
public final class Facade {

    private Facade() {
        throw new AssertionError("Creation of " + getClass().getName() + " instances is forbidden.");
    }

    @SuppressWarnings("PMD.EmptyCatchBlock")
    private static <T> void copyFieldsDeclaredBy(final @Nonnull T instance, final T proxy) {

        // For each field declared by instance class, except the static fields, ...
        final Class<?> instanceClass = instance.getClass();
        Arrays.stream(instanceClass.getDeclaredFields())
              .filter(staticField().negate())
              .forEachOrdered(field ->
              {
                  try {
                      // ... try to copy value from the instance to proxy.
                      field.setAccessible(true);
                      field.set(proxy, field.get(instance));
                  } catch (final IllegalAccessException | IllegalArgumentException exception) {
                      // Ignore ...
                  }
              });
    }

    private static <T> Class[] determineInterfaces(final Class<T> aClass) {
        final Class<?>[] interfaces = aClass.getInterfaces();
        return aClass.isInterface() ? new Class[] {aClass} : (0 == interfaces.length ? null : interfaces);
    }

    private static <T> Class<? super T> determineSuperclass(@Nonnull final Class<T> candidate) {
        return is(candidate, inheritable()) ? candidate : determineSuperclass(candidate.getSuperclass());
    }

    /**
     * Creates and returns the facade (proxy) built over given instance of class {@literal T}.
     *
     * @param instance instance to be proxied
     * @param listener property traverse listener to be used for the facade
     * @param <S>      superclass of {@literal T} which will be extended by the facade
     * @param <T>      type of the instance to be proxied
     * @return newly created facade (proxy) build over given instance of class {@literal T}
     */
    @SuppressWarnings("unchecked")
    public static <S, T extends S> S over(@Nonnull final T instance, final TraverseListener listener) {

        // Create CGLIB Enhancer using the instance class as superclass of the proxy we intend to create, ...
        final Class<T> instanceClass = (Class<T>) instance.getClass();
        final Class<? super T> superclass = determineSuperclass(instanceClass);
        final Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(superclass);

        // ... specify set of interfaces to be implemented by the proxy, ...
        enhancer.setInterfaces(determineInterfaces(instanceClass));

        // ... define the type of callback to be used, ...
        enhancer.setCallbackType(DelegatingMethodInterceptor.class);

        // ... and created the proxied class (skipping creating the instance for now).
        final Class<T> proxiedClass = enhancer.createClass();

        // Now prepare to create an instance of the proxied class, register callbacks and the class loader, ...
        Enhancer.registerCallbacks(proxiedClass, new Callback[] {new DelegatingMethodInterceptor(instance, listener)});

        // ... and do the dirty work.
        final T proxy = ObjenesisHelper.newInstance(proxiedClass);
        copyFieldsDeclaredBy(instance, proxy);
        return proxy;
    }

}
