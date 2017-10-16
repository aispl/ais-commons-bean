package pl.ais.commons.bean.validation;

import org.javamoney.moneta.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static pl.ais.commons.bean.validation.Constraints.greaterThan;

/**
 * Verifies {@link Constraints#greaterThan(Comparable)} expectations.
 *
 * @author Warlock, AIS.PL
 * @since 1.4.1
 */
@RunWith(Parameterized.class)
public class GreaterThanConstraintExpectations {

    private final Comparable candidate;

    private final Constraint constraint;

    private final boolean satisfied;

    public <T extends Comparable<T>> GreaterThanConstraintExpectations(final String description,
                                                                       final T candidate, final T boundary,
                                                                       final boolean satisfied) {
        this.candidate = candidate;
        constraint = greaterThan(boundary);
        this.satisfied = satisfied;
    }

    @Parameters(name = "{0}")
    public static Object[][] parameters() {
        return new Object[][] {
            new Object[] {
                "Type: Integer, -100 000 000 > -100 000 000 -> constraint not satisfied",
                Integer.valueOf(-100_000_000), Integer.valueOf(-100_000_000), false
            },
            new Object[] {
                "Type: Big Decimal, -100 000 000 000 000 > -100 000 000 000 000 -> constraint not satisfied",
                BigDecimal.valueOf(-100_000_000_000_000.0), BigDecimal.valueOf(-100_000_000_000_000.0), false
            },
            new Object[] {
                "Type: Monetary Amount, -100 000 000 000 000 > -100 000 000 000 000 -> constraint not satisfied",
                Money.of(-100_000_000_000_000.0, "EUR"), Money.of(-100_000_000_000_000.0, "EUR"), false
            },
            new Object[] {
                "Type: Integer, -100 000 > -100 000 000 -> constraint satisfied",
                Integer.valueOf(-100_000), Integer.valueOf(-100_000_000), true
            },
            new Object[] {
                "Type: Big Decimal, -100 000 000 000 > -100 000 000 000 000 -> constraint satisfied",
                BigDecimal.valueOf(-100_000_000_000.0), BigDecimal.valueOf(-100_000_000_000_000.0), true
            },
            new Object[] {
                "Type: Monetary Amount, -100 000 000 000 > -100 000 000 000 000 -> constraint satisfied",
                Money.of(-100_000_000_000.0, "EUR"), Money.of(-100_000_000_000_000.0, "EUR"), true
            }
        };
    }

    @Test
    public void verifyExpectedBehavior() {
        assertEquals(satisfied, constraint.test(candidate));
    }

}
