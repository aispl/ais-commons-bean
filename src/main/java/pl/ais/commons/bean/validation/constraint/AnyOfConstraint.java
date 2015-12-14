package pl.ais.commons.bean.validation.constraint;

import pl.ais.commons.bean.validation.Constraint;
import pl.ais.commons.bean.validation.constrainable.Constrainable;
import pl.ais.commons.bean.validation.event.ConstraintViolated;
import pl.ais.commons.bean.validation.event.ValidationListener;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Composite constraint being disjunction of other constraints.
 *
 * @param <T>
 * @author Warlock, AIS.PL
 * @since 1.2.1
 */
@Immutable
public final class AnyOfConstraint<T> extends AbstractConstraint<AnyOfConstraint<T>, T> {

    private final Constraint<?, T>[] constraints;

    private final boolean thorough;

    private AnyOfConstraint(@Nonnull final String name, final Constraint<?, T>[] constraints, final boolean active,
                            final boolean thorough, @Nonnull final Object[] messageParameters, @Nullable final String message) {
        super(name, active, messageParameters, message);

        this.constraints = Arrays.copyOf(constraints, constraints.length);
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
        super("disjunction", true, new Object[0], null);

        // Verify constructor requirements, ...
        Objects.requireNonNull(first, "First constraint is required");

        // ... and initialize this instance fields.
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
    public Boolean apply(final Constrainable<? extends T> constrainable, final ValidationListener listener) {
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
                final boolean satisfied = constrainable.apply(constraint);
                result |= satisfied;
                if (satisfied && !thorough) {
                    break processing;
                }
            }

            if (!result) {
                listener.constraintViolated(new ConstraintViolated(this, constrainable));
            }
        }
        return result;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     */
    @Override
    public boolean equals(final Object object) {
        boolean result = (this == object);
        if (!result && (object instanceof AnyOfConstraint)) {
            final AnyOfConstraint other = (AnyOfConstraint) object;
            result = Objects.equals(name, other.name) && Objects.equals(thorough, other.thorough)
                && Arrays.equals(constraints, other.constraints);
        }
        return result;
    }

    /**
     * @return a hash code value for this constraint
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, thorough, Arrays.hashCode(constraints));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Constraint<?, T> negate() {
        return new SimpleConstraint<>(getNegatedName(), candidate -> !test(candidate));
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
    @Override
    public String toString() {
        return Arrays.stream(constraints)
                     .map(Constraint::toString)
                     .collect(Collectors.joining(" || ", "Composite Constraint: (", ")"));
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
