package pl.ais.commons.bean.validation.constraints;

import pl.ais.commons.bean.validation.Constraint;
import pl.ais.commons.bean.validation.internal.DefaultConstraint;
import pl.ais.commons.domain.specification.Specifications;

/**
 * Constraint verifying if required value has been defined (is not {@code null})
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
public final class RequiredConstraint extends DefaultConstraint<Object> {

    /**
     * Defines singleton instance of {@link RequiredConstraint}.
     */
    public static final Constraint<?> INSTANCE = new RequiredConstraint();

    /**
     * Defines the constraint name.
     */
    public static final String NAME = "required";

    /**
     * Constructs new instance.
     */
    public RequiredConstraint() {
        super(NAME, Specifications.notNull());
    }
}
