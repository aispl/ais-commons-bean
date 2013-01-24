package pl.ais.commons.bean.validation;

import pl.ais.commons.domain.specification.Specification;

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
    <V, C extends Specification<Constrainable<? extends V>>> boolean satisfies(C constraint);

}
