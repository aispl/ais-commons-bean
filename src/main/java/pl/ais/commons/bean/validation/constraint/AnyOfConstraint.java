package pl.ais.commons.bean.validation.constraint;

import pl.ais.commons.bean.validation.Constrainable;
import pl.ais.commons.bean.validation.Constraint;
import pl.ais.commons.bean.validation.event.ConstraintViolated;
import pl.ais.commons.bean.validation.event.ValidationListener;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;

/**
 * Composite constraint being disjunction of other constraints.
 *
 * @param <T>
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
@Immutable
public final class AnyOfConstraint<T> implements Constraint<AnyOfConstraint<T>,T> {

    private final boolean active;

    private final Constraint<?, T>[] constraints;

    private final String message;

    private final Object[] messageParameters;

    private final String name;

    private final boolean thorough;

    private AnyOfConstraint(@Nonnull final String name, final Constraint<?, T>[] constraints, final boolean active,
                            final boolean thorough, @Nonnull final Object[] messageParameters, @Nullable final String message) {
        this.active = active;
        this.constraints = Arrays.copyOf(constraints, constraints.length);
        this.message = message;
        this.messageParameters = Arrays.copyOf(messageParameters, messageParameters.length);
        this.name = name;
        this.thorough = thorough;
    }

    /**
     * Constructs new instance.
     *
     * @param thorough determines if all constraints should be checked (when {@code true}), or we should abort checking
     *                 at first satisfied constraint (when {@code false})
     * @param first    first constraint to be enclosed by this instance
     * @param rest     remaining constraints to be enclosed by this instance
     */
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public AnyOfConstraint(final boolean thorough, @Nonnull final Constraint<?, T> first, final Constraint<?, T>... rest) {
        super();

        // Verify constructor requirements, ...
        Objects.requireNonNull(first, "First constraint is required");

        // ... and initialize this instance fields.
        active = true;
        message = null;
        name = "disjunction";
        messageParameters = new Object[0];
        this.thorough = thorough;

        constraints = (Constraint<?, T>[]) Array.newInstance(Constraint.class, rest.length + 1);
        constraints[0] = first;
        System.arraycopy(rest, 0, constraints, 1, rest.length);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Boolean apply(final Constrainable<T> constrainable, final ValidationListener listener) {
        boolean result = false;
        processing:
        {
            // Walk through the constraints, ...
            for (final Constraint<?, T> constraint : constraints) {

                // ... skip all inactive, ...
                if (!constraint.isActive()) {
                    continue;
                }

                // ... verify if constraint is satisfied, break processing if needed.
                final boolean satisfied = constraint.test(constrainable.getValue());
                if (satisfied && !thorough) {
                    break processing;
                }
            }

            listener.constraintViolated(new ConstraintViolated(this, constrainable));
        }
        return result;
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
    @Override
    public boolean test(@Nullable final T candidate) {
        return Arrays.stream(constraints)
                     .filter(Constraint::isActive)
                     .anyMatch(constraint -> constraint.test(candidate));
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public AnyOfConstraint<T> when(final boolean active) {
        return new AnyOfConstraint<>(name, constraints, active, thorough, messageParameters, message);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public AnyOfConstraint<T> withDescription(@Nonnull final String message, final Object... messageParameters) {
        return new AnyOfConstraint<>(name, constraints, active, thorough, messageParameters, message);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public AnyOfConstraint<T> withMessageParameters(final Object... messageParameters) {
        return new AnyOfConstraint<>(name, constraints, active, thorough, messageParameters, message);
    }

}
