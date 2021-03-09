package im.aop.loggers.advice.after.returning;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.slf4j.event.Level;

/**
 * Log after leaving the target method normally.
 *
 * @author Andy Lian
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface LogAfterReturning {

  Class<?> declaringClass() default void.class;

  Level level() default Level.INFO;

  String exitedMessage() default "";
}
