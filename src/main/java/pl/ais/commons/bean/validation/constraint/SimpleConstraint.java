package pl.ais.commons.bean.validation.constraint;

import pl.ais.commons.bean.validation.Constraint;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Simple Constraint.
 *
 * @param <T> the type of the values handled by the constraint
 * @author Warlock, AIS.PL
 * @since 1.2.1
 */
@Immutable
public final class SimpleConstraint<T> extends AbstractConstraint<T> {

    private final Predicate<T> determinant;

    private SimpleConstraint(@Nonnull final String name, @Nonnull final Predicate<T> determinant, final boolean active,
                             @Nonnull final Object[] messageParameters, @Nullable final String message) {
        super(name, active, messageParameters, message);

        // Verify constructor requirements, ...
        Objects.requireNonNull(name, "SimpleConstraint name is required.");
        Objects.requireNonNull(determinant, "SimpleConstraint determinant is required.");
        Objects.requireNonNull(messageParameters, "Invalid message parameters provided");

        // ... and initialize this instance fields.
        this.determinant = determinant;
    }

    /**
     * Constructs new instance.
     *
     * @param name        name of the constraint
     * @param determinant predicate being determinant of the constraint
     */
    public SimpleConstraint(@Nonnull final String name, @Nonnull final Predicate<T> determinant) {
        this(name, determinant, true, ZERO_LENGTH_ARRAY, null);
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     */
    @Override
    public boolean equals(final Object object) {
        boolean result = (this == object);
        if (!result && (object instanceof SimpleConstraint)) {
            final SimpleConstraint other = (SimpleConstraint) object;
            result = Objects.equals(name, other.name) && Objects.equals(determinant, other.determinant);
        }
        return result;
    }

    /**
     * @return a hash code value for this constraint
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, determinant);
    }

    @Override
    public Constraint<T> negate() {
        return new SimpleConstraint<>(getNegatedName(), determinant.negate());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean test(final T candidate) {
        return !active || determinant.test(candidate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new StringBuilder().append("Constraint '")
                                  .append(determinant)
                                  .append('\'')
                                  .toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("hiding")
    @Nonnull
    public SimpleConstraint<T> when(final boolean active) {
        return new SimpleConstraint<>(name, determinant, active, messageParameters, message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("hiding")
    @Nonnull
    public SimpleConstraint<T> withDescription(final String message, final Object... messageParameters) {
        return new SimpleConstraint<>(name, determinant, active, messageParameters, message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("hiding")
    @Nonnull
    public SimpleConstraint<T> withMessageParameters(final Object... messageParameters) {
        return new SimpleConstraint<>(name, determinant, active, messageParameters, message);
    }

}
