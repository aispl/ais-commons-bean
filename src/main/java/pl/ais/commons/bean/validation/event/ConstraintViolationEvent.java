package pl.ais.commons.bean.validation.event;

import java.util.EventObject;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.ais.commons.bean.validation.Constrainable;
import pl.ais.commons.bean.validation.Constraint;

/**
 * Event delivered on each validation constraint violation.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class ConstraintViolationEvent extends EventObject {

    private final Constrainable<?> offender;

    /**
     * Constructs new instance.
     *
     * @param constraint violated constraint
     * @param offender the constrainable which violated the constraint
     */
    public ConstraintViolationEvent(final Constraint<?> constraint, final Constrainable<?> offender) {
        super(constraint);
        this.offender = offender;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object object) {
        boolean result = (this == object);
        if (!result && (null != object) && (getClass() == object.getClass())) {
            final ConstraintViolationEvent other = (ConstraintViolationEvent) object;
            result = new EqualsBuilder().append(source, other.source).append(offender, other.offender).isEquals();
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
    public Constraint<?> getSource() {
        return (Constraint) source;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(source).append(offender).toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("constraint", source).append("offender", offender).build();
    }

}
