package im.aop.loggers.advice.after.throwing;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import im.aop.loggers.logging.Level;

/**
 * Log after leaving the target method by throwing an exception.
 *
 * @author Andy Lian
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface LogAfterThrowing {

  /**
   * Class name used as Logger's category name
   *
   * @return
   */
  Class<?> declaringClass() default void.class;

  /**
   * Log Level for exited abnormally message
   *
   * @return
   */
  Level level() default Level.DEFAULT;

  /**
   * Exited abnormally message template
   *
   * @return
   */
  String exitedAbnormallyMessage() default "";

  /**
   * Exceptions that will be ignored by Logger
   *
   * @return
   */
  Class<? extends Throwable>[] ignoreExceptions() default {};

  /**
   * Whether to print exception and its backtrace
   *
   * @return
   */
  boolean printStackTrace() default true;
}
