package im.aop.loggers.advice.around;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import im.aop.loggers.logging.Level;

/**
 * Log before entering and after leaving the target method, regardless of leaving normally or
 * throwing an exception.
 *
 * @author Andy Lian
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface LogAround {

  Class<?> declaringClass() default void.class;

  Level level() default Level.DEFAULT;

  String enteringMessage() default "";

  String exitedMessage() default "";

  Level exitedAbnormallyLevel() default Level.DEFAULT;

  String exitedAbnormallyMessage() default "";

  Class<? extends Throwable>[] ignoreExceptions() default {};

  String elapsedMessage() default "";
}
