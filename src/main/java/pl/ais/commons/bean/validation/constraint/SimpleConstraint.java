package pl.ais.commons.bean.validation.constraint;

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
public final class SimpleConstraint<T> extends AbstractConstraint<SimpleConstraint<T>, T> {

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
        this(name, determinant, true, new Object[0], null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean test(final T candidate) {
        return active ? determinant.test(candidate) : true;
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
    @SuppressWarnings("hiding")
    @Nonnull
    public SimpleConstraint<T> when(final boolean active) {
        return new SimpleConstraint<>(name, determinant, active, messageParameters, message);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("hiding")
    @Nonnull
    public SimpleConstraint<T> withDescription(final String message, final Object... messageParameters) {
        return new SimpleConstraint<>(name, determinant, active, messageParameters, message);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("hiding")
    @Nonnull
    public SimpleConstraint<T> withMessageParameters(final Object... messageParameters) {
        return new SimpleConstraint<>(name, determinant, active, messageParameters, message);
    }

}
