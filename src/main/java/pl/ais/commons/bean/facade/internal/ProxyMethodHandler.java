package pl.ais.commons.bean.facade.internal;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.EventObject;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.ais.commons.bean.facade.ObservableFacade;
import pl.ais.commons.bean.facade.event.MethodInvocationEvent;

/**
 * Proxy method handler.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
public final class ProxyMethodHandler implements ExecutionContext, InvocationHandler, MethodInterceptor, PropertyStore,
    Serializable {

    private static ExecutableFactory createFactoryChain(final EventMulticaster eventMulticaster) {
        return new ObservableMethodExecutableFactory(eventMulticaster, new CollectionMethodExecutableFactory(
            new InvokeSuperExecutableFactory(new PropertyAccessExecutableFactory(
                new ObjectMethodExecutableFactory(null)))));
    }

    private final EventMulticaster eventMulticaster;

    private transient ExecutableFactory factory;

    private final Map<String, Object> properties;

    /**
     * Constructs new instance.
     */
    public ProxyMethodHandler() {
        super();
        this.eventMulticaster = new EventMulticaster();
        this.properties = new TreeMap<>();
    }

    @Nonnull
    private Executable createExecutable(
        @Nonnull final Object proxy, @Nonnull final Method method, @Nullable final MethodProxy methodProxy,
        @Nullable final Object[] args) {
        synchronized (this) {
            if (null == factory) {
                factory = createFactoryChain(eventMulticaster);
            }
        }
        return factory.createExecutable(proxy, method, methodProxy, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertyStore getPropertyStore() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getPropertyValue(final String propertyName) {
        return properties.get(propertyName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object intercept(final Object proxy, final Method method, final Object[] args, final MethodProxy methodProxy)
        throws Throwable {
        return invokeMethod(proxy, method, methodProxy, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object invoke(@Nonnull final Object proxy, @Nonnull final Method method, @Nullable final Object[] args)
        throws Throwable {
        return invokeMethod(proxy, method, null, args);
    }

    private Object invokeMethod(
        final Object proxy, final Method method, final MethodProxy methodProxy, final Object[] args) throws Throwable {

        // Create the executable based on provided parameters, ...
        final Executable executable = createExecutable(proxy, method, methodProxy, args);

        // ... verify if we know how to handle the method, ...
        if (null == executable) {
            throw new UnsupportedOperationException();
        }

        // ... notify about the method invocation (except the ObservableFacade methods), ...
        if (ObservableFacade.class != method.getDeclaringClass()) {
            multicastEvent(new MethodInvocationEvent(proxy, method, methodProxy, args));
        }

        // ... and finally execute it.
        return executable.execute(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void multicastEvent(final EventObject event) {
        eventMulticaster.multicastEvent(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPropertyValue(final String propertyName, final Object value) {
        properties.put(propertyName, value);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("eventMulticaster", eventMulticaster).append("properties", properties)
            .build();
    }

}
