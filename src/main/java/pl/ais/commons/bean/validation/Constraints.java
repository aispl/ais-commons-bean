package pl.ais.commons.bean.validation;

import java.util.Arrays;

import org.joda.money.BigMoneyProvider;

import pl.ais.commons.bean.validation.constraint.EmptyConstraint;
import pl.ais.commons.bean.validation.constraint.NotBlankConstraint;
import pl.ais.commons.bean.validation.constraint.RequiredConstraint;
import pl.ais.commons.bean.validation.constraint.composite.AllConstraint;
import pl.ais.commons.bean.validation.constraint.composite.AnyConstraint;
import pl.ais.commons.bean.validation.constraint.mail.ValidEmailConstraint;
import pl.ais.commons.domain.specification.Specifications;
import pl.ais.commons.domain.specification.composite.NotSpecification;

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
    public static <T extends Comparable<? super T>> Constraint<T> after(final T bound) {
        return new Constraint<>("after", Specifications.after(bound));
    }

    /**
     * @param constraints the constraints
     * @return specification of constrainable matching all given constraints
     */
    @SafeVarargs
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <V> Constraint<? super V> allConstraints(final Constraint<? super V>... constraints) {
        return new AllConstraint(false, constraints);
    }

    /**
     * @param constraints the constraints
     * @return specification of constrainable matching any of given constraints
     */
    @SafeVarargs
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <V> Constraint<? super V> anyConstraint(final Constraint<? super V>... constraints) {
        return new AnyConstraint(false, constraints);
    }

    /**
     * @param bound the bound
     * @return constraint verifying if constrainable value is before predefined bound
     */
    public static <T extends Comparable<? super T>> Constraint<T> before(final T bound) {
        return new Constraint<>("before", Specifications.before(bound));
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
        return (Constraint<T>) new Constraint<>("fitInto", Specifications.fitInto(upperLimit));
    }

    /**
     * @param bound the bound
     * @return constraint verifying if constrainable value is greater than given bound
     */
    public static <T extends Number & Comparable<? super T>> Constraint<? super T> greaterThan(final T bound) {
        return new Constraint<>("greaterThan", Specifications.after(bound));
    }

    /**
     * @param bound the bound
     * @return constraint verifying if constrainable value is greater than given bound
     */
    public static <T extends BigMoneyProvider & Comparable<? super T>> Constraint<T> greaterThan(final T bound) {
        return new Constraint<>("greaterThan", Specifications.after(bound));
    }

    /**
     * @param bound the bound
     * @return constraint verifying if constrainable value is greater than or equal to the given bound
     */
    public static <T extends Number & Comparable<T>> Constraint<? super T> greaterThanOrEqualTo(final T bound) {
        return new Constraint<>("greaterThanOrEqualTo", new NotSpecification<>(Specifications.before(bound)));
    }

    /**
     * @param bound the bound
     * @return constraint verifying if constrainable value is greater than or equal to the given bound
     */
    public static <T extends BigMoneyProvider & Comparable<? super T>> Constraint<T> greaterThanOrEqualTo(final T bound) {
        return new Constraint<>("greaterThanOrEqualTo", new NotSpecification<>(Specifications.before(bound)));
    }

    /**
     * @param value the value
     * @return constraint verifying if constrainable value is equal to predefined value
     */
    public static final <T> Constraint<T> isEqual(final T value) {
        return new Constraint<>("isEqual", Specifications.isEqual(value));
    }

    /**
     * @param bound the bound
     * @return constraint verifying if constrainable value is less than given bound
     */
    public static <T extends BigMoneyProvider & Comparable<? super T>> Constraint<T> lessThan(final T bound) {
        return new Constraint<>("lessThan", Specifications.before(bound));
    }

    /**
     * @param bound the bound
     * @return constraint verifying if constrainable value is less than given bound
     */
    public static <T extends Number & Comparable<? super T>> Constraint<T> lessThan(final T bound) {
        return new Constraint<>("lessThan", Specifications.before(bound));
    }

    /**
     * @param bound the bound
     * @return constraint verifying if constrainable value is less than or equal to the given bound
     */
    public static <T extends Number & Comparable<? super T>> Constraint<T> lessThanOrEqualTo(final T bound) {
        return new Constraint<>("lessThanOrEqualTo", new NotSpecification<>(Specifications.after(bound)));
    }

    /**
     * @param bound the bound
     * @return constraint verifying if constrainable value is less than or equal to the given bound
     */
    public static <T extends BigMoneyProvider & Comparable<? super T>> Constraint<T> lessThanOrEqualTo(final T bound) {
        return new Constraint<>("lessThanOrEqualTo", new NotSpecification<>(Specifications.after(bound)));
    }

    /**
     * @param regex the regular expression
     * @return constraint verifying if constrainable value matches given regular expression
     */
    public static final Constraint<CharSequence> matches(final String regex) {
        return new Constraint<>("regex", Specifications.matches(regex));
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
        return new Constraint<>(builder.toString(), new NotSpecification<>(constraint.getDeterminant()));
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
    @SuppressWarnings("unchecked")
    public static final <T extends CharSequence> Constraint<T> validEmail() {
        return (Constraint<T>) ValidEmailConstraint.INSTANCE;
    }

    /**
     * Constructs new instance.
     */
    private Constraints() {
        super();
    }

}
