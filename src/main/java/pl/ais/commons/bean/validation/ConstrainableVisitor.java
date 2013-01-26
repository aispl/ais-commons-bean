package pl.ais.commons.bean.validation;

import pl.ais.commons.application.pattern.Visitor;

/**
 * Defines the API contract for {@link Constrainable} visitor.
 *
 * @param <R> determines the type of value returned by visitor methods
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
public interface ConstrainableVisitor<R> extends Visitor<ConstrainableVisitor<?>> {

    /**
     * Visits the {@link Constrainable} property.
     *
     * @param constrainable the constrainable property to visit
     * @return value specific for concrete visitor implementation
     */
    R visit(ConstrainableProperty<?> constrainable);

    /**
     * Visits the {@link Constrainable} value.
     *
     * @param constrainable the constrainable value to visit
     * @return value specific for concrete visitor implementation
     */
    R visit(ConstrainableValue<?> constrainable);

}
