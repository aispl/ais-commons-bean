package pl.ais.commons.bean.validation.constraints;

import pl.ais.commons.bean.validation.internal.DefaultConstraint;
import pl.ais.commons.domain.specification.Specifications;

/**
 * Constraint verifying if character sequence is limited to predefined number of characters.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
public class FitIntoConstraint extends DefaultConstraint<CharSequence> {

    /**
     * Defines the constraint name.
     */
    public static final String NAME = "fitInto";

    /**
     * Constructs new instance.
     *
     * @param upperLimit the upper limit for character sequence length (inclusive)
     */
    public FitIntoConstraint(final int upperLimit) {
        super(NAME, Specifications.fitInto(upperLimit));
    }

}
