package pl.ais.commons.bean.validation;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import pl.ais.commons.domain.specification.Specification;

/**
 * Defines the API contract for constraint.
 *
 * <p>
 *     <strong>Note:</strong>
 *     Although constraint extends {@link Specification}, it differs from majority of specifications, because calling
 *     its {@link Specification#isSatisfiedBy(Object) isSatisfiedBy} method may in turn trigger Constrainable's
 *     {@link Constrainable#notifyAboutViolation(pl.ais.commons.bean.validation.event.ConstraintViolationEvent) notifyAboutViolation}
 *     method, thus using constraint for building composite specifications may not be a good idea.
 *     In such cases you should use {@link #getDeterminant()} method result.
 * </p>
 *
 * @param <V> determines the type of constrainable value
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
public interface Constraint<V> extends Specification<Constrainable<? extends V>> {

    /**
     * Returns the constraint determinant.
     *
     * @return the constraint determinant
     */
    Specification<V> getDeterminant();

    /**
     * Returns the message bound to this constraint.
     *
     * @return the message bound to this constraint (can be {@code null})
     */
    @CheckForNull
    String getMessage();

    /**
     * Returns the parameters of message bound to this constraint.
     *
     * @return the parameters of message bound to this constraint
     */
    @CheckForNull
    Object[] getMessageParameters();

    /**
     * Returns the name of this constraint.
     *
     * @return the name of this constraint
     */
    @Nonnull
    String getName();

    /**
     * Provides the constraint (de)activating method.
     *
     * @param active defines if this constraint is active
     * @return constraint instance (for method invocation chaining)
     */
    Constraint<V> when(boolean active);

    /**
     * Provides the message (potentially parameterized) which should be used to describe the constraint.
     *
     * @param message the message (potentially parameterized)
     * @param parameters the message parameters (if any)
     * @return constraint instance (for method invocation chaining)
     */
    @Nonnull
    Constraint<V> withDescription(@Nonnull String message, Object... parameters);

}
