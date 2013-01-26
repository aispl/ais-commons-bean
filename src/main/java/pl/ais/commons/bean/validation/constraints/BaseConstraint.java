package pl.ais.commons.bean.validation.constraints;

import java.util.Arrays;

import javax.annotation.Nonnull;

import pl.ais.commons.bean.validation.Constrainable;
import pl.ais.commons.bean.validation.Constraint;
import pl.ais.commons.bean.validation.event.ConstraintViolationEvent;
import pl.ais.commons.domain.specification.Specification;

/**
 * Base class for {@link Constraint} implementations.
 *
 * @param <V> determines the type of constrainable value
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
public class BaseConstraint<V> implements Constraint<V> {

    private transient boolean active = true;

    private transient String message;

    private transient Object[] messageParameters;

    private transient String name;

    @SuppressWarnings("rawtypes")
    private transient Specification specification;

    /**
     * Constructs new instance.
     */
    private BaseConstraint() {
        super();
    }

    /**
     * Constructs new instance.
     *
     * @param name the constraint name
     * @param specification the specification which should be satisfied
     */
    public BaseConstraint(final String name, final Specification<?> specification) {
        this();
        this.name = name;
        this.specification = specification;
    }

    /**
     * Constructs new instance.
     *
     * @param name the constraint name
     * @param specification the specification which should be satisfied
     */
    protected BaseConstraint(final String name, final Specification<?> specification, final boolean active,
        final String message, final Object... messageParameters) {
        this();
        this.active = active;
        this.message = message;
        this.messageParameters = messageParameters;
        this.name = name;
        this.specification = specification;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] getMessageParameters() {
        return (null == messageParameters) ? null : Arrays.copyOf(messageParameters, messageParameters.length);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSatisfiedBy(@Nonnull final Constrainable<? extends V> candidate) {
        boolean result = true;
        if (active && !specification.isSatisfiedBy(candidate.getValue())) {
            result = false;
            candidate.notifyAboutViolation(new ConstraintViolationEvent(this, candidate));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("hiding")
    @Override
    public Constraint<V> when(final boolean active) {
        return new BaseConstraint<>(this.name, this.specification, active, this.message, this.messageParameters);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("hiding")
    @Override
    public Constraint<V> withDescription(final String message, final Object... messageParameters) {
        return new BaseConstraint<>(this.name, this.specification, this.active, message, messageParameters);
    }

}
