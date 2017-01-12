package pl.ais.commons.bean.facade;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;

/**
 * @author Warlock, AIS.PL
 * @since 1.3.3
 */
final class FieldPredicates {

    private FieldPredicates() {
        throw new AssertionError("Creation of " + getClass().getName() + " instances is forbidden.");
    }

    /**
     * Verifies if candidate field matches the predicate.
     *
     * @param candidate candidate field
     * @param predicate predicate to be matched
     * @return {code true} if candidate field matches the predicate, {@code false} otherwise
     */
    public static boolean is(final Field candidate, final Predicate<Field> predicate) {
        return predicate.test(candidate);
    }

    /**
     * @return predicate matched by static fields
     */
    public static Predicate<Field> staticField() {
        return candidate -> {
            final int modifiers = candidate.getModifiers();
            return Modifier.isStatic(modifiers);
        };
    }

}
