package pl.ais.commons.bean.validation.constrainable;

import pl.ais.commons.bean.validation.Constraint;

import javax.annotation.Nonnull;
import java.util.function.Function;

/**
 * Defines the API contract for the Constrainable.
 *
 * <p>Constrainable is a single value, or collection of values, which can be constrained.
 * Specifically, we can {@link #apply} a Constraint to it.
 *
 * <p>Constrainable can be also visited by {@link ConstrainableVisitor}.
 *
 * @author Warlock, AIS.PL
 * @since 1.2.1
 */
public interface Constrainable<T> extends Function<Constraint<?, ? super T>, Boolean> {

    /**
     * Accepts given visitor.
     *
     * @param <R>     type of values returned to the caller (usually provided by the visitor)
     * @param visitor the visitor to be accepted
     * @return the value provided by the visitor
     */
    <R> R accept(@Nonnull ConstrainableVisitor<R> visitor);

    /**
     * Applies given constraint to this constrainable.
     *
     * @param constraint the constraint to be applied
     * @return {@code true} if given constraint is satisfied by this constrainable, {@code false} otherwise
     */
    @Override
    Boolean apply(Constraint<?, ? super T> constraint);

}
