package pl.ais.commons.bean.validation.listener;

import org.springframework.validation.Errors;
import pl.ais.commons.bean.validation.Constraint;
import pl.ais.commons.bean.validation.constrainable.Constrainable;
import pl.ais.commons.bean.validation.constrainable.ConstrainableGroup;
import pl.ais.commons.bean.validation.constrainable.ConstrainableValue;
import pl.ais.commons.bean.validation.constrainable.ConstrainableVisitor;
import pl.ais.commons.bean.validation.event.ConstraintViolated;
import pl.ais.commons.bean.validation.event.ValidationListener;

import java.text.MessageFormat;

/**
 * {@link ValidationListener} implementation notifying Spring Framework about the constraint violations
 * using provided {@link Errors} instance.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
public final class SpringValidationListener implements ValidationListener {

    private final Errors errors;

    /**
     * Constructs new instance.
     *
     * @param errors binding/validation2 errors holder
     */
    public SpringValidationListener(final Errors errors) {
        this.errors = errors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void constraintViolated(final ConstraintViolated event) {
        final Constraint<?> constraint = event.getSource();
        final Constrainable<?> offender = event.getOffender();
        offender.accept(new ErrorReportingVisitor(constraint));
    }

    @SuppressWarnings("PMD.NullAssignment")
    private class ErrorReportingVisitor implements ConstrainableVisitor<Void> {

        private final Constraint<?> constraint;

        ErrorReportingVisitor(final Constraint<?> constraint) {
            this.constraint = constraint;
        }

        @Override
        public Void visit(final ConstrainableValue<?> constrainable) {
            final String message = constraint.getMessage();
            final Object[] messageParameters = constraint.getMessageParameters();
            errors.rejectValue(constrainable.getId(), constraint.getName(), messageParameters,
                (null == message) ? null : MessageFormat.format(message, messageParameters));
            return null;
        }

        @Override
        public Void visit(final ConstrainableGroup<?> constrainable) {
            final String message = constraint.getMessage();
            final Object[] messageParameters = constraint.getMessageParameters();
            errors.reject(constraint.getName(), messageParameters,
                (null == message) ? null : MessageFormat.format(message, messageParameters));
            return null;
        }

    }
}
