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
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
@SuppressWarnings("PMD.TooManyMethods")
public final class ValidationContext implements AutoCloseable, ValidationListener {

    private final Object target;

    private final TraverseListener traverseListener;

    private ValidationListener[] listeners;

    /**
     * Constructs new instance.
     *
     * @param object the object which will be validated
     */
    private ValidationContext(final Object object) {
        super();
        traverseListener = new TraverseListener();
        target = Facade.over(object, traverseListener);
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

    /**
     * Decorates given values to allow validation of all of them against some constraint.
     *
     * @param <T>    the type of constrainable values
     * @param first  first constrainable value
     * @param second second constrainable value
     * @param rest   remaining constrainable values
     * @return decorated collection of values
     */
    public <T> Validatable<T> allOf(final T first, final T second, final T... rest) {
        final Constrainable<T> constrainable = ConstrainableCollection.allOf(first, second, rest);
        return validatable(constrainable);
    }

    /**
     * Decorates given values to allow validation of any of them against some constraint.
     *
     * @param <T>    the type of constrainable values
     * @param first  first constrainable value
     * @param second second constrainable value
     * @param rest   remaining constrainable values
     * @return decorated collection of values
     */
    public <T> Validatable<T> anyOf(final T first, final T second, final T... rest) {
        final Constrainable<T> constrainable = ConstrainableCollection.anyOf(first, second, rest);
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
    public ValidationContext observedBy(@Nonnull final ValidationListener... listeners) {
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
     * @param <T> the type of validation subject
     * @return the validation subject
     */
    public <T> T subject() {
        return (T) target;
    }

    private <T> Validatable<T> validatable(final Constrainable<T> constrainable) {
        final ValidationListener listener = this;
        return new Validatable<T>() {

            @Override
            public Constrainable<T> get() {
                return constrainable;
            }

            @Override
            public boolean satisfies(@Nonnull final Constraint<?, ? super T> first, final Constraint<?, ? super T>... rest) {
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
    public <T> Validatable<T> valueOf(final T value) {
        final Constrainable<T> constrainable = new ConstrainableValue<>(traverseListener.asPath(), value);
        return validatable(constrainable);
    }

}
