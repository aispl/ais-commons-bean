package pl.ais.commons.bean.validation;

import pl.ais.commons.bean.facade.Facade;
import pl.ais.commons.bean.facade.TraverseListener;
import pl.ais.commons.bean.validation.constrainable.Constrainable;
import pl.ais.commons.bean.validation.constrainable.ConstrainableGroup;
import pl.ais.commons.bean.validation.constrainable.ConstrainableValue;
import pl.ais.commons.bean.validation.event.ConstraintViolated;
import pl.ais.commons.bean.validation.event.ValidationListener;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

import static pl.ais.commons.bean.validation.Validatable.validatable;

/**
 * Validation context.
 *
 * @param <T> determines the type of validated object
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
@SuppressWarnings("PMD.TooManyMethods")
public final class ValidationContext<T> implements AutoCloseable, ValidationListener {

    private final T target;

    private final TraverseListener traverseListener;

    private ValidationListener[] listeners;

    /**
     * Constructs new instance.
     *
     * @param object the object which will be validated
     */
    private ValidationContext(final T object) {
        this(object, null, null);
    }

    private ValidationContext(final T object, final String basePath, final ValidationListener[] listeners) {
        super();
        traverseListener = new TraverseListener(basePath);

        this.listeners = (null == listeners) ? null : listeners.clone();
        target = Facade.over(object, traverseListener);
    }

    /**
     * Creates and returns the validation context for given object.
     *
     * @param <B>    type of the object to be validated
     * @param object the object which will be validated
     * @return newly created validation context
     */
    public static <B> ValidationContext<B> validationOf(final B object) {
        return new ValidationContext<>(object);
    }

    /**
     * Decorates given values to allow validation of all of them against some constraint.
     *
     * @param first  first constrainable value
     * @param second second constrainable value
     * @param rest   remaining constrainable values
     * @return decorated collection of values
     */
    @SafeVarargs
    @SuppressWarnings("PMD.UnnecessaryFinalModifier")
    public final <V> Validatable<V> allOf(final V first, final V second, final V... rest) {
        final Constrainable<V> constrainable = ConstrainableGroup.allOf(first, second, rest);
        return validatable(constrainable, this, traverseListener::reset);
    }

    /**
     * Decorates given values to allow validation of any of them against some constraint.
     *
     * @param first  first constrainable value
     * @param second second constrainable value
     * @param rest   remaining constrainable values
     * @return decorated collection of values
     */
    @SafeVarargs
    @SuppressWarnings("PMD.UnnecessaryFinalModifier")
    public final <V> Validatable<V> anyOf(final V first, final V second, final V... rest) {
        final Constrainable<V> constrainable = ConstrainableGroup.anyOf(first, second, rest);
        return validatable(constrainable, this, traverseListener::reset);
    }

    /**
     * @see AutoCloseable#close()
     */
    @Override
    public void close() {
        // Do nothing ...
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void constraintViolated(@Nonnull final ConstraintViolated event) {
        if (null != listeners) {
            Arrays.stream(listeners)
                  .forEachOrdered(listener -> listener.constraintViolated(event));
        }
    }

    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    public <V> void forEach(final Collection<V> elements, final Consumer<ValidationContext<V>> delegate) {
        final String basePath = traverseListener.asPath();
        final Iterator<V> element = elements.iterator();
        for (int i = 0; element.hasNext(); i++) {
            try (final ValidationContext<V> validateThat = new ValidationContext<>(element.next(), String.format("%s[%d]", basePath, i), listeners)) {
                delegate.accept(validateThat);
            }
        }
    }

    public <V> void forEach(final Map<?, V> map, final Consumer<ValidationContext<V>> delegate) {
        final String basePath = traverseListener.asPath();
        map.forEach((key, value) -> {
            try (final ValidationContext<V> validateThat = new ValidationContext<>(value, String.format("%s['%s']", basePath, key), listeners)) {
                delegate.accept(validateThat);
            }
        });
    }

    /**
     * Registers listeners interested in watching validation errors.
     *
     * @param first first validation listener
     * @param rest  the rest of validation listeners
     * @return this instance (for method invocation chaining)
     */
    @SuppressWarnings("hiding")
    public ValidationContext<T> observedBy(final ValidationListener first, final ValidationListener... rest) {
        listeners = new ValidationListener[1 + rest.length];
        listeners[0] = first;
        System.arraycopy(rest, 0, listeners, 1, rest.length);
        return this;
    }

    /**
     * Returns the nested path leading to the provided value.
     *
     * @param value the value
     * @return the nested path leading to the provided value
     */
    public String pathTo(final Object value) {
        return traverseListener.asPath();
    }

    /**
     * Returns the validation subject.
     *
     * @return the validation subject
     */
    public T subject() {
        return target;
    }

    /**
     * Decorates given value to allow its validation against some constraint.
     *
     * @param value the value which will be constrained
     * @return decorated value
     */
    public <V> Validatable<V> valueOf(final V value) {
        final Constrainable<V> constrainable = new ConstrainableValue<>(traverseListener.asPath(), value);
        return validatable(constrainable, this, traverseListener::reset);
    }

}
