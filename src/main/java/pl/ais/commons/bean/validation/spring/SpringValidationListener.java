package pl.ais.commons.bean.validation.spring;

import java.text.MessageFormat;

import org.springframework.validation.Errors;

import pl.ais.commons.bean.validation.Constrainable;
import pl.ais.commons.bean.validation.ConstrainableProperty;
import pl.ais.commons.bean.validation.ConstrainableValue;
import pl.ais.commons.bean.validation.ConstrainableVisitor;
import pl.ais.commons.bean.validation.Constraint;
import pl.ais.commons.bean.validation.event.ConstraintViolationEvent;
import pl.ais.commons.bean.validation.event.ValidationListener;

/**
 * {@link ValidationListener} implementation notifying Spring Framework about the constraint violations
 * using provided {@link Errors} instance.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
public class SpringValidationListener implements ValidationListener {

    protected transient final Errors errors;

    /**
     * Constructs new instance.
     *
     * @param errors binding/validation errors holder
     */
    public SpringValidationListener(final Errors errors) {
        this.errors = errors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("PMD.NullAssignment")
    public void constraintViolated(final ConstraintViolationEvent event) {
        final Constraint<?> constraint = event.getSource();
        final Constrainable<?> offender = event.getOffender();
        offender.accept(new ConstrainableVisitor<Void>() {

            /**
             * {@inheritDoc}
             */
            @Override
            public Void visit(final ConstrainableProperty<?> constrainable) {
                final String message = constraint.getMessage();
                final Object[] messageParameters = constraint.getMessageParameters();
                errors.rejectValue(constrainable.getPathRepresentation(), constraint.getName(), messageParameters,
                    (null == message) ? null : MessageFormat.format(message, messageParameters));
                return null;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public Void visit(final ConstrainableValue<?> constrainable) {
                final String message = constraint.getMessage();
                final Object[] messageParameters = constraint.getMessageParameters();
                errors.reject(constraint.getName(), messageParameters,
                    (null == message) ? null : MessageFormat.format(message, messageParameters));
                return null;
            }

        });
    }

}
