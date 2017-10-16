package pl.ais.commons.bean.validation.constraint;

import org.junit.Assert;
import org.junit.Test;
import pl.ais.commons.bean.validation.Constraint;

import java.time.LocalDate;

import static pl.ais.commons.bean.validation.Constraints.after;
import static pl.ais.commons.bean.validation.Constraints.before;

/**
 * Verifies {@link AnyOfConstraint} expectations.
 *
 * @author Warlock, AIS.PL
 * @since 1.4.1
 */
public class AnyOfConstraintExpectations {

    @Test
    public void disjunctionOfConstraintsShouldBeSatisfiedIfAnyConstraintIsSatisfied() {

        // Given set of constraints, ...
        final LocalDate today = LocalDate.now();
        final Constraint<LocalDate> inTheFuture = after(today);
        final Constraint<LocalDate> inThePast = before(today);

        // ... and value satisfying some of the constraints.
        final LocalDate tomorrow = today.plusDays(1);

        // When we build the disjunction of those constraints
        final Constraint<LocalDate> disjunction = inTheFuture.or(inThePast);

        // Then disjunction of constraints should be satisfied by the same value.
        Assert.assertTrue("Disjunction of constraints should be satisfied, if any of the constraints were satisfied.",
            disjunction.test(tomorrow));
    }

    @Test
    public void disjunctionOfConstraintsShouldBeViolatedIfAllConstraintsAreViolated() {

        // Given set of constraints
        final LocalDate today = LocalDate.now();
        final Constraint<LocalDate> inTheFuture = after(today);
        final Constraint<LocalDate> inThePast = before(today);

        // When we build the disjunction of those constraints
        final Constraint<LocalDate> disjunction = inTheFuture.or(inThePast);

        // Then disjunction of constraints should be violated by the value violating all constraints.
        Assert.assertFalse("Disjunction of constraints should be violated, if all of the constraints were violated.",
            disjunction.test(today));
    }

}
