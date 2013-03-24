package pl.ais.commons.bean.validation.constraint.composite;

import javax.annotation.concurrent.Immutable;

import pl.ais.commons.bean.validation.Constrainable;
import pl.ais.commons.bean.validation.Constraint;
import pl.ais.commons.bean.validation.event.ConstraintViolationEvent;
import pl.ais.commons.domain.specification.Specification;
import pl.ais.commons.domain.specification.composite.AndSpecification;

/**
 * Composite constraint being conjunction of other constraints.
 *
 * @param <V>
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
@Immutable
public class AllConstraint<V> extends Constraint<V> {

    private final Constraint<V>[] constraints;

    private final boolean thorough;

    /**
     * Constructs new instance.
     *
     * @param thorough determines if all constraints should be checked (when {@code true}), or we should abort checking
     *        at first not satisfied constraint (when {@code false})
     * @param constraints the constraints for which we create conjunction
     */
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public AllConstraint(final boolean thorough, final Constraint<V>... constraints) {
        super("conjunction", new AndSpecification<>((Specification<V>[]) constraints));
        this.constraints = constraints;
        this.thorough = thorough;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Constrainable<V>> boolean isSatisfiedBy(final T candidate) {
        boolean result = false;
        processing: {
            // Walk through the constraints, ...
            for (Constraint<V> constraint : constraints) {

                // ... skip all inactive, ...
                if (!constraint.isActive()) {
                    continue;
                }

                // ... verify if constraint is satisfied using its determinant, ...
                final Specification<V> determinant = constraint.getDeterminant();
                if (!determinant.isSatisfiedBy(candidate.getValue())) {
                    candidate.notifyAboutViolation(new ConstraintViolationEvent(constraint, candidate));
                    if (!thorough) {
                        break processing;
                    }
                }
            }
            result = true;
        }
        return result;
    }

}
