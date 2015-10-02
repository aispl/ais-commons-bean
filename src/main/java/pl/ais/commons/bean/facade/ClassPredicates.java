package pl.ais.commons.bean.facade;

import net.sf.cglib.proxy.Factory;

import java.lang.reflect.Modifier;
import java.util.function.Predicate;

/**
 * @author Warlock, AIS.PL
 * @since 1.2.1
 */
final class ClassPredicates {

    private ClassPredicates() {
        throw new AssertionError("Creation of " + getClass().getName() + " instances is forbidden.");
    }

    /**
     * @return Predicate matched by classes being protected or public, having no <em>final</em> modifier applied
     */
    public static Predicate<Class<?>> inheritable() {
        return candidate -> {
            final int modifiers = candidate.getModifiers();
            return !Modifier.isFinal(modifiers) && (Modifier.isProtected(modifiers) || Modifier.isPublic(modifiers));
        };
    }

    public static boolean is(final Class<?> candidate, final Predicate<Class<?>> predicate) {
        return predicate.test(candidate);
    }

    public static Predicate<Class<?>> proxyClass() {
        return candidate -> Factory.class.isAssignableFrom(candidate);
    }

    public static Predicate<Class<?>> proxyable() {
        return inheritable().and(proxyClass().negate());
    }

}
