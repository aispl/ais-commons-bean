package pl.ais.commons.bean.validation.constraint.composite;

import javax.annotation.concurrent.Immutable;

import pl.ais.commons.bean.validation.Constrainable;
import pl.ais.commons.bean.validation.Constraint;
import pl.ais.commons.bean.validation.event.ConstraintViolationEvent;
import pl.ais.commons.domain.specification.Specification;
import pl.ais.commons.domain.specification.composite.OrSpecification;

/**
 * Composite constraint being disjunction of other constraints.
 *
 * @param <V>
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
@Immutable
public class AnyConstraint<V> extends Constraint<V> {

    private final Constraint<V>[] constraints;

    private final boolean thorough;

    /**
     * Constructs new instance.
     *
     * @param thorough determines if all constraints should be checked (when {@code true}), or we should abort checking
     *        at first satisfied constraint (when {@code false})
     * @param constraints the constraints for which we create disjunction
     */
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public AnyConstraint(final boolean thorough, final Constraint<V>... constraints) {
        super("disjunction", new OrSpecification<>((Specification<V>[]) constraints));
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
                if (determinant.isSatisfiedBy(candidate.getValue())) {
                    result = true;
                    if (!thorough) {
                        break processing;
                    }
                } else {
                    candidate.notifyAboutViolation(new ConstraintViolationEvent(constraint, candidate));
                }
            }
        }
        return result;
    }

}
