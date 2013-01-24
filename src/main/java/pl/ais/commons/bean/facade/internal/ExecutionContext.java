package pl.ais.commons.bean.facade.internal;

import java.util.EventObject;

import javax.annotation.Nonnull;

/**
 * Defines the API contract for execution context.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
interface ExecutionContext {

    /**
     * @return the {@code PropertyStore property store}
     */
    @Nonnull
    PropertyStore getPropertyStore();

    /**
     * Multicast the event to the appropriate listeners.
     *
     * @param event the event to multicast
     */
    void multicastEvent(@Nonnull EventObject event);

}
