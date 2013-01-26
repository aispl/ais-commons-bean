package pl.ais.commons.bean.validation.constraints;

import pl.ais.commons.bean.validation.Constraint;
import pl.ais.commons.domain.specification.Specifications;

/**
 * Constraint verifying if character sequence is holding at least one non-whitespace character.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
public final class NotBlankConstraint extends BaseConstraint<CharSequence> {

    /**
     * Defines singleton instance of {@link NotBlankConstraint}.
     */
    public static final Constraint<CharSequence> INSTANCE = new NotBlankConstraint();

    /**
     * Defines the constraint name.
     */
    public static final String NAME = "notBlank";

    /**
     * Constructs new instance.
     */
    public NotBlankConstraint() {
        super(NAME, Specifications.notBlank());
    }
}
