package pl.ais.commons.bean.facade.internal;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.ais.commons.bean.facade.ObservableFacade;
import pl.ais.commons.bean.facade.event.MethodInvocationEvent;
import pl.ais.commons.bean.facade.event.MethodInvocationListener;
import pl.ais.commons.bean.facade.event.PropertyAccessEvent;
import pl.ais.commons.bean.facade.event.PropertyAccessListener;

/**
 * Default implementation of {@link ObservableFacade}.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
final class EventMulticaster implements ObservableFacade {

    interface EventHandlerAdapter {

        void handleEvent(EventObject event, EventListener listener);

    }

    /**
     * Defines mapping between event listener class and event class supported by the listener.
     */
    private static final Map<Class<? extends EventListener>, Class<? extends EventObject>> EVENT_MAPPING;

    private static final Map<Class<? extends EventObject>, EventHandlerAdapter> HANDLER_MAPPING;

    private static final Collection<Class<? extends EventObject>> PROPAGABLE;

    static {

        // Initialize listener to event mapping.
        Map<Class<? extends EventListener>, Class<? extends EventObject>> eventMapping = new HashMap<>();
        eventMapping.put(MethodInvocationListener.class, MethodInvocationEvent.class);
        eventMapping.put(PropertyAccessListener.class, PropertyAccessEvent.class);
        eventMapping.put(PropertyChangeListener.class, PropertyChangeEvent.class);
        EVENT_MAPPING = Collections.unmodifiableMap(eventMapping);

        // Initialize event to handler mapping.
        Map<Class<? extends EventObject>, EventHandlerAdapter> handlerMapping = new HashMap<>();
        handlerMapping.put(MethodInvocationEvent.class, new EventHandlerAdapter() {

            @Override
            public void handleEvent(final EventObject event, final EventListener listener) {
                ((MethodInvocationListener) listener).methodInvocation((MethodInvocationEvent) event);
            }
        });
        handlerMapping.put(PropertyAccessEvent.class, new EventHandlerAdapter() {

            @Override
            public void handleEvent(final EventObject event, final EventListener listener) {
                ((PropertyAccessListener) listener).propertyAccess((PropertyAccessEvent) event);
            }
        });
        handlerMapping.put(PropertyChangeEvent.class, new EventHandlerAdapter() {

            @Override
            public void handleEvent(final EventObject event, final EventListener listener) {
                ((PropertyChangeListener) listener).propertyChange((PropertyChangeEvent) event);
            }
        });
        HANDLER_MAPPING = Collections.unmodifiableMap(handlerMapping);

        // Initialize the propagable events.
        Set<Class<? extends EventObject>> propagable = new HashSet<>();
        propagable.add(PropertyAccessEvent.class);
        propagable.add(PropertyChangeEvent.class);
        PROPAGABLE = Collections.unmodifiableSet(propagable);
    }

    @Nonnull
    private static final Collection<Class<? extends EventObject>> determineEventClasses(
        @Nonnull final EventListener listener) {
        final Class<? extends EventListener> listenerClass = listener.getClass();
        List<Class<? extends EventObject>> result = new ArrayList<>();

        for (Class<? extends EventListener> key : EVENT_MAPPING.keySet()) {
            if (key.isAssignableFrom(listenerClass)) {
                result.add(EVENT_MAPPING.get(key));
            }
        }
        if (result.isEmpty()) {
            throw new IllegalArgumentException("Unsupported event listener type.");
        }
        return result;
    }

    private final Map<Class<? extends EventObject>, Collection<EventListener>> listenerMapping = new HashMap<>();

    private ObservableFacade parent;

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener(@Nonnull final EventListener listener) {

        // Determine the event classes handled by the listener first, ...
        final Collection<Class<? extends EventObject>> eventClasses = determineEventClasses(listener);

        // ... and add the listener for the event class.
        synchronized (listenerMapping) {
            for (Class<? extends EventObject> eventClass : eventClasses) {
                Collection<EventListener> listeners = listenerMapping.get(eventClass);
                if (null == listeners) {
                    listeners = new LinkedHashSet<>();
                    listenerMapping.put(eventClass, listeners);
                }
                if (false == listeners.contains(listener)) {
                    listeners.add(listener);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObservableFacade getParent() {
        return parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void multicastEvent(@Nonnull final EventObject event) {
        final Class<? extends EventObject> eventClass = event.getClass();

        // Propagate the event to parent facade first (if possible), ...
        if ((null != parent) && PROPAGABLE.contains(eventClass)) {
            parent.multicastEvent(event);
        }

        // ... and handle it itself.
        final EventHandlerAdapter adapter = HANDLER_MAPPING.get(eventClass);
        synchronized (listenerMapping) {
            final Collection<EventListener> listeners = listenerMapping.get(eventClass);
            if (null != listeners) {
                for (EventListener listener : listeners) {
                    adapter.handleEvent(event, listener);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAllListeners() {
        synchronized (listenerMapping) {
            listenerMapping.clear();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListener(@Nonnull final EventListener listener) {
        // Determine the event classes handled by the listener first, ...
        final Collection<Class<? extends EventObject>> eventClasses = determineEventClasses(listener);

        // ... and remove the listener.
        synchronized (listenerMapping) {
            for (Class<? extends EventObject> eventClass : eventClasses) {
                final Collection<EventListener> listeners = listenerMapping.get(eventClass);
                if (null != listeners) {
                    listeners.remove(listener);
                    if (listeners.isEmpty()) {
                        listenerMapping.remove(eventClass);
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setParent(final ObservableFacade parent) {
        this.parent = parent;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("parent", parent).append("listenerMapping", listenerMapping).build();
    }

}
