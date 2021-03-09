package im.aop.loggers.advice.after.throwing;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.slf4j.event.Level;

/**
 * Log after leaving the target method by throwing an exception.
 *
 * @author Andy Lian
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface LogAfterThrowing {

  Class<?> declaringClass() default void.class;

  Level level() default Level.ERROR;

  Class<? extends Throwable>[] ignoreExceptions() default {};

  String exitedAbnormallyMessage() default "";
}
