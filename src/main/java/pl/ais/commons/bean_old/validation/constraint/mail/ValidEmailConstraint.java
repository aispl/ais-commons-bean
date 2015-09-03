package pl.ais.commons.bean_old.validation.constraint.mail;

import pl.ais.commons.bean_old.validation.Constraint;
import pl.ais.commons.domain.specification.Specifications;

/**
 * Constraint verifying if string contains valid email address.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
public class ValidEmailConstraint extends Constraint<CharSequence> {

    /**
     * Defines singleton instance of {@link ValidEmailConstraint}.
     */
    public static final Constraint<CharSequence> INSTANCE = new ValidEmailConstraint();

    /**
     * Defines the constraint name.
     */
    public static final String NAME = "validEmail";

    /**
     * Constructs new instance.
     */
    public ValidEmailConstraint() {
        super(NAME, Specifications.validEmail());
    }

}
