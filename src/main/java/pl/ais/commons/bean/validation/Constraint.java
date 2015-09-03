package pl.ais.commons.bean.validation;

import pl.ais.commons.bean.validation.event.ConstraintViolated;
import pl.ais.commons.bean.validation.event.ValidationListener;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * Defines the API contract for the Constraint.
 *
 * @param <C> the type of constraint
 * @param <T> the type of values supported by the constraint
 * @author Warlock, AIS.PL
 * @since 1.2.1
 */
public interface Constraint<C extends Constraint<C, T>, T> extends BiFunction<Constrainable<T>, ValidationListener, Boolean>, Predicate<T> {

    /**
     * Verifies if given constrainable matches the constraint and reports violation if needed.
     *
     * @param constrainable the constrainable to be checked
     * @param listener      the validation listener observing constraint violations
     * @return {@code true} if given constrainable matches the constraint, {@code false} otherwise
     */
    @Nonnull
    default Boolean apply(final Constrainable<T> constrainable, final ValidationListener listener) {
        boolean matched = test(constrainable.getValue());
        if (!matched) {
            listener.constraintViolated(new ConstraintViolated(this, constrainable));
        }
        return matched;
    }

    /**
     * Indicates if this constraint is active.
     *
     * @return {@code true} if this constraint is active, {@code false} otherwise
     */
    boolean isActive();

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
    C when(boolean active);

    /**
     * Provides the message (potentially parametrized) which should be used to describe the constraint.
     *
     * @param message           the message (potentially parametrized)
     * @param messageParameters the message parameters (if any)
     * @return constraint instance (for method invocation chaining)
     */
    @Nonnull
    C withDescription(@Nonnull String message, Object... messageParameters);

    /**
     * Provides the message parameters which should be used to describe the constraint.
     *
     * @param messageParameters the message parameters (if any)
     * @return constraint instance (for method invocation chaining)
     */
    @Nonnull
    C withMessageParameters(Object... messageParameters);

}
