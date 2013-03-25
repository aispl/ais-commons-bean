package pl.ais.commons.bean.facade.event;

import java.util.EventListener;

/**
 * Defines the API contract for the method invocation listener.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
public interface MethodInvocationListener extends EventListener {

    /**
     * This method is called on each facade method invocation.
     *
     * @param event describes facade method invocation
     */
    void methodInvocation(MethodInvocationEvent event);

}
