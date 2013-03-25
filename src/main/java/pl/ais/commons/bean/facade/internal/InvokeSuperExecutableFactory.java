package pl.ais.commons.bean.facade.internal;

import static pl.ais.commons.bean.facade.internal.ProxyHelper.proxyIfNeeded;

import java.beans.PropertyChangeEvent;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.MethodProxy;

import org.objectweb.asm.Type;

import pl.ais.commons.bean.facade.ObservableFacade;
import pl.ais.commons.bean.facade.event.PropertyAccessEvent;

/**
 * Factory for creating executables delegating the work to the real (non-intercepted) method of proxied class.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
@Immutable
final class InvokeSuperExecutableFactory extends AbstractPropertyAccessExecutableFactory {

    /**
     * Creates the signature for property accessor method.
     *
     * @param method property mutator method
     * @param propertyName property name
     * @return the signature for property accessor method
     */
    protected static Signature createAccessorSignature(final Method method, final String propertyName) {
        // The accessor name depends on the type of returned values, ...
        final Type returnType = Type.getArgumentTypes(method)[0];
        final String prefix = Type.BOOLEAN_TYPE.equals(returnType) ? "is" : "get";

        // ... and is built from the prefix computed above, and the property name.
        char[] name = propertyName.toCharArray();
        name[0] = Character.toUpperCase(name[0]);

        return new Signature(prefix + new String(name), returnType, new Type[0]);
    }

    private static Executable createExecutable(final Object proxy, final MethodProxy methodProxy, final Object[] args) {
        return new Executable() {

            /**
             * {@inheritDoc}
             */
            @Override
            public Object execute(final ExecutionContext context) throws Throwable {
                return methodProxy.invokeSuper(proxy, proxyIfNeeded(args));
            }

        };
    }

    /**
     * Constructs new instance.
     *
     * @param nextFactory factory which should be asked for creating executable if this one doesn't support
     *        parameters provided during creation process
     */
    public InvokeSuperExecutableFactory(@Nonnull final ExecutableFactory nextFactory) {
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

                // Execute the accessor method, ...
                final Object result = methodProxy.invokeSuper(proxy, new Object[0]);

                // ... notify about the property access, and return the accessor invocation result.
                context.multicastEvent(new PropertyAccessEvent(proxy, propertyName, result));
                return result;
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

                // Remember current property value for further event creation, ...
                final MethodProxy getterProxy = MethodProxy.find(proxy.getClass(),
                    createAccessorSignature(method, propertyName));
                final Object oldValue = getterProxy.invokeSuper(proxy, new Object[0]);

                // ... process new property value - bind to parent observable if possible, ...
                final Object newValue = proxyIfNeeded(args[0]);
                if ((proxy instanceof ObservableFacade) && (newValue instanceof ObservableFacade)) {
                    ((ObservableFacade) newValue).setParent((ObservableFacade) proxy);
                }

                // ... execute the mutator method, and notify about the property change.
                methodProxy.invokeSuper(proxy, new Object[] {newValue});
                context.multicastEvent(new PropertyChangeEvent(proxy, propertyName, oldValue, newValue));

                return null;
            }

        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @CheckForNull
    @SuppressWarnings("PMD.NullAssignment")
    protected Executable doCreateExecutable(
        final Object proxy, final Method method, final MethodProxy methodProxy, final Object[] args) {
        Executable result;

        // We cannot handle situation where method proxy is undefined, or the method is abstract, ...
        if ((null == methodProxy) || Modifier.isAbstract(method.getModifiers())) {
            result = null;
        } else {

            // ... accessors/mutators need special treatment, ...
            result = super.doCreateExecutable(proxy, method, methodProxy, args);

            // ... all the rest will just forward the call to the non-intercepted method.
            if (null == result) {
                result = createExecutable(proxy, methodProxy, args);
            }
        }
        return result;
    }
}
