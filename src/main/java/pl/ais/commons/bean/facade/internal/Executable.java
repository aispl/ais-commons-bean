package pl.ais.commons.bean.facade.internal;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Defines the API contract for generic executable.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
interface Executable {

    /**
     * Executes this executable in given context.
     *
     * @param context execution context
     * @return execution result
     * @throws Throwable in case of troubles while executing the executable
     */
    @CheckForNull
    Object execute(@Nonnull ExecutionContext context) throws Throwable;

}
