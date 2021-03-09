package im.aop.loggers.advice.before;

import java.util.Objects;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.event.Level;

import im.aop.loggers.AopLoggersProperties;
import im.aop.loggers.logging.LoggerService;
import im.aop.loggers.logging.message.JoinPointStringSupplierRegistrar;
import im.aop.loggers.logging.message.StringSubstitutor;
import im.aop.loggers.logging.message.StringSupplierLookup;

public class LogBeforeService {

  private static final LoggerService LOGGER_SERVICE = new LoggerService();

  private static final StringSubstitutor STRING_SUBSTITUTOR = new StringSubstitutor();

  private static final JoinPointStringSupplierRegistrar JOIN_POINT_STRING_SUPPLIER_REGISTRAR =
      new JoinPointStringSupplierRegistrar();

  private final AopLoggersProperties aopLoggersProperties;

  public LogBeforeService(final AopLoggersProperties aopLoggersProperties) {
    this.aopLoggersProperties = Objects.requireNonNull(aopLoggersProperties);
  }

  public void log(final JoinPoint joinPoint, final LogBefore annotation) {
    final Logger logger = LOGGER_SERVICE.getLogger(annotation.declaringClass(), joinPoint);
    if (isDisabled() || isLoggerLevelDisabled(logger, annotation.level())) {
      return;
    }

    final StringSupplierLookup stringLookup = new StringSupplierLookup();

    logEnteringMessage(joinPoint, annotation, logger, stringLookup);
  }

  private boolean isDisabled() {
    return aopLoggersProperties.isEnabled() == false;
  }

  private boolean isLoggerLevelDisabled(final Logger logger, final Level level) {
    return LOGGER_SERVICE.isEnabled(logger, level) == false;
  }

  private void logEnteringMessage(
      final JoinPoint joinPoint,
      final LogBefore annotation,
      final Logger logger,
      final StringSupplierLookup stringLookup) {
    JOIN_POINT_STRING_SUPPLIER_REGISTRAR.register(stringLookup, joinPoint);

    final String message =
        STRING_SUBSTITUTOR.substitute(
            getMessageTemplate(
                annotation.enteringMessage(), aopLoggersProperties.getEnteringMessage()),
            stringLookup);

    LOGGER_SERVICE.log(logger, annotation.level(), message);
  }

  private String getMessageTemplate(
      final String messageTemplate, final String defaultMessageTemplate) {
    return messageTemplate.length() > 0 ? messageTemplate : defaultMessageTemplate;
  }
}
