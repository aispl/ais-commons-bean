package pl.ais.commons.bean.validation;

import pl.ais.commons.bean.validation.constrainable.Constrainable;
import pl.ais.commons.bean.validation.event.ConstraintViolated;
import pl.ais.commons.bean.validation.event.ValidationListener;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.BiFunction;

/**
 * Defines the API contract for the Constraint.
 *
 * @param <T> the type of values supported by the constraint
 * @author Warlock, AIS.PL
 * @since 1.2.1
 */
public interface Constraint<T> extends BiFunction<Constrainable<? extends T>, ValidationListener, Boolean> {

    /**
     * Creates and returns composite constraint being conjunction of this and provided constraint.
     *
     * @param other constraint to be used for building conjunction with this one
     * @return constraint being conjunction of this and provided constraint
     */
    Constraint<T> and(@Nonnull final Constraint<T> other);

    /**
     * Verifies if given constrainable matches the constraint and reports violation if needed.
     *
     * @param constrainable the constrainable to be checked
     * @param listener      the validation listener observing constraint violations
     * @return {@code true} if given constrainable matches the constraint, {@code false} otherwise
     */
    @Override
    @Nonnull
    default Boolean apply(final Constrainable<? extends T> constrainable, final ValidationListener listener) {
        final boolean matched = constrainable.apply(this);
        if (!matched) {
            listener.constraintViolated(new ConstraintViolated(this, constrainable));
        }
        return matched;
    }

    /**
     * @return the message which should be used to describe this constraint
     */
    String getMessage();

    /**
     * @return the message parameters
     */
    Object[] getMessageParameters();

    /**
     * @return name of this constraint
     */
    String getName();

    /**
     * Negates the name of the constraint by adding/removing 'not' at the beginning of the name.
     *
     * @return negated constraint name
     */
    default String getNegatedName() {
        char[] name = getName().toCharArray();
        final StringBuilder builder = new StringBuilder();

        // ... by cutting off / adding the 'not' prefix, ...
        if ((name.length >= 3) && ('n' == name[0]) && ('o' == name[1]) && ('t' == name[2])) {
            name = Arrays.copyOfRange(name, 3, name.length);
        } else {
            builder.append("not");
        }

        name[0] = Character.toUpperCase(name[0]);
        builder.append(name);
        return builder.toString();
    }

    /**
     * Indicates if this constraint is active.
     *
     * @return {@code true} if this constraint is active, {@code false} otherwise
     */
    boolean isActive();

    /**
     * Creates and returns constraint being negation of this one.
     *
     * @return constraint being negation of this one
     */
    Constraint<T> negate();

    /**
     * Creates and returns composite constraint being disjunction of this one and provided constraint.
     *
     * @param other constraint to be used for building disjunction with this one
     * @return constraint being disjunction of this one and provided constraint
     */
    Constraint<T> or(@Nonnull final Constraint<T> other);

    /**
     * Evaluates this constraint on the given argument.
     *
     * <p><strong>Note:</strong> disabled constraints are matched by any candidates. Use {@link #isActive()} method to
     * properly handle this situation.</p>
     *
     * @param candidate value to be matched against this constraint
     * @return {@code true} if given argument matches this constraint, {@code false} otherwise
     */
    boolean test(@Nullable T candidate);

    /**
     * Provides the constraint (de)activating method.
     *
     * @param active defines if this constraint is active
     * @return constraint instance (for method invocation chaining)
     */
    @Nonnull
    Constraint<T> when(boolean active);

    /**
     * Provides the message (potentially parametrized) which should be used to describe the constraint.
     *
     * @param message           the message (potentially parametrized)
     * @param messageParameters the message parameters (if any)
     * @return constraint instance (for method invocation chaining)
     */
    @Nonnull
    Constraint<T> withDescription(@Nonnull String message, Object... messageParameters);

    /**
     * Provides the message parameters which should be used to describe the constraint.
     *
     * @param messageParameters the message parameters (if any)
     * @return constraint instance (for method invocation chaining)
     */
    @Nonnull
    Constraint<T> withMessageParameters(Object... messageParameters);

}
