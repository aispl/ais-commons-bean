package pl.ais.commons.bean.validation;

import java.util.Arrays;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.ais.commons.bean.validation.event.ConstraintViolationEvent;
import pl.ais.commons.domain.specification.Specification;

/**
 * Base class for all constraints.
 *
 * @param <V> determines the type of constrainable value
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
public class Constraint<V> implements Specification<Constrainable<V>> {

    private transient boolean active = true;

    private transient Specification<V> determinant;

    private transient String message;

    private transient Object[] messageParameters;

    private transient String name;

    /**
     * Constructs new instance.
     *
     * @param name the constraint name
     * @param determinant the specification which should be satisfied
     */
    protected Constraint(@Nonnull final String name, @Nonnull final Specification<V> determinant) {
        super();
        if ((null == name) || (null == determinant)) {
            throw new IllegalArgumentException("Both constraint name and determinant cannot be null.");
        }
        this.name = name;
        this.determinant = determinant;
    }

    /**
     * Constructs new instance.
     *
     * @param name the constraint name
     * @param determinant the specification which should be satisfied
     * @param active {@code true} if the constraint is active, {@code false} otherwise
     * @param message default message which should be used if constraint is violated
     * @param messageParameters the message parameters
     */
    private Constraint(@Nonnull final String name, @Nonnull final Specification<V> determinant, final boolean active,
        @Nullable final String message, final Object... messageParameters) {
        this(name, determinant);
        this.active = active;
        this.message = message;
        this.messageParameters = messageParameters;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(final Object object) {
        boolean result = (this == object);
        if (!result && (null != object) && (getClass() == object.getClass())) {
            final Constraint other = (Constraint) object;
            result = new EqualsBuilder().append(name, other.name).append(determinant, other.determinant).isEquals();
        }
        return result;
    }

    /**
     * Returns the constraint determinant.
     *
     * @return the constraint determinant
     */
    @Nonnull
    public Specification<V> getDeterminant() {
        return determinant;
    }

    /**
     * Returns the message bound to this constraint.
     *
     * @return the message bound to this constraint (can be {@code null})
     */
    @CheckForNull
    public String getMessage() {
        return message;
    }

    /**
     * Returns the parameters of message bound to this constraint.
     *
     * @return the parameters of message bound to this constraint
     */
    @CheckForNull
    public Object[] getMessageParameters() {
        return (null == messageParameters) ? null : Arrays.copyOf(messageParameters, messageParameters.length);
    }

    /**
     * Returns the name of this constraint.
     *
     * @return the name of this constraint
     */
    @Nonnull
    public String getName() {
        return name;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(determinant).toHashCode();
    }

    /**
     * Verifies if this constraint is active.
     *
     * @return {@code true} if this constraint is active, {@code false} otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Constrainable<V>> boolean isSatisfiedBy(final T candidate) {
        boolean result = true;
        if (active) {
            if (!determinant.isSatisfiedBy(candidate.getValue())) {
                result = false;
                candidate.notifyAboutViolation(new ConstraintViolationEvent(this, candidate));
            }
        }
        return result;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name).append("active", active)
            .append("determinant", determinant).build();
    }

    /**
     * Provides the constraint (de)activating method.
     *
     * @param active defines if this constraint is active
     * @return constraint instance (for method invocation chaining)
     */
    @SuppressWarnings("hiding")
    @Nonnull
    public Constraint<V> when(final boolean active) {
        return new Constraint<>(this.name, this.determinant, active, this.message, this.messageParameters);
    }

    /**
     * Provides the message (potentially parameterized) which should be used to describe the constraint.
     *
     * @param message the message (potentially parameterized)
     * @param messageParameters the message parameters (if any)
     * @return constraint instance (for method invocation chaining)
     */
    @SuppressWarnings("hiding")
    @Nonnull
    public Constraint<V> withDescription(final String message, final Object... messageParameters) {
        return new Constraint<>(this.name, this.determinant, this.active, message, messageParameters);
    }

    /**
     * Provides the message parameters which should be used to describe the constraint.
     *
     * @param messageParameters the message parameters (if any)
     * @return constraint instance (for method invocation chaining)
     */
    @SuppressWarnings("hiding")
    @Nonnull
    public Constraint<V> withMessageParameters(final Object... messageParameters) {
        return new Constraint<>(this.name, this.determinant, this.active, this.message, messageParameters);
    }

    /**
     * Provides the new name for the constraint.
     *
     * @param name new constraint name
     * @return constraint instance (for method invocation chaining)
     */
    @SuppressWarnings("hiding")
    @Nonnull
    public Constraint<V> withName(@Nonnull final String name) {
        return new Constraint<>(name, this.determinant, this.active, this.message, this.messageParameters);
    }

}
