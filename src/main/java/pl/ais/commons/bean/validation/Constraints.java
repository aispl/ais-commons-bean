package pl.ais.commons.bean.validation;

import pl.ais.commons.bean.validation.constraint.AllOfConstraint;
import pl.ais.commons.bean.validation.constraint.AnyOfConstraint;
import pl.ais.commons.bean.validation.constraint.SimpleConstraint;
import pl.ais.commons.domain.specification.Specifications;

import javax.annotation.Nonnull;
import javax.money.MonetaryAmount;
import java.util.function.Predicate;

/**
 * Provides set of useful {@link Constraint} implementations.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
@SuppressWarnings({"PMD.AvoidFieldNameMatchingMethodName", "PMD.TooManyMethods"})
public final class Constraints {

    private static final Constraint REQUIRED = new SimpleConstraint<>("required", Specifications.notNull());

    private Constraints() {
        throw new AssertionError("Creation of " + getClass().getName() + " instances is forbidden.");
    }

    /**
     * @param <T>      type of the values to be constrained
     * @param boundary the boundary
     * @return constraint verifying if constrainable value is after predefined boundary
     */
    public static <T extends Comparable<? super T>> Constraint<?, T> after(final T boundary) {
        return new SimpleConstraint<>("after", Specifications.after(boundary));
    }

    /**
     * @param <T>   the type of values supported by the constraints
     * @param first first constraint to be enclosed
     * @param rest  remaining constraints to be enclosed
     * @return constraint matched if and only if all of the enclosed constraints are matched
     */
    @SafeVarargs
    public static <T> Constraint<?, ? super T> allOf(@Nonnull final Constraint<?, T> first, final Constraint<?, T>... rest) {
        return new AllOfConstraint<>(false, first, rest);
    }

    /**
     * @param <T>   the type of values supported by the constraints
     * @param first first constraint to be enclosed
     * @param rest  remaining constraints to be enclosed
     * @return constraint matched if and only if any of the enclosed constraints are matched
     */
    @SafeVarargs
    public static <T> Constraint<?, ? super T> anyOf(@Nonnull final Constraint<?, T> first, final Constraint<?, T>... rest) {
        return new AnyOfConstraint<>(false, first, rest);
    }

    /**
     * @param <T>      type of the values to be constrained
     * @param boundary the boundary
     * @return constraint verifying if constrainable value is before predefined bound
     */
    public static <T extends Comparable<? super T>> Constraint<?, T> before(final T boundary) {
        return new SimpleConstraint<>("before", Specifications.before(boundary));
    }

    /**
     * @param <T>         type of the values to be constrained
     * @param name        name of the constraint
     * @param determinant predicate being determinant of the constraint
     * @return constraint verifying if constrainable value matches given determinant
     */
    public static <T> Constraint<?, T> constraint(final String name, final Predicate<T> determinant) {
        return new SimpleConstraint<>(name, determinant);
    }

    /**
     * @param <T> type of the values to be constrained
     * @return constraint verifying if constrainable value is empty (applicable to Collection, Map or String)
     */
    public static <T> Constraint<?, T> empty() {
        return new SimpleConstraint<>("empty", Specifications.empty());
    }

    /**
     * @param <T>        type of the values to be constrained
     * @param upperLimit the upper limit for character sequence length (inclusive)
     * @return constraint verifying if character sequence is limited to predefined number of characters
     */
    @SuppressWarnings("unchecked")
    public static <T extends CharSequence> Constraint<?, T> fitInto(final int upperLimit) {
        return new SimpleConstraint<>("fitInto", Specifications.fitInto(upperLimit));
    }

    /**
     * @param <T>      type of the values to be constrained
     * @param boundary the boundary
     * @return constraint verifying if constrainable value is greater than given boundary
     */
    public static <T extends Number & Comparable<? super T>> Constraint<?, T> greaterThan(final T boundary) {
        return new SimpleConstraint<>("greaterThan", Specifications.after(boundary));
    }

    /**
     * @param <T>      type of the values to be constrained
     * @param boundary the boundary
     * @return constraint verifying if constrainable value is greater than given boundary
     */
    public static <T extends MonetaryAmount> Constraint<?, T> greaterThan(final T boundary) {
        return new SimpleConstraint<>("greaterThan", Specifications.after(boundary));
    }

    /**
     * @param <T>      type of the values to be constrained
     * @param boundary the boundary
     * @return constraint verifying if constrainable value is greater than or equal to the given boundary
     */
    public static <T extends Number & Comparable<T>> Constraint<?, T> greaterThanOrEqualTo(final T boundary) {
        return new SimpleConstraint<>("greaterThanOrEqualTo", Specifications.before(boundary).negate());
    }

    /**
     * @param <T>      type of the values to be constrained
     * @param boundary the boundary
     * @return constraint verifying if constrainable value is greater than or equal to the given boundary
     */
    public static <T extends MonetaryAmount> Constraint<?, T> greaterThanOrEqualTo(final T boundary) {
        return new SimpleConstraint<>("greaterThanOrEqualTo", Specifications.before(boundary).negate());
    }

    /**
     * @param <T>   type of the values to be constrained
     * @param value the value
     * @return constraint verifying if constrainable value is equal to predefined value
     */
    public static <T> Constraint<?, T> isEqual(final T value) {
        return new SimpleConstraint<>("isEqual", Specifications.isEqual(value));
    }

    /**
     * @param <T>      type of the values to be constrained
     * @param boundary the boundary
     * @return constraint verifying if constrainable value is less than given boundary
     */
    public static <T extends Number & Comparable<? super T>> Constraint<?, T> lessThan(final T boundary) {
        return new SimpleConstraint<>("lessThan", Specifications.before(boundary));
    }

    /**
     * @param <T>      type of the values to be constrained
     * @param boundary the boundary
     * @return constraint verifying if constrainable value is less than given boundary
     */
    public static <T extends MonetaryAmount> Constraint<?, T> lessThan(final T boundary) {
        return new SimpleConstraint<>("lessThan", Specifications.before(boundary));
    }

    /**
     * @param <T>      type of the values to be constrained
     * @param boundary the boundary
     * @return constraint verifying if constrainable value is less than or equal to the given boundary
     */
    public static <T extends Number & Comparable<? super T>> Constraint<?, T> lessThanOrEqualTo(final T boundary) {
        return new SimpleConstraint<>("lessThanOrEqualTo", Specifications.after(boundary).negate());
    }

    /**
     * @param <T>      type of the values to be constrained
     * @param boundary the boundary
     * @return constraint verifying if constrainable value is less than or equal to the given boundary
     */
    public static <T extends MonetaryAmount> Constraint<?, T> lessThanOrEqualTo(final T boundary) {
        return new SimpleConstraint<>("lessThanOrEqualTo", Specifications.after(boundary).negate());
    }

    /**
     * @param <T>   type of the values to be constrained
     * @param regex the regular expression
     * @return constraint verifying if constrainable value matches given regular expression
     */
    public static <T extends CharSequence> Constraint<?, T> matches(final String regex) {
        return new SimpleConstraint<>("regex", Specifications.matches(regex));
    }

    /**
     * @param <T>        type of the values to be constrained
     * @param constraint constraint to be negated
     * @return negation of given constraint
     */
    public static <T> Constraint<?, T> not(final Constraint<?, T> constraint) {
        return constraint.negate();
    }

    /**
     * @return constraint verifying if character sequence is holding at least one non-whitespace character.
     */
    public static Constraint<?, CharSequence> notBlank() {
        return new SimpleConstraint<>("notBlank", Specifications.notBlank());
    }

    /**
     * @return constraint verifying if required value has been defined (is not {@code null})
     */
    @SuppressWarnings("unchecked")
    public static Constraint<?, Object> required() {
        return REQUIRED;
    }

    /**
     * @return constraint verifying if string contains valid email address.
     */
    @SuppressWarnings("unchecked")
    public static Constraint<?, CharSequence> validEmail() {
        return new SimpleConstraint<>("validEmail", Specifications.validEmail());
    }

}
