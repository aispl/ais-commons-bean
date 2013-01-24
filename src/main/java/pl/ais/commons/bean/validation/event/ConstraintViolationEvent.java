package pl.ais.commons.bean.validation.event;

import java.util.EventObject;

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
        return (Constraint) super.getSource();
    }

}
