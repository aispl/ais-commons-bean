package pl.ais.commons.bean.validation;

import java.util.Arrays;

import pl.ais.commons.bean.validation.constraints.BaseConstraint;
import pl.ais.commons.bean.validation.constraints.EmptyConstraint;
import pl.ais.commons.bean.validation.constraints.NotBlankConstraint;
import pl.ais.commons.bean.validation.constraints.RequiredConstraint;
import pl.ais.commons.bean.validation.constraints.mail.ValidEmailConstraint;
import pl.ais.commons.domain.specification.Specification;
import pl.ais.commons.domain.specification.Specifications;
import pl.ais.commons.domain.specification.composite.AndSpecification;
import pl.ais.commons.domain.specification.composite.OrSpecification;

/**
 * Provides set of useful {@link Constraint} implementations.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
public final class Constraints {

    /**
     * @param bound the bound
     * @return constraint verifying if constrainable value is after predefined bound
     */
    public static <T extends Comparable<T>> Constraint<T> after(final T bound) {
        return new BaseConstraint<>("after", Specifications.after(bound));
    }

    /**
     * @param constraints the constraints
     * @return specification of constrainable matching all given constraints
     */
    @SafeVarargs
    public static <V> Specification<Constrainable<? extends V>> allConstraints(
        final Specification<Constrainable<? extends V>>... constraints) {
        return new AndSpecification<>(constraints);
    }

    /**
     * @param constraints the constraints
     * @return specification of constrainable matching any of given constraints
     */
    @SafeVarargs
    public static <V> Specification<Constrainable<? extends V>> anyConstraint(
        final Specification<Constrainable<? extends V>>... constraints) {
        return new OrSpecification<>(constraints);
    }

    /**
     * @param bound the bound
     * @return constraint verifying if constrainable value is before predefined bound
     */
    public static <T extends Comparable<T>> Constraint<T> before(final T bound) {
        return new BaseConstraint<>("before", Specifications.before(bound));
    }

    /**
     * @return constraint verifying if constrainable value is empty (applicable to Collection, Map or String)
     */
    @SuppressWarnings("unchecked")
    public static <V> Constraint<V> empty() {
        return (Constraint<V>) EmptyConstraint.INSTANCE;
    }

    /**
     * @param upperLimit the upper limit for character sequence length (inclusive)
     * @return constraint verifying if character sequence is limited to predefined number of characters
     */
    @SuppressWarnings("unchecked")
    public static <T extends CharSequence> Constraint<T> fitInto(final int upperLimit) {
        return (Constraint<T>) new BaseConstraint<>("fitInto", Specifications.fitInto(upperLimit));
    }

    /**
     * @param bound the bound
     * @return constraint verifying if constrainable value is greater than given bound
     */
    public static <T extends Number & Comparable<T>> Constraint<T> greaterThan(final T bound) {
        return new BaseConstraint<>("greaterThan", Specifications.after(bound));
    }

    /**
     * @param value the value
     * @return constraint verifying if constrainable value is equal to predefined value
     */
    public static final <T> Constraint<T> isEqual(final T value) {
        return new BaseConstraint<>("isEqual", Specifications.isEqual(value));
    }

    /**
     * @param bound the bound
     * @return constraint verifying if constrainable value is less than given bound
     */
    public static <T extends Number & Comparable<T>> Constraint<T> lessThan(final T bound) {
        return new BaseConstraint<>("lessThan", Specifications.before(bound));
    }

    /**
     * @param regex the regular expression
     * @return constraint verifying if constrainable value matches given regular expression
     */
    public static final Constraint<CharSequence> matches(final String regex) {
        return new BaseConstraint<>("regex", Specifications.matches(regex));
    }

    /**
     * Creates and returns the constraint being negation of given constraint.
     *
     * @param constraint the constraint to negate
     * @return newly created constraint being negation of given constraint
     */
    public static <V> Constraint<V> not(final Constraint<V> constraint) {
        // Adjust the name of the constraint, ...
        char[] name = constraint.getName().toCharArray();
        final StringBuilder builder = new StringBuilder();

        // ... by cutting off / adding the 'not' prefix, ...
        if ((name.length >= 3) && ('n' == name[0]) && ('o' == name[1]) && ('t' == name[2])) {
            name = Arrays.copyOfRange(name, 3, name.length);
        } else {
            builder.append("not");
        }

        // ... capitalizing the remainder, and adding it to the result name ...
        name[0] = Character.toUpperCase(name[0]);
        builder.append(name);
        return new BaseConstraint<>(builder.toString(), constraint.getDeterminant());
    }

    /**
     * @return constraint verifying if character sequence is holding at least one non-whitespace character.
     */
    public static Constraint<CharSequence> notBlank() {
        return NotBlankConstraint.INSTANCE;
    }

    /**
     * @return constraint verifying if required value has been defined (is not {@code null})
     */
    @SuppressWarnings("unchecked")
    public static <V> Constraint<V> required() {
        return (Constraint<V>) RequiredConstraint.INSTANCE;
    }

    /**
     * @return constraint verifying if string contains valid email address.
     */
    public static final Constraint<String> validEmail() {
        return ValidEmailConstraint.INSTANCE;
    }

    /**
     * Constructs new instance.
     */
    private Constraints() {
        super();
    }

}
