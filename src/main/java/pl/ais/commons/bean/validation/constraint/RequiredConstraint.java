package pl.ais.commons.bean.validation.constraint;

import javax.annotation.concurrent.Immutable;

import pl.ais.commons.bean.validation.Constraint;
import pl.ais.commons.domain.specification.Specifications;

/**
 * Constraint verifying if required value has been defined (is not {@code null})
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
@Immutable
public final class RequiredConstraint extends Constraint<Object> {

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
    private RequiredConstraint() {
        super(NAME, Specifications.notNull());
    }
}
