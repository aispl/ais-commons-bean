package pl.ais.commons.bean.facade.event;

import java.util.EventObject;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Delivered whenever bean property is accessed.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
@Immutable
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public final class PropertyAccessEvent extends EventObject {

    private final String propertyName;

    private final Object propertyValue;

    /**
     * Constructs new instance.
     *
     * @param source the event source (usually bean owning the property)
     * @param propertyName the property name
     * @param propertyValue current property value
     */
    public PropertyAccessEvent(@Nonnull final Object source, @Nonnull final String propertyName,
        @Nullable final Object propertyValue) {
        super(source);
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    /**
     * @return the property name
     */
    @Nonnull
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * @return the property value
     */
    @CheckForNull
    public Object getPropertyValue() {
        return propertyValue;
    }

    /**
     * @see java.util.EventObject#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("propertyName", propertyName).append("propertyValue", propertyValue)
            .build();
    }

}
