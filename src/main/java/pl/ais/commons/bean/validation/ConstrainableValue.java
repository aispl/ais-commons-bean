package pl.ais.commons.bean.validation;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Constrainable value.
 *
 * @param <V> determines the type of constrainable value
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
@Immutable
public final class ConstrainableValue<V> extends AbstractConstrainable<V> {

    /**
     * Constructs new instance.
     *
     * @param value the constrainable value
     */
    public ConstrainableValue(final V value) {
        super(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R> R accept(@Nonnull final ConstrainableVisitor<?> visitor) {
        return (R) visitor.visit(this);
    }

}