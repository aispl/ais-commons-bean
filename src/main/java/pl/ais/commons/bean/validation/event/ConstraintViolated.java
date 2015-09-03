package pl.ais.commons.bean.validation.event;

import pl.ais.commons.bean.validation.Constrainable;
import pl.ais.commons.bean.validation.Constraint;

import java.util.EventObject;
import java.util.Objects;

/**
 * Event delivered on each constraint violation.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
public final class ConstraintViolated extends EventObject {

    private final Constrainable<?> offender;

    /**
     * Constructs new instance.
     *
     * @param constraint violated constraint
     * @param offender   the constrainable which violated the constraint
     */
    public ConstraintViolated(final Constraint<?, ?> constraint, final Constrainable<?> offender) {
        super(constraint);
        this.offender = offender;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     */
    @Override
    public boolean equals(final Object object) {
        boolean result = (this == object);
        if (!result && (object instanceof ConstraintViolated)) {
            final ConstraintViolated other = (ConstraintViolated) object;
            result = Objects.equals(source, other.source) && Objects.equals(offender, other.offender);
        }
        return result;
    }

    /**
     * @return the constrainable which violated the constraint
     */
    @SuppressWarnings("unchecked")
    public <V> Constrainable<V> getOffender() {
        return (Constrainable<V>) offender;
    }

    /**
     * @return violated constraint
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Constraint<?, ?> getSource() {
        return (Constraint) source;
    }

    /**
     * @return a hash code value for this constrainable value
     */
    @Override
    public int hashCode() {
        return Objects.hash(source, offender);
    }

    /**
     * @return a String representation of this event
     */
    @Override
    public String toString() {
        return new StringBuilder().append(source)
                                  .append(" violated by ")
                                  .append(offender)
                                  .toString();
    }

}
