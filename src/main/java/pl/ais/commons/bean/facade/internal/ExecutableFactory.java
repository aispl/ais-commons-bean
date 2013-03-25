package pl.ais.commons.bean.facade.internal;

import java.lang.reflect.Method;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.sf.cglib.proxy.MethodProxy;

/**
 * Defines the API contract for factory creating executables.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
interface ExecutableFactory {

    /**
     * Creates new {@code Executable} instance.
     *
     * @param proxy the proxy instance that the {@code method} was invoked on
     * @param method the {@link Method} instance corresponding to method invoked on the proxy instance
     * @param methodProxy provides an access to original (non-intercepted) method of the proxy instance (CGLIB)
     * @param args an array of objects containing the values of the arguments passed in the method invocation
     *        on the proxy instance, or {@code null} if interface method takes no arguments
     * @return newly created {@code Executable} instance
     */
    @CheckForNull
    Executable createExecutable(
        @Nonnull Object proxy, @Nonnull Method method, @Nullable MethodProxy methodProxy, @Nullable Object[] args);

}
