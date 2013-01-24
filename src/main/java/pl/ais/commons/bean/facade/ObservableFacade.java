package pl.ais.commons.bean.facade;

import java.util.EventListener;
import java.util.EventObject;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Defines the API contract for observable facade.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
public interface ObservableFacade {

    /**
     * Registers given listener as events' receiver.
     *
     * @param listener the listener to register
     */
    void addListener(@Nonnull final EventListener listener);

    /**
     * Returns parent of this observable facade.
     *
     * @return parent observable facade (can be {@code null})
     */
    @CheckForNull
    ObservableFacade getParent();

    /**
     * Multicast the event to the appropriate event listeners.
     *
     * @param event the event to multicast
     */
    void multicastEvent(@Nonnull final EventObject event);

    /**
     * Removes all currently registered listeners.
     */
    void removeAllListeners();

    /**
     * Removes given listener from the set of events' receivers.
     *
     * @param listener the listener
     */
    void removeListener(@Nonnull final EventListener listener);

    /**
     * Sets the parent facade.
     *
     * @param parent the parent facade
     */
    void setParent(@Nullable ObservableFacade parent);

}
