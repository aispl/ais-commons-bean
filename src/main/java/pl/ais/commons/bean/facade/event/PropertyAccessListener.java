package pl.ais.commons.bean.facade.event;

import java.util.EventListener;

import javax.annotation.Nonnull;

/**
 * Defines the API contract for the property access listener.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
public interface PropertyAccessListener extends EventListener {

    /**
     * This method is called on each property access.
     *
     * @param event describes performed property access
     */
    void propertyAccess(@Nonnull PropertyAccessEvent event);

}
