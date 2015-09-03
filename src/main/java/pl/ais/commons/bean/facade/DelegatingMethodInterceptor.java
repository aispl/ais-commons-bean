package pl.ais.commons.bean.facade;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Optional;

import static pl.ais.commons.bean.facade.ClassPredicates.proxyable;

/**
 * @author Warlock, AIS.PL
 * @since 1.2.1
 */
final class DelegatingMethodInterceptor implements MethodInterceptor {

    private final Object delegate;

    private final TraverseListener listener;

    DelegatingMethodInterceptor(final Object delegate, final TraverseListener listener) {
        this.delegate = delegate;
        this.listener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object intercept(final Object proxy, final Method overridden, final Object[] args, final MethodProxy forwarder) throws Throwable {
        listener.onMethodCall(delegate, overridden, args);

        // Invoke the intercepted method on delegate instance, ...
        final Object result = overridden.invoke(delegate, args);

        // ... and proxy the result, if possible (and needed).
        return proxyIfNeeded(result);
    }

    private Object proxyIfNeeded(@Nullable final Object object) {
        return Optional.ofNullable(object)
                       .map(subject -> subject.getClass())
                       .filter(proxyable())
                       .map(subjectClass -> Facade.over(object, listener))
                       .orElse(object);
    }

}
