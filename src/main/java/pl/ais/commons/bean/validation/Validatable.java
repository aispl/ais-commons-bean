package pl.ais.commons.bean.validation;

import javax.annotation.Nonnull;

/**
 * Defines the API contract for validatable value.
 *
 * @param <T> the type of the validatable values
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
public interface Validatable<T> {

    /**
     * Verifies if this validatable satisfies given constraint.
     *
     * @param first constraint which should be satisfied
     * @param rest  remaining constraints which should be satisfied
     * @return {@code true} if constraint is satisfied by this validatable, {@code false} otherwise
     */
    boolean satisfies(@Nonnull Constraint<?, ? super T> first, Constraint<?, ? super T>... rest);

}
