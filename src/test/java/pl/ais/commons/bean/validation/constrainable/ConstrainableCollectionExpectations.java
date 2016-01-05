package pl.ais.commons.bean.validation.constrainable;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import static pl.ais.commons.bean.validation.Constraints.lessThan;
import static pl.ais.commons.bean.validation.Constraints.required;
import static pl.ais.commons.bean.validation.constrainable.ConstrainableCollection.allOf;
import static pl.ais.commons.bean.validation.constrainable.ConstrainableCollection.anyOf;

/**
 * @author Warlock, AIS.PL
 * @since 1.3.1
 */
public class ConstrainableCollectionExpectations {

    /**
     * Verifies if constrainable returned by {@link ConstrainableCollection#allOf(Collection)} is matched by some
     * constraint when all elements are matching this constraint.
     */
    @Test
    public void allOfShouldMatchIfAllAreMatching() {
        final Constrainable<?> constrainable = allOf(Arrays.asList(Integer.valueOf(3), "Some String", Integer.valueOf(7), new BigDecimal("14.75")));
        Assert.assertTrue(constrainable.apply(required()));
    }

    /**
     * Verifies if constrainable returned by {@link ConstrainableCollection#allOf(Collection)} is not matched by some
     * constraint when some of the elements is not matching this constraint.
     */
    @Test
    public void allOfShouldNotMatchIfSomeIsNotMatching() {
        final Constrainable<?> constrainable = allOf(Arrays.asList(Integer.valueOf(3), "Some String", null, new BigDecimal("14.75")));
        Assert.assertFalse(constrainable.apply(required()));
    }

    /**
     * Verifies if constrainable returned by {@link ConstrainableCollection#anyOf(Collection)} is matched by some
     * constraint when any of the elements is matching this constraint.
     */
    @Test
    public void anyOfShouldMatchIfAnyIsMatching() {
        final Constrainable<Integer> constrainable = anyOf(Arrays.asList(3, 5, 7, 14));
        Assert.assertTrue(constrainable.apply(lessThan(4)));
    }

    /**
     * Verifies if constrainable returned by {@link ConstrainableCollection#anyOf(Collection)} is not matched by some
     * constraint when all of the elements are not matching this constraint.
     */
    @Test
    public void anyOfShouldNotMatchIfAllAreNotMatching() {
        final Constrainable<Integer> constrainable = anyOf(Arrays.asList(3, 5, 7, 14));
        Assert.assertFalse(constrainable.apply(lessThan(2)));
    }

}
