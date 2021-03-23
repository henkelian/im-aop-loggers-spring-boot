package im.aop.loggers.advice.around;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

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

  /**
   * Class name used as Logger's category name
   *
   * @return
   */
  Class<?> declaringClass() default void.class;

  /**
   * Log Level for entering, exited normally and elapsed message
   *
   * @return
   */
  Level level() default Level.DEFAULT;

  /**
   * Entering message template
   *
   * @return
   */
  String enteringMessage() default "";

  /**
   * Exited message template
   *
   * @return
   */
  String exitedMessage() default "";

  /**
   * Log level for exited abnormally message
   *
   * @return
   */
  Level exitedAbnormallyLevel() default Level.DEFAULT;

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
   * Elapsed message template
   *
   * @return
   */
  String elapsedMessage() default "";

  /**
   * Log level for elapsed warning message
   *
   * @return
   */
  Level elapsedWarningLevel() default Level.DEFAULT;

  /**
   * Elapsed warning message template
   *
   * @return
   */
  String elapsedWarningMessage() default "";

  /**
   * Elapsed time limit to log elapsed warning message
   *
   * @return
   */
  long elapsedTimeLimit() default 0;

  /**
   * Elapsed time unit
   *
   * @return
   */
  ChronoUnit elapsedTimeUnit() default ChronoUnit.MILLIS;
}
