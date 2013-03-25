package pl.ais.commons.bean.facade.internal;

import java.util.Map;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Defines the API contract for property store.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
interface PropertyStore {

    /**
     * Returns unmodifiable view of all properties.
     *
     * @return unmodifiable view of all properties
     */
    Map<String, Object> getProperties();

    /**
     * Returns the property value.
     *
     * @param propertyName the property name
     * @return the property value
     */
    @CheckForNull
    Object getPropertyValue(@Nonnull final String propertyName);

    /**
     * Sets the property value.
     *
     * @param propertyName the property name
     * @param value the value to set
     */
    void setPropertyValue(@Nonnull String propertyName, @Nullable Object value);

}