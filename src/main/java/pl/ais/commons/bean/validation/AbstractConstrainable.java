package pl.ais.commons.bean.validation;

import javax.annotation.Nullable;

import pl.ais.commons.bean.validation.event.ConstraintViolationEvent;
import pl.ais.commons.bean.validation.event.ValidationListener;

/**
 * Base class for {@link Constrainable} implementations.
 *
 * @param <V> determines the type of constrainable value
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
abstract class AbstractConstrainable<V> implements Constrainable<V> {

    private transient ValidationListener[] listeners;

    private transient final Object value;

    /**
     * Constructs new instance.
     *
     * @param value constrained value
     */
    protected AbstractConstrainable(@Nullable final Object value) {
        super();
        this.value = value;
    }

    /**
     * @return the constrained value
     */
    @SuppressWarnings("unchecked")
    @Override
    public V getValue() {
        return (V) value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyAboutViolation(final ConstraintViolationEvent event) {
        for (ValidationListener listener : listeners) {
            listener.constraintViolated(event);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("hiding")
    @Override
    public Constrainable<V> observedBy(final ValidationListener... listeners) {
        this.listeners = listeners;
        return this;
    }

}
