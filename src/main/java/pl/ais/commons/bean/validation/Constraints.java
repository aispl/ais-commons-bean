package pl.ais.commons.bean.validation;

import pl.ais.commons.bean.validation.constraints.FitIntoConstraint;
import pl.ais.commons.bean.validation.constraints.NotBlankConstraint;
import pl.ais.commons.bean.validation.constraints.RequiredConstraint;
import pl.ais.commons.domain.specification.Specification;
import pl.ais.commons.domain.specification.composite.AndSpecification;
import pl.ais.commons.domain.specification.composite.OrSpecification;

/**
 * Provides set of useful {@link Constraint} implementations.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
public final class Constraints {

    /**
     * @param constraints the constraints
     * @return specification of constrainable matching all given constraints
     */
    @SafeVarargs
    public static <V> Specification<Constrainable<? extends V>> allConstraints(
        final Specification<Constrainable<? extends V>>... constraints) {
        return new AndSpecification<>(constraints);
    }

    /**
     * @param constraints the constraints
     * @return specification of constrainable matching any of given constraints
     */
    @SafeVarargs
    public static <V> Specification<Constrainable<? extends V>> anyConstraint(
        final Specification<Constrainable<? extends V>>... constraints) {
        return new OrSpecification<>(constraints);
    }

    /**
     * @param upperLimit the upper limit for character sequence length (inclusive)
     * @return constraint verifying if character sequence is limited to predefined number of characters
     */
    public static Constraint<CharSequence> fitInto(final int upperLimit) {
        return new FitIntoConstraint(upperLimit);
    }

    /**
     * @return constraint verifying if character sequence is holding at least one non-whitespace character.
     */
    public static Constraint<CharSequence> notBlank() {
        return NotBlankConstraint.INSTANCE;
    }

    /**
     * @return constraint verifying if required value has been defined (is not {@code null})
     */
    @SuppressWarnings("unchecked")
    public static <V> Constraint<V> required() {
        return (Constraint<V>) RequiredConstraint.INSTANCE;
    }

    /**
     * Constructs new instance.
     */
    private Constraints() {
        super();
    }

}
