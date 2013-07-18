package pl.ais.commons.bean.facade.internal;

import static pl.ais.commons.bean.facade.internal.ProxyHelper.defaultValueForMethod;
import static pl.ais.commons.bean.facade.internal.ProxyHelper.defaultValueForType;
import static pl.ais.commons.bean.facade.internal.ProxyHelper.proxyIfNeeded;

import java.beans.PropertyChangeEvent;
import java.lang.reflect.Method;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import net.sf.cglib.proxy.MethodProxy;
import pl.ais.commons.bean.facade.ObservableFacade;
import pl.ais.commons.bean.facade.event.PropertyAccessEvent;

/**
 * Factory for creating executables providing an access/mutating bean properties.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
@Immutable
final class PropertyAccessExecutableFactory extends AbstractPropertyAccessExecutableFactory {

    /**
     * Constructs new instance.
     *
     * @param nextFactory factory which should be asked for creating executable if this one doesn't support
     *        parameters provided during creation process
     */
    public PropertyAccessExecutableFactory(@Nonnull final ExecutableFactory nextFactory) {
        super(nextFactory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Executable createPropertyAccessor(
        final Object proxy, final Method method, final MethodProxy methodProxy, final String propertyName) {
        return new Executable() {

            /**
             * {@inheritDoc}
             */
            @Override
            public Object execute(final ExecutionContext context) throws Throwable {

                // Try to read the property value from property store first, ...
                final PropertyStore store = context.getPropertyStore();
                Object propertyValue = store.getPropertyValue(propertyName);

                // ... if no value has been found, try to guess default one ...
                if (null == propertyValue) {
                    propertyValue = defaultValueForMethod(method);
                    if (null != propertyValue) {

                        // ... process default value - bind to parent observable if possible, ...
                        propertyValue = proxyIfNeeded(propertyValue);
                        if ((proxy instanceof ObservableFacade) && (propertyValue instanceof ObservableFacade)) {
                            ((ObservableFacade) propertyValue).setParent((ObservableFacade) proxy);
                        }

                        // ... put the default value into property store, ...
                        store.setPropertyValue(propertyName, propertyValue);
                    }
                }

                // ... propagate the property access event, and return the value to the caller.
                context.multicastEvent(new PropertyAccessEvent(proxy, propertyName, propertyValue));
                return propertyValue;
            }

        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Executable createPropertyMutator(
        final Object proxy, final Method method, final MethodProxy methodProxy, final String propertyName,
        final Object[] args) {
        return new Executable() {

            /**
             * {@inheritDoc}
             */
            @Override
            public Object execute(final ExecutionContext context) throws Throwable {
                final PropertyStore store = context.getPropertyStore();

                // Remember current property value for further event creation, ...
                Object oldValue = store.getPropertyValue(propertyName);
                if (null == oldValue) {
                    oldValue = defaultValueForType(method.getReturnType());
                }

                // ... process new property value - bind to parent observable if possible, ...
                final Object newValue = proxyIfNeeded(args[0]);
                if ((proxy instanceof ObservableFacade) && (newValue instanceof ObservableFacade)) {
                    ((ObservableFacade) newValue).setParent((ObservableFacade) proxy);
                }

                // ... notify about the value change and store it for future use.
                context.multicastEvent(new PropertyChangeEvent(proxy, propertyName, oldValue, newValue));
                store.setPropertyValue(propertyName, newValue);

                return null;
            }

        };
    }

}
