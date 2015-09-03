package pl.ais.commons.bean.validation;

import pl.ais.commons.bean.validation.constraint.AllOfConstraint;
import pl.ais.commons.bean.validation.constraint.AnyOfConstraint;
import pl.ais.commons.bean.validation.constraint.SimpleConstraint;
import pl.ais.commons.domain.specification.Specifications;

import javax.annotation.Nonnull;

/**
 * Provides set of useful {@link SimpleConstraint} implementations.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
public final class Constraints {

    private static final Constraint REQUIRED = new SimpleConstraint<>("required", Specifications.notNull());

    /**
     * Constructs new instance.
     */
    private Constraints() {
        super();
    }

    /**
     * @param bound the bound
     * @return constraint verifying if constrainable value is after predefined bound
     */
    public static <T extends Comparable<? super T>> Constraint<?, T> after(final T bound) {
        return new SimpleConstraint<>("after", Specifications.after(bound));
    }

    /**
     * @param first first constraint to be enclosed
     * @param rest  remaining constraints to be enclosed
     * @return constraint matched if and only if all of the enclosed constraints are matched
     */
    public static <T> Constraint<?, ? super T> allOf(@Nonnull final Constraint<?, T> first, final Constraint<?, T>... rest) {
        return new AllOfConstraint(false, first, rest);
    }

    /**
     * @param first first constraint to be enclosed
     * @param rest  remaining constraints to be enclosed
     * @return constraint matched if and only if any of the enclosed constraints are matched
     */
    public static <T> Constraint<?, ? super T> anyOf(@Nonnull final Constraint<?, T> first, final Constraint<?, T>... rest) {
        return new AnyOfConstraint(false, first, rest);
    }

    /**
     * @param bound the bound
     * @return constraint verifying if constrainable value is before predefined bound
     */
    public static <T extends Comparable<? super T>> Constraint<?, T> before(final T bound) {
        return new SimpleConstraint<>("before", Specifications.before(bound));
    }

    /**
     * @return constraint verifying if constrainable value is empty (applicable to Collection, Map or String)
     */
    public static <T> SimpleConstraint<T> empty() {
        return new SimpleConstraint<>("empty", Specifications.empty());
    }

    /**
     * @param upperLimit the upper limit for character sequence length (inclusive)
     * @return constraint verifying if character sequence is limited to predefined number of characters
     */
    @SuppressWarnings("unchecked")
    public static <T extends CharSequence> Constraint<?, T> fitInto(final int upperLimit) {
        return new SimpleConstraint<>("fitInto", Specifications.fitInto(upperLimit));
    }

    /**
     * @param bound the bound
     * @return constraint verifying if constrainable value is greater than given bound
     */
    /*
    public static <T extends BigMoneyProvider & Comparable<? super T>> SimpleConstraint<T> greaterThan(final T bound) {
        return new SimpleConstraint<>("greaterThan", Specifications.after(bound));
    }
    */

    /**
     * @param bound the bound
     * @return constraint verifying if constrainable value is greater than given bound
     */
    public static <T extends Number & Comparable<? super T>> Constraint<?, T> greaterThan(final T bound) {
        return new SimpleConstraint<>("greaterThan", Specifications.after(bound));
    }

    /**
     * @param bound the bound
     * @return constraint verifying if constrainable value is greater than or equal to the given bound
     */
    /*
    public static <T extends BigMoneyProvider & Comparable<? super T>> SimpleConstraint<T> greaterThanOrEqualTo(final T bound) {
        return new SimpleConstraint<>("greaterThanOrEqualTo", new NotSpecification<>(Specifications.before(bound)));
    }
    */

    /**
     * @param bound the bound
     * @return constraint verifying if constrainable value is greater than or equal to the given bound
     */
    public static <T extends Number & Comparable<T>> Constraint<?, T> greaterThanOrEqualTo(final T bound) {
        return new SimpleConstraint<>("greaterThanOrEqualTo", Specifications.before(bound).negate());
    }

    /**
     * @param bound the bound
     * @return constraint verifying if constrainable value is less than given bound
     */
    /*
    public static <T extends BigMoneyProvider & Comparable<? super T>> SimpleConstraint<T> lessThan(final T bound) {
        return new SimpleConstraint<>("lessThan", Specifications.before(bound));
    }
    */

    /**
     * @param value the value
     * @return constraint verifying if constrainable value is equal to predefined value
     */
    public static final <T> Constraint<?, T> isEqual(final T value) {
        return new SimpleConstraint<>("isEqual", Specifications.isEqual(value));
    }

    /**
     * @param bound the bound
     * @return constraint verifying if constrainable value is less than given bound
     */
    public static <T extends Number & Comparable<? super T>> Constraint<?, T> lessThan(final T bound) {
        return new SimpleConstraint<>("lessThan", Specifications.before(bound));
    }

    /**
     * @param bound the bound
     * @return constraint verifying if constrainable value is less than or equal to the given bound
     */
    /*
    public static <T extends BigMoneyProvider & Comparable<? super T>> SimpleConstraint<T> lessThanOrEqualTo(final T bound) {
        return new SimpleConstraint<>("lessThanOrEqualTo", new NotSpecification<>(Specifications.after(bound)));
    }
    */

    /**
     * @param bound the bound
     * @return constraint verifying if constrainable value is less than or equal to the given bound
     */
    public static <T extends Number & Comparable<? super T>> Constraint<?, T> lessThanOrEqualTo(final T bound) {
        return new SimpleConstraint<>("lessThanOrEqualTo", Specifications.after(bound).negate());
    }

    /**
     * Creates and returns the constraint being negation of given constraint.
     *
     * @param constraint the constraint to negate
     * @return newly created constraint being negation of given constraint
     */
    /*
    public static <V> SimpleConstraint<V> not(final SimpleConstraint<V> constraint) {
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
        return new SimpleConstraint<>(builder.toString(), new NotSpecification<>(constraint.getDeterminant()));
    }
    */

    /**
     * @param regex the regular expression
     * @return constraint verifying if constrainable value matches given regular expression
     */
    public static final <T extends CharSequence> Constraint<?, T> matches(final String regex) {
        return new SimpleConstraint<>("regex", Specifications.matches(regex));
    }

    /**
     * @return constraint verifying if character sequence is holding at least one non-whitespace character.
     */
    public static <T extends CharSequence> Constraint<?, T> notBlank() {
        return new SimpleConstraint<>("notBlank", Specifications.notBlank());
    }

    /**
     * @return constraint verifying if string contains valid email address.
     */
    /*
    @SuppressWarnings("unchecked")
    public static final <T extends CharSequence> SimpleConstraint<T> validEmail() {
        return (SimpleConstraint<T>) ValidEmailConstraint.INSTANCE;
    }
    */

    /**
     * @return constraint verifying if required value has been defined (is not {@code null})
     */
    @SuppressWarnings("unchecked")
    public static <T> Constraint<?, T> required() {
        return REQUIRED;
    }

}
