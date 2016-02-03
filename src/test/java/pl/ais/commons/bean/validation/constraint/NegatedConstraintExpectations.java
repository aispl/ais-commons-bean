package pl.ais.commons.bean.validation.constraint;

import org.junit.Assert;
import org.junit.Test;
import pl.ais.commons.bean.validation.Constraint;

import java.time.LocalDate;

import static pl.ais.commons.bean.validation.Constraints.after;
import static pl.ais.commons.bean.validation.Constraints.before;
import static pl.ais.commons.bean.validation.Constraints.not;

/**
 * @author Warlock, AIS.PL
 * @since 1.3.2
 */
public class NegatedConstraintExpectations {

    @Test
    public void negationOfSatisfiedConstraintShouldBeViolated() {

        // Given constraint ...
        final LocalDate today = LocalDate.now();
        final Constraint<?, LocalDate> inTheFuture = after(today);

        // ... satisfied by some value.
        final LocalDate tomorrow = today.plusDays(1);

        // When we negate the constraint.
        final Constraint<?, LocalDate> inThePastOrToday = not(inTheFuture);

        // Then negated constraint should be violated by the same value.
        Assert.assertFalse("Negated constraint should be violated, if the original constraint was satisfied.",
            inThePastOrToday.test(tomorrow));
    }

    @Test
    public void negationOfViolatedConstraintShouldBeSatisfied() {

        // Given constraint ...
        final LocalDate today = LocalDate.now();
        final Constraint<?, LocalDate> inThePast = before(today);

        // ... violated by some value.
        final LocalDate tomorrow = today.plusDays(1);

        // When we negate the constraint.
        final Constraint<?, LocalDate> todayOrInTheFuture = not(inThePast);

        // Then negated constraint should be satisfied by the same value.
        Assert.assertTrue("Negated constraint should be satisfied, if the original constraint was violated.",
            todayOrInTheFuture.test(tomorrow));
    }

}
