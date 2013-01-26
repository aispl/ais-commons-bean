package pl.ais.commons.bean.validation;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import pl.ais.commons.application.pattern.VisitorAcceptor;
import pl.ais.commons.bean.validation.event.ConstraintViolationEvent;
import pl.ais.commons.bean.validation.event.ValidationListener;

/**
 * Defines the API contract for constrainable.
 *
 * @param <V> determines the type of constrainable value
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
public interface Constrainable<V> extends VisitorAcceptor<ConstrainableVisitor<?>> {

    /**
     * @return the constrainable value
     */
    @CheckForNull
    V getValue();

    /**
     * Notifies the observers about the constraint violation.
     *
     * @param event describes the constraint violation
     */
    void notifyAboutViolation(@Nonnull ConstraintViolationEvent event);

    /**
     * Let the constrainable know that given observers are interested in the constraint violations.
     *
     * @param observers observers interested in the constraint violations
     * @return this instance (for method invocation chaining)
     */
    @Nonnull
    Constrainable<V> observedBy(@Nonnull ValidationListener... observers);

}