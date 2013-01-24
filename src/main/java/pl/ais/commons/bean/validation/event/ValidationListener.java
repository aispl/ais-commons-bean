package pl.ais.commons.bean.validation.event;

import java.util.EventListener;

import javax.annotation.Nonnull;

/**
 * Defines the API contract for validation listener.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
public interface ValidationListener extends EventListener {

    /**
     * This method is called on each constraint violation.
     *
     * @param event describes the constraint violation
     */
    void constraintViolated(@Nonnull ConstraintViolationEvent event);

}
