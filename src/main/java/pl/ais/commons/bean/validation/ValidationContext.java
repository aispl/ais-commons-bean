package pl.ais.commons.bean.validation;

import pl.ais.commons.bean.facade.Facade;
import pl.ais.commons.bean.facade.TraverseListener;
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
     * @see AutoCloseable#close()
     */
    @Override
    public void close() {
        // Do nothing ...
    }

    @Override
    public void constraintViolated(@Nonnull final ConstraintViolated event) {
        Arrays.stream(listeners).forEachOrdered(listeners -> listeners.constraintViolated(event));
    }

    /**
     * Provides global validation2 listeners which will be used if there are no listeners defined
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
     * Decorates given value to allow its validation against some constraint.
     *
     * @param value the value which will be constrained
     * @return decorated value
     */
    public <T> Validatable<T> validateThat(final T value) {
        final Constrainable constrainable = new Constrainable(traverseListener.asPath(), value);
        return (first, rest) -> first.apply(constrainable, this) && Arrays.stream(rest)
                                                                          .map(constraint -> constraint.apply(constrainable, this))
                                                                          .allMatch(Specifications.isEqual(true));
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

    public <T> T subject() {
        return (T) target;
    }

}
