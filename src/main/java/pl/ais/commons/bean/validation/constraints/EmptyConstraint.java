package pl.ais.commons.bean.validation.constraints;

import pl.ais.commons.bean.validation.Constraint;
import pl.ais.commons.domain.specification.Specifications;

/**
 * Constraint verifying if candidate is empty (applicable to Collection, Map or String).
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
public final class EmptyConstraint extends BaseConstraint<Object> {

    /**
     * Defines singleton instance of {@link EmptyConstraint}.
     */
    public static final Constraint<?> INSTANCE = new EmptyConstraint();

    /**
     * Defines the constraint name.
     */
    public static final String NAME = "empty";

    /**
     * Constructs new instance.
     */
    public EmptyConstraint() {
        super(NAME, Specifications.empty());
    }
}
