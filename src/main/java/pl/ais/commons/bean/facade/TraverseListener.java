package pl.ais.commons.bean.facade;

import javax.annotation.concurrent.NotThreadSafe;
import java.lang.reflect.Method;
import java.util.Stack;

import static java.util.stream.Collectors.joining;

/**
 * Listener tracking down the method calls to provide path to accessed property.
 *
 * @author Warlock, AIS.PL
 * @since 1.2.1
 */
@NotThreadSafe
public final class TraverseListener {

    private final Stack<String> stack = new Stack<>();


    /**
     * @return path to the accessed property
     */
    public String asPath() {
        final String path = stack.stream().collect(joining("."));
        stack.clear();
        return path;
    }

    private void handleCollectionOrMapElementAccessor(final Object arg) {
        final String result;
        if ((null != arg) && String.class.equals(arg.getClass())) {
            result = new StringBuilder().append("['").append(arg).append("']").toString();
        } else {
            result = new StringBuilder().append('[').append(arg).append(']').toString();
        }
        if (!stack.isEmpty()) {
            stack.push(stack.pop() + result);
        }
    }

    @SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.UseVarargs"})
    public void onMethodCall(final Object object, final Method method, final Object[] args) {
        final char[] name = method.getName().toCharArray();
        final Class<?> returnType = method.getReturnType();
        processing:
        {
            // Accessors (get*) and mutators (set*), ...
            if ((3 < name.length) && (('g' == name[0]) || ('s' == name[0])) && ('e' == name[1]) && ('t' == name[2])) {
                name[3] = Character.toLowerCase(name[3]);
                stack.push(String.valueOf(name, 3, name.length - 3));
                break processing;
            }

            // ... boolean accessors (is*), ...
            if ((2 < name.length) && ('i' == name[0]) && ('s' == name[1]) && returnType.equals(boolean.class)) {
                name[2] = Character.toLowerCase(name[2]);
                stack.push(String.valueOf(name, 2, name.length - 2));
                break processing;
            }

            // ... Collection/Map element accessor.
            if ((3 == name.length) && ('g' == name[0]) && ('e' == name[1]) && ('t' == name[2]) && (1 == method.getParameterCount())) {
                handleCollectionOrMapElementAccessor(args[0]);
                break processing;
            }
        }
    }

    /**
     * Resets the listener to the initial state.
     */
    public void reset() {
        stack.clear();
    }

}
