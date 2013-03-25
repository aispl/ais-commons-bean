package pl.ais.commons.bean.facade.event;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EventObject;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import net.sf.cglib.proxy.MethodProxy;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Delivered whenever any facade method is invoked.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
@Immutable
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public final class MethodInvocationEvent extends EventObject {

    private final Object[] args;

    private final Method method;

    private final MethodProxy methodProxy;

    /**
     * Constructs new instance.
     *
     * @param proxy the facade object on which the method was called
     * @param method intercepted method
     * @param methodProxy used to invoke super (non-intercepted method); may be called as many times as needed
     * @param args argument array; primitive types are wrapped
     */
    public MethodInvocationEvent(@Nonnull final Object proxy, @Nonnull final Method method,
        @Nullable final MethodProxy methodProxy, @Nullable final Object[] args) {
        super(proxy);
        this.method = method;
        this.methodProxy = methodProxy;
        this.args = args;
    }

    /**
     * @return the method call arguments
     */
    @CheckForNull
    public Object[] getArgs() {
        return args;
    }

    /**
     * @return the intercepted method
     */
    @Nonnull
    public Method getMethod() {
        return method;
    }

    /**
     * @return the method proxy
     */
    @CheckForNull
    public MethodProxy getMethodProxy() {
        return methodProxy;
    }

    /**
     * @see java.util.EventObject#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("source", getSource()).append("method", method)
            .append("methodProxy", methodProxy).append("args", (null == args) ? null : Arrays.asList(args)).build();
    }

}
