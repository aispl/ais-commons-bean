package pl.ais.commons.bean.validation;

import javax.annotation.Nonnull;

/**
 * Defines the API contract for validatable value.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
public interface Validatable {

    /**
     * Verifies if this validatable satisfies given constraint.
     *
     * @param constraint constraint which should be satisfied
     * @return {@code true} if constraint is satisfied by this validatable, {@code false} otherwise
     */
    <V, C extends Constraint<V>> boolean satisfies(@Nonnull C constraint);

}
