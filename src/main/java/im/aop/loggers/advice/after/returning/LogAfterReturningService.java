package im.aop.loggers.advice.after.returning;

import java.util.Objects;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.event.Level;

import im.aop.loggers.AopLoggersProperties;
import im.aop.loggers.logging.LoggerService;
import im.aop.loggers.logging.message.JoinPointStringSupplierRegistrar;
import im.aop.loggers.logging.message.ReturnValueStringSupplierRegistrar;
import im.aop.loggers.logging.message.StringSubstitutor;
import im.aop.loggers.logging.message.StringSupplierLookup;

public class LogAfterReturningService {

  private static final StringSubstitutor STRING_SUBSTITUTOR = new StringSubstitutor();

  private static final JoinPointStringSupplierRegistrar JOIN_POINT_STRING_SUPPLIER_REGISTRAR =
      new JoinPointStringSupplierRegistrar();

  private static final ReturnValueStringSupplierRegistrar RETURN_VALUE_STRING_SUPPLIER_REGISTRAR =
      new ReturnValueStringSupplierRegistrar();

  private static final LoggerService LOGGER_SERVICE = new LoggerService();

  private final AopLoggersProperties aopLoggersProperties;

  public LogAfterReturningService(final AopLoggersProperties aopLoggersProperties) {
    this.aopLoggersProperties = Objects.requireNonNull(aopLoggersProperties);
  }

  public void log(
      final JoinPoint joinPoint, final LogAfterReturning annotation, final Object returnValue) {
    final Logger logger = LOGGER_SERVICE.getLogger(annotation.declaringClass(), joinPoint);
    if (isDisabled() || isLoggerLevelDisabled(logger, annotation.level())) {
      return;
    }

    final StringSupplierLookup stringLookup = new StringSupplierLookup();

    logExitedMessage(joinPoint, annotation, logger, stringLookup, returnValue);
  }

  private boolean isDisabled() {
    return aopLoggersProperties.isEnabled() == false;
  }

  private boolean isLoggerLevelDisabled(final Logger logger, final Level level) {
    return LOGGER_SERVICE.isEnabled(logger, level) == false;
  }

  private void logExitedMessage(
      final JoinPoint joinPoint,
      final LogAfterReturning annotation,
      final Logger logger,
      final StringSupplierLookup stringLookup,
      final Object returnValue) {
    JOIN_POINT_STRING_SUPPLIER_REGISTRAR.register(stringLookup, joinPoint);
    RETURN_VALUE_STRING_SUPPLIER_REGISTRAR.register(stringLookup, joinPoint, returnValue);

    final String message =
        STRING_SUBSTITUTOR.substitute(
            getMessageTemplate(annotation.exitedMessage(), aopLoggersProperties.getExitedMessage()),
            stringLookup);
    LOGGER_SERVICE.log(logger, annotation.level(), message);
  }

  private String getMessageTemplate(
      final String messageTemplate, final String defaultMessageTemplate) {
    return messageTemplate.length() > 0 ? messageTemplate : defaultMessageTemplate;
  }
}
