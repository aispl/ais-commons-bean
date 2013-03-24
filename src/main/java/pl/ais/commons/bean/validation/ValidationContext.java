package pl.ais.commons.bean.validation;

import javax.annotation.Nonnull;

import pl.ais.commons.bean.facade.ObservableFacade;
import pl.ais.commons.bean.facade.event.PropertyAccessTracker;
import pl.ais.commons.bean.validation.event.ValidationListener;

/**
 * Validation context.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
public final class ValidationContext implements AutoCloseable {

    /**
     * Decorates the constrainable to allow its validation against some constraint.
     *
     * @param constrainable the constrainable which will be decorated
     * @return decorated constrainable
     */
    @SuppressWarnings("rawtypes")
    public static Validatable validateThat(final Constrainable constrainable) {
        return new Validatable() {

            /**
             * {@inheritDoc}
             */
            @SuppressWarnings("unchecked")
            @Override
            public <V, C extends Constraint<V>> boolean satisfies(final C constraint) {
                return constraint.isSatisfiedBy(constrainable);
            }

        };
    }

    /**
     * Creates and returns the validation context for given object.
     *
     * @param object the object which will be validated
     * @return newly created validation context
     */
    public static ValidationContext validationOf(final Object object) {
        return new ValidationContext(object);
    }

    private transient ValidationListener[] listeners;

    private final transient Object target;

    private final transient PropertyAccessTracker tracker = new PropertyAccessTracker();

    /**
     * Constructs new instance.
     *
     * @param object the object which will be validated
     */
    private ValidationContext(final Object object) {
        super();
        this.target = object;
        if (target instanceof ObservableFacade) {
            ((ObservableFacade) target).addListener(tracker);
        }
    }

    /**
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() {
        if (target instanceof ObservableFacade) {
            ((ObservableFacade) target).removeListener(tracker);
        }
    }

    /**
     * Provides global validation listeners which will be used if there are no listeners defined
     * at the specific {@link Constrainable} level.
     *
     * @param listeners listeners watching the constraint violations
     * @return this instance (for method invocation chaining)
     */
    @SuppressWarnings("hiding")
    public ValidationContext observedBy(@Nonnull final ValidationListener... listeners) {
        this.listeners = listeners;
        return this;
    }

    /**
     * Returns the nested path leading to the provided value.
     *
     * @param value the value
     * @return the nested path leading to the provided value
     */
    public String pathTo(final Object value) {
        return tracker.getCurrentPath().getRepresentation();
    }

    /**
     * Converts given value into corresponding {@link Constrainable}.
     *
     * @param value the value which will be constrained
     * @return {@link Constrainable} for given value
     */
    public <T> Constrainable<T> property(final T value) {
        Constrainable<T> result;
        if (target instanceof ObservableFacade) {
            result = new ConstrainableProperty<>(value, tracker.getCurrentPath());
        } else {
            result = new ConstrainableValue<>(value);
        }
        return result.observedBy(listeners);
    }

}
