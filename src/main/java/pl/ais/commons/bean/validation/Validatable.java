package pl.ais.commons.bean.validation;

import pl.ais.commons.bean.validation.constrainable.Constrainable;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * Defines the API contract for validatable value.
 *
 * @param <T> the type of the validatable values
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
public interface Validatable<T> extends Supplier<Constrainable<T>> {

    /**
     * Verifies if this validatable satisfies given constraints.
     *
     * @param first constraint which should be satisfied
     * @param rest  remaining constraints which should be satisfied
     * @return {@code true} if all constraints are satisfied by this validatable, {@code false} otherwise
     */
    boolean satisfies(@Nonnull Constraint<?, ? super T> first, Constraint<?, ? super T>... rest);

}
