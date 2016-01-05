package pl.ais.commons.bean.validation.constraint;

import pl.ais.commons.bean.validation.Constraint;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Base class to be extended by constraints.
 *
 * @author Warlock, AIS.PL
 * @since 1.2.1
 */
abstract class AbstractConstraint<C extends Constraint<C, T>, T> implements Constraint<C, T> {

    protected static final Object[] ZERO_LENGTH_ARRAY = new Object[0];

    protected final boolean active;

    protected final String message;

    protected final Object[] messageParameters;

    protected final String name;

    protected AbstractConstraint(@Nonnull final String name, final boolean active,
                                 @Nonnull final Object[] messageParameters, @Nullable final String message) {
        super();
        this.name = name;
        this.message = message;
        this.messageParameters = Arrays.copyOf(messageParameters, messageParameters.length);
        this.active = active;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Constraint<?, T> and(@Nonnull final Constraint<?, T> other) {
        Objects.requireNonNull(other, "Constraint is required.");
        return new AllOfConstraint<>(false, this, other);
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
        return Arrays.copyOf(messageParameters, messageParameters.length);
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
    public boolean isActive() {
        return active;
    }

    /**
     * {@inheritDoc}
     */
    public Constraint<?, T> or(@Nonnull final Constraint<?, T> other) {
        return new AnyOfConstraint<>(false, this, other);
    }

}
