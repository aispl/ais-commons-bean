package pl.ais.commons.bean.facade;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Provides facade creating advises.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface FacadeAdvice {

    /**
     * @return default implementation type (for interface based facade)
     */
    Class<?> targetClass() default void.class;

}
