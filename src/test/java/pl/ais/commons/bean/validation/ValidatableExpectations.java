package pl.ais.commons.bean.validation;

import org.junit.Test;
import pl.ais.commons.bean.domain.model.Activity;

import static org.junit.Assert.assertFalse;
import static pl.ais.commons.bean.domain.model.Activity.anActivity;
import static pl.ais.commons.bean.validation.Constraints.required;
import static pl.ais.commons.bean.validation.ValidationContext.validationOf;

/**
 * Verifies {@link Validatable} expectations.
 *
 * @author Warlock, AIS.PL
 * @since 1.0
 */
public class ValidatableExpectations {

    private static Constraint<String> willThrowNPEXForNullCandidate() {
        return Constraints.constraint("my", candidate -> {throw new NullPointerException("Ha!"); });
    }

    @Test
    public void satisfiesShouldStopEvaluatingConstraintsOnFirstViolation() {
        final Activity activity = anActivity().get();

        try (final ValidationContext<Activity> validateThat = validationOf(activity)) {
            final boolean satisfied = validateThat.valueOf(activity.getName())
                                                  .satisfies(required(), willThrowNPEXForNullCandidate());
            assertFalse(satisfied);
        }
    }

}
