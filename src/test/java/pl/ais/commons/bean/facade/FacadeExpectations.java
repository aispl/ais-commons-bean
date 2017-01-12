package pl.ais.commons.bean.facade;

import org.junit.Test;
import pl.ais.commons.bean.domain.model.DefaultName;
import pl.ais.commons.bean.domain.model.Name;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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

    /**
     * Verifies if fields declared by the class instance are copied to the created facade.
     */
    @Test
    public void shouldCopyInstanceFieldsIntoFacade() {
        final ValueHolder holder = new ValueHolder();
        holder.setInheritedValue("Kettenkraftrad");
        holder.setInstanceValue("Panzerkampfwagen");

        final ValueHolder nestedInstance = new ValueHolder();
        nestedInstance.setInstanceValue("Sonderkraftfahrzeug");
        holder.setNestedInstance(nestedInstance);

        final ValueHolder facade = Facade.over(holder, new TraverseListener());
        assertEquals(facade.instanceValue, holder.instanceValue);
        assertEquals(facade.nestedInstance, holder.nestedInstance);
        assertNull(((BaseValueHolder) facade).inheritedValue);
    }

    protected static class BaseValueHolder {

        private String inheritedValue;

        public String getInheritedValue() {
            return inheritedValue;
        }

        public void setInheritedValue(final String inheritedValue) {
            this.inheritedValue = inheritedValue;
        }

    }

    public static class ValueHolder extends BaseValueHolder {

        private String instanceValue;

        private ValueHolder nestedInstance;

        @Override
        public boolean equals(final Object object) {
            boolean result = (this == object);
            if (!result && (object instanceof ValueHolder)) {
                final ValueHolder other = (ValueHolder) object;
                result = Objects.equals(instanceValue, other.instanceValue) && Objects.equals(nestedInstance, other.nestedInstance);
            }
            return result;
        }

        public String getInstanceValue() {
            return instanceValue;
        }

        public ValueHolder getNestedInstance() {
            return nestedInstance;
        }

        @Override
        public int hashCode() {
            return Objects.hash(instanceValue, nestedInstance);
        }

        public void setInstanceValue(final String instanceValue) {
            this.instanceValue = instanceValue;
        }

        public void setNestedInstance(final ValueHolder nestedInstance) {
            this.nestedInstance = nestedInstance;
        }
    }

}
