package pl.ais.commons.bean.facade.internal;

import java.lang.reflect.Method;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.sf.cglib.proxy.MethodProxy;

/**
 * Base class for executable factories which can be chained.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
abstract class AbstractChainedExecutableFactory implements ExecutableFactory {

    private final transient ExecutableFactory nextFactory;

    /**
     * Constructs new instance.
     *
     * @param nextFactory factory which should be asked for creating executable if this one doesn't support
     *        parameters provided during creation process
     */
    protected AbstractChainedExecutableFactory(final ExecutableFactory nextFactory) {
        super();
        this.nextFactory = nextFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Executable createExecutable(
        final Object proxy, final Method method, final MethodProxy methodProxy, final Object[] args) {

        // Try to create executable itself, ...
        Executable result = doCreateExecutable(proxy, method, methodProxy, args);

        // ... if provided parameters are not supported by this class, and there is another factory in the chain,
        //     ask it about the executable creating.
        if ((null == result) && (null != nextFactory)) {
            result = nextFactory.createExecutable(proxy, method, methodProxy, args);
        }
        return result;
    }

    @CheckForNull
    protected abstract Executable doCreateExecutable(
        @Nonnull final Object proxy, @Nonnull final Method method, @Nullable final MethodProxy methodProxy,
        final Object[] args);

}
