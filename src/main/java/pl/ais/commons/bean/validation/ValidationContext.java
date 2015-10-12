package pl.ais.commons.bean.validation;

import pl.ais.commons.bean.facade.Facade;
import pl.ais.commons.bean.facade.TraverseListener;
import pl.ais.commons.bean.validation.constrainable.Constrainable;
import pl.ais.commons.bean.validation.constrainable.ConstrainableCollection;
import pl.ais.commons.bean.validation.constrainable.ConstrainableValue;
import pl.ais.commons.bean.validation.event.ConstraintViolated;
import pl.ais.commons.bean.validation.event.ValidationListener;
import pl.ais.commons.domain.specification.Specifications;

import javax.annotation.Nonnull;
import java.util.Arrays;

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
        super();
        traverseListener = new TraverseListener();
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
    public <V> Validatable<V> allOf(final V first, final V second, final V... rest) {
        final Constrainable<V> constrainable = ConstrainableCollection.allOf(first, second, rest);
        return validatable(constrainable);
    }

    /**
     * Decorates given values to allow validation of any of them against some constraint.
     *
     * @param first  first constrainable value
     * @param second second constrainable value
     * @param rest   remaining constrainable values
     * @return decorated collection of values
     */
    public <V> Validatable<V> anyOf(final V first, final V second, final V... rest) {
        final Constrainable<V> constrainable = ConstrainableCollection.anyOf(first, second, rest);
        return validatable(constrainable);
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
        Arrays.stream(listeners).forEachOrdered(listeners -> listeners.constraintViolated(event));
    }

    /**
     * Provides global validation2 listeners which will be used if there are no listeners defined
     * at the specific {@link ConstrainableValue} level.
     *
     * @param listeners listeners watching the constraint violations
     * @return this instance (for method invocation chaining)
     */
    @SuppressWarnings("hiding")
    public ValidationContext<T> observedBy(@Nonnull final ValidationListener... listeners) {
        this.listeners = Arrays.copyOf(listeners, listeners.length);
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

    private <V> Validatable<V> validatable(final Constrainable<V> constrainable) {
        final ValidationListener listener = this;
        return new Validatable<V>() {

            @Override
            public Constrainable<V> get() {
                return constrainable;
            }

            @Override
            public boolean satisfies(@Nonnull final Constraint<?, ? super V> first, final Constraint<?, ? super V>... rest) {
                return first.apply(constrainable, listener)
                    && Arrays.stream(rest)
                             .map(constraint -> constraint.apply(constrainable, listener))
                             .allMatch(Specifications.isEqual(true));
            }
        };
    }

    /**
     * Decorates given value to allow its validation against some constraint.
     *
     * @param value the value which will be constrained
     * @return decorated value
     */
    public <V> Validatable<V> valueOf(final V value) {
        final Constrainable<V> constrainable = new ConstrainableValue<>(traverseListener.asPath(), value);
        return validatable(constrainable);
    }

}
