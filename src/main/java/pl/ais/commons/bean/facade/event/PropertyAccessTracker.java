package pl.ais.commons.bean.facade.event;

import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.ais.commons.bean.util.Path;

/**
 * Listener tracking the property accessing.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
public class PropertyAccessTracker implements MethodInvocationListener, PropertyAccessListener {

    private transient Path currentPath = Path.ROOT;

    /**
     * @return the current path
     */
    public Path getCurrentPath() {
        return currentPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void methodInvocation(final MethodInvocationEvent event) {
        this.currentPath = Path.ROOT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void propertyAccess(final PropertyAccessEvent event) {
        currentPath = currentPath.nestedPath(event.getPropertyName());
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("currentPath", currentPath).build();
    }

}
