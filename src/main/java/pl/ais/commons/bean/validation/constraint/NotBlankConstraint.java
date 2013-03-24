package pl.ais.commons.bean.validation.constraint;

import javax.annotation.concurrent.Immutable;

import pl.ais.commons.bean.validation.Constraint;
import pl.ais.commons.domain.specification.Specifications;

/**
 * Constraint verifying if character sequence is holding at least one non-whitespace character.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
@Immutable
public final class NotBlankConstraint extends Constraint<CharSequence> {

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
    private NotBlankConstraint() {
        super(NAME, Specifications.notBlank());
    }

}
