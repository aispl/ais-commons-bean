package pl.ais.commons.bean.validation.constraint;

import org.junit.Assert;
import org.junit.Test;
import pl.ais.commons.bean.validation.Constraint;

import java.time.LocalDate;

import static pl.ais.commons.bean.validation.Constraints.after;
import static pl.ais.commons.bean.validation.Constraints.before;

/**
 * Verifies {@link AllOfConstraint} expectations.
 *
 * @author Warlock, AIS.PL
 * @since 1.4.1
 */
public class AllOfConstraintExpectations {

    @Test
    public void conjunctionOfConstraintsShouldBeSatisfiedIfAllConstraintsAreSatisfied() {

        // Given set of constraints, ...
        final LocalDate today = LocalDate.now();
        final Constraint<LocalDate> inTheFuture = after(today);
        final Constraint<LocalDate> inTwoDays = before(today.plusDays(2));

        // ... satisfied by some value.
        final LocalDate tomorrow = today.plusDays(1);

        // When we build the conjunction of those constraints
        final Constraint<LocalDate> withinAWeek = inTheFuture.and(inTwoDays);

        // Then conjunction of constraints should be satisfied by the same value.
        Assert.assertTrue("Conjunction of constraints should be satisfied, if all of the constraints are satisfied.",
            withinAWeek.test(tomorrow));
    }

    @Test
    public void conjunctionOfConstraintsShouldBeViolatedIfAnyConstraintIsViolated() {

        // Given set of constraints, ...
        final LocalDate today = LocalDate.now();
        final Constraint<LocalDate> inTheFuture = after(today);
        final Constraint<LocalDate> inThePast = before(today);

        // ... satisfied partially by some value.
        final LocalDate tomorrow = today.plusDays(1);

        // When we build the conjunction of those constraints
        final Constraint<LocalDate> conjunction = inTheFuture.and(inThePast);

        // Then conjunction of constraints should be violated by the same value.
        Assert.assertFalse("Conjunction of constraints should be violated, if any of the constraints is violated.",
            conjunction.test(tomorrow));
    }

}
