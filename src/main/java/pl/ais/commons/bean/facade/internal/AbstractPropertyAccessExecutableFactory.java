package pl.ais.commons.bean.facade.internal;

import java.lang.reflect.Method;
import java.util.Arrays;

import javax.annotation.CheckForNull;

import net.sf.cglib.proxy.MethodProxy;

/**
 * Base class for executable factories supporting property accessor/mutator handling.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
abstract class AbstractPropertyAccessExecutableFactory extends AbstractChainedExecutableFactory {

    /**
     * Calculates the property name using accessor method name.
     *
     * @param name accessor method name
     * @param offset the property name offset in method name
     * @return the property name
     */
    protected static final String getPropertyName(final char[] name, final int offset) {
        final char[] propertyName = Arrays.copyOfRange(name, offset, name.length);
        final int nameLength = propertyName.length;
        if ((1 == nameLength) || ((nameLength > 2) && Character.isLowerCase(propertyName[1]))) {
            propertyName[0] = Character.toLowerCase(propertyName[0]);
        }
        return new String(propertyName);
    }

    /**
     * Constructs new instance.
     *
     * @param nextFactory factory which should be asked for creating executable if this one doesn't support
     *        parameters provided during creation process
     */
    protected AbstractPropertyAccessExecutableFactory(final ExecutableFactory nextFactory) {
        super(nextFactory);
    }

    protected abstract Executable createPropertyAccessor(
        Object proxy, Method method, MethodProxy methodProxy, String propertyName);

    protected abstract Executable createPropertyMutator(
        Object proxy, Method method, MethodProxy methodProxy, String propertyName, Object[] args);

    /**
     * {@inheritDoc}
     */
    @Override
    @CheckForNull
    protected Executable doCreateExecutable(
        final Object proxy, final Method method, final MethodProxy methodProxy, final Object[] args) {
        Executable result = null;

        processing: {
            final char[] name = method.getName().toCharArray();

            if ((name.length > 3) && (name[1] == 'e') && (name[2] == 't')) {

                // Accessors (get*), ...
                if (name[0] == 'g') {
                    result = createPropertyAccessor(proxy, method, methodProxy, getPropertyName(name, 3));
                    break processing;
                }

                // ... mutators (set*), ...
                if (name[0] == 's') {
                    result = createPropertyMutator(proxy, method, methodProxy, getPropertyName(name, 3), args);
                    break processing;
                }
            }

            // ... accessors continued (is*).
            if ((name.length > 2) && (name[0] == 'i') && (name[1] == 's')) {
                result = createPropertyAccessor(proxy, method, methodProxy, getPropertyName(name, 2));
                break processing;
            }
        }

        return result;
    }

}
