package pl.ais.commons.bean.validation.constrainable;

import pl.ais.commons.bean.validation.Constraint;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Objects;

/**
 * Constrainable value.
 *
 * <p>Constrainable value has unique ID within the validation context. It can be for example a path to the property
 * of bean being validated.
 *
 * @param <T> the type of the value being constrained
 * @author Warlock, AIS.PL
 * @since 1.2.1
 */
@Immutable
@SuppressWarnings("PMD.ShortVariable")
public final class ConstrainableValue<T> implements Constrainable<T> {

    private final String id;

    private final T value;

    /**
     * Constructs new instance.
     *
     * @param id    ID of the value in the current validation context
     * @param value value to be constrained
     */
    public ConstrainableValue(final String id, final T value) {
        this.id = id;
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R> R accept(@Nonnull final ConstrainableVisitor visitor) {
        return (R) visitor.visit(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean apply(final Constraint<?, ? super T> constraint) {
        return constraint.test(value);
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     */
    @Override
    public boolean equals(final Object object) {
        boolean result = (this == object);
        if (!result && (object instanceof ConstrainableValue)) {
            final ConstrainableValue other = (ConstrainableValue) object;
            result = Objects.equals(id, other.id) && Objects.equals(value, other.value);
        }
        return result;
    }

    /**
     * @return path identifying the value in current validation context
     */
    public String getId() {
        return id;
    }

    /**
     * @return value itself
     */
    public T getValue() {
        return value;
    }

    /**
     * @return a hash code value for this constrainable value
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, value);
    }

    /**
     * @return a String representation of this constrainable value
     */
    @Override
    public String toString() {
        return new StringBuilder().append("Constrainable value of '")
                                  .append(value)
                                  .append("' (")
                                  .append(id)
                                  .append(')')
                                  .toString();
    }

}
