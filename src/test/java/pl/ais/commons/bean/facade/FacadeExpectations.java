package pl.ais.commons.bean.facade;

import org.junit.Test;
import pl.ais.commons.bean.domain.model.DefaultName;
import pl.ais.commons.bean.domain.model.Name;

import static org.junit.Assert.fail;

/**
 * Verifies {@link Facade} expectations.
 *
 * @author Warlock, AIS.PL
 * @since 1.3.3
 */
public class FacadeExpectations {

    /**
     * Verifies if facade created for final class instance, implementing some i-face, will implement same i-face itself.
     */
    @Test
    public void shouldBeAssignableToImplementedIfaceForFinalClass() {
        final Name name = new DefaultName("John", "Smith");
        final TraverseListener traverseListener = new TraverseListener();

        try {
            final Name facade = Facade.over(name, traverseListener);
        } catch (final ClassCastException exception) {
            fail("Created facade does not implement desired i-face.");
        }
    }

}
