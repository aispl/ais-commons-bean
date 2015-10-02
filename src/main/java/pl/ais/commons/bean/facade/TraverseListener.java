package pl.ais.commons.bean.facade;

import javax.annotation.concurrent.NotThreadSafe;
import java.lang.reflect.Method;
import java.util.Stack;

import static java.util.stream.Collectors.joining;

/**
 * @author Warlock, AIS.PL
 * @since 1.2.1
 */
@NotThreadSafe
public final class TraverseListener {

    private final Stack<String> stack = new Stack<>();

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
            if ((name.length > 3) && ((name[0] == 'g') || (name[0] == 's')) && (name[1] == 'e') && (name[2] == 't')) {
                name[3] = Character.toLowerCase(name[3]);
                stack.push(String.valueOf(name, 3, name.length - 3));
                break processing;
            }

            // ... boolean accessors (is*), ...
            if ((name.length > 2) && (name[0] == 'i') && (name[1] == 's') && returnType.equals(boolean.class)) {
                name[2] = Character.toLowerCase(name[2]);
                stack.push(String.valueOf(name, 2, name.length - 2));
                break processing;
            }

            // ... Collection/Map element accessor.
            if ((3 == name.length) && (name[0] == 'g') && (name[1] == 'e') && (name[2] == 't') && (1 == method.getParameterCount())) {
                handleCollectionOrMapElementAccessor(args[0]);
                break processing;
            }
        }
    }

}
