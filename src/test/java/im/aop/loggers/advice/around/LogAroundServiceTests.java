package im.aop.loggers.advice.around;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import im.aop.loggers.AopLoggersProperties;
import im.aop.loggers.logging.Level;

/**
 * Tests for {@link LogAroundService}.
 *
 * @author Andy Lian
 */
@ExtendWith(OutputCaptureExtension.class)
class LogAroundServiceTests {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withUserConfiguration(AopLoggersPropertiesTestConfiguration.class)
          .withBean(LogAroundService.class);

  @TestConfiguration(proxyBeanMethods = false)
  @EnableConfigurationProperties({AopLoggersProperties.class})
  static class AopLoggersPropertiesTestConfiguration {}

  static class Foo {

    void foo() {}
  }

  private MethodSignature methodSignature;

  private ProceedingJoinPoint joinPoint;

  @BeforeEach
  void beforeEach() throws NoSuchMethodException {
    methodSignature = mockMethodSignature(Foo.class, "foo");
    joinPoint = mockJoinPoint(methodSignature);
  }

  @Test
  void logEnteringMessage_defaultLevel(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".entering-level=DEBUG")
        .run(
            (context) -> {
              when(joinPoint.proceed()).thenReturn("foo");

              final LogAround annotation = mockLogAround(Level.DEFAULT, "foo", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.DEBUG);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.log(joinPoint, annotation);

              assertThat(capturedOutput).contains("DEBUG " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logEnteringMessage_customLevel(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".entering-level=DEBUG")
        .run(
            (context) -> {
              when(joinPoint.proceed()).thenReturn("foo");

              final LogAround annotation = mockLogAround(Level.INFO, "foo", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.DEBUG);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.log(joinPoint, annotation);

              assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logEnteringMessage_defaultMessage(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".entering-message=foo")
        .run(
            (context) -> {
              when(joinPoint.proceed()).thenReturn("foo");

              final LogAround annotation = mockLogAround(Level.INFO, "", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.log(joinPoint, annotation);

              assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logEnteringMessage_customMessage(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenReturn("foo");

          final LogAround annotation = mockLogAround(Level.INFO, "foo", null);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.log(joinPoint, annotation);

          assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void logEnteringMessage_disabled(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              when(joinPoint.proceed()).thenReturn("foo");

              final LogAround annotation = mockLogAround(Level.INFO, "foo", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.log(joinPoint, annotation);

              assertThat(capturedOutput).doesNotContain("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logEnteringMessage_loggerLevelDisabled(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenReturn("foo");

          final LogAround annotation = mockLogAround(Level.DEBUG, "foo", null);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.log(joinPoint, annotation);

          assertThat(capturedOutput).doesNotContain("DEBUG " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void logExitedMessage_defaultLevel(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".exited-level=DEBUG")
        .run(
            (context) -> {
              when(joinPoint.proceed()).thenReturn("foo");

              final LogAround annotation = mockLogAround(Level.DEFAULT, "foo", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.DEBUG);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.log(joinPoint, annotation);

              assertThat(capturedOutput).contains("DEBUG " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logExitedMessage_customLevel(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".exited-level=DEBUG")
        .run(
            (context) -> {
              when(joinPoint.proceed()).thenReturn("foo");

              final LogAround annotation = mockLogAround(Level.INFO, "foo", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.DEBUG);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.log(joinPoint, annotation);

              assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logExitedMessage_defaultMessage(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".exited-message=foo")
        .run(
            (context) -> {
              when(joinPoint.proceed()).thenReturn("foo");

              final LogAround annotation = mockLogAround(Level.INFO, "", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.log(joinPoint, annotation);

              assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logExitedMessage_customMessage(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenReturn("foo");

          final LogAround annotation = mockLogAround(Level.INFO, "foo", null);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.log(joinPoint, annotation);

          assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void logExitedMessage_disabled(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              when(joinPoint.proceed()).thenReturn("foo");

              final LogAround annotation = mockLogAround(Level.INFO, "foo", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.log(joinPoint, annotation);

              assertThat(capturedOutput).doesNotContain("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logExitedMessage_loggerLevelDisabled(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenReturn("foo");

          final LogAround annotation = mockLogAround(Level.DEBUG, "foo", null);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.log(joinPoint, annotation);

          assertThat(capturedOutput).doesNotContain("DEBUG " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void logExitedAbnormallyMessage_defaultLevel(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".exited-abnormally-level=DEBUG")
        .run(
            (context) -> {
              when(joinPoint.proceed()).thenThrow(new RuntimeException("foo"));

              final LogAround annotation = mockLogAround(Level.DEFAULT, "foo", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.DEBUG);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.log(joinPoint, annotation);

              assertThat(capturedOutput).contains("DEBUG " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logExitedAbnormallyMessage_customLevel(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".exited-abnormally-level=DEBUG")
        .run(
            (context) -> {
              when(joinPoint.proceed()).thenThrow(new RuntimeException());

              final LogAround annotation = mockLogAround(Level.INFO, "foo", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.log(joinPoint, annotation);

              assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logExitedAbnormallyMessage_defaultMessage(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".exited-abnormally-message=foo")
        .run(
            (context) -> {
              when(joinPoint.proceed()).thenThrow(new RuntimeException());

              final LogAround annotation = mockLogAround(Level.INFO, "", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.log(joinPoint, annotation);

              assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logExitedAbnormallyMessage_customMessage(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenThrow(new RuntimeException());

          final LogAround annotation = mockLogAround(Level.INFO, "foo", null);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.log(joinPoint, annotation);

          assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void logExitedAbnormallyMessage_disabled(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              when(joinPoint.proceed()).thenThrow(new RuntimeException("foo"));

              final LogAround annotation = mockLogAround(Level.ERROR, "foo", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.log(joinPoint, annotation);

              assertThat(capturedOutput).doesNotContain("ERROR " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logExitedAbnormallyMessage_loggerLevelDisabled(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenThrow(new RuntimeException());

          final LogAround annotation = mockLogAround(Level.DEBUG, "foo", null);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.log(joinPoint, annotation);

          assertThat(capturedOutput).doesNotContain("DEBUG " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void logExitedAbnormallyMessage_globallyIgnoredException(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(
            AopLoggersProperties.PREFIX + ".ignore-exceptions=java.lang.RuntimeException")
        .run(
            (context) -> {
              when(joinPoint.proceed()).thenThrow(new RuntimeException());

              final LogAround annotation =
                  mockLogAroundForExitAbnormally(Level.DEFAULT, "foo", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.log(joinPoint, annotation);

              assertThat(capturedOutput).doesNotContain("ERROR " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logExitedAbnormallyMessage_locallyIgnoredException(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenThrow(new RuntimeException());

          final LogAround annotation =
              mockLogAroundForExitAbnormally(
                  Level.DEFAULT, "foo", Arrays.array(RuntimeException.class));
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.log(joinPoint, annotation);

          assertThat(capturedOutput).doesNotContain("ERROR " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void logExitedAbnormallyMessage_nonIgnoredException(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenThrow(new RuntimeException());

          final LogAround annotation =
              mockLogAround(Level.ERROR, "foo", Arrays.array(ClassNotFoundException.class));
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.log(joinPoint, annotation);

          assertThat(capturedOutput).contains("ERROR " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void logExitedAbnormallyMessage_emptyIgnoredExceptions(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenThrow(new RuntimeException());

          final LogAround annotation = mockLogAround(Level.ERROR, "foo", Arrays.array());
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.ERROR);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.log(joinPoint, annotation);

          assertThat(capturedOutput).contains("ERROR " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void logExitedAbnormallyMessage_ignoredExceptionsContainNull(
      final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenThrow(new RuntimeException());

          final LogAround annotation =
              mockLogAround(Level.ERROR, "foo", Arrays.array((Class<Exception>) null));
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.ERROR);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.log(joinPoint, annotation);

          assertThat(capturedOutput).contains("ERROR " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void logElapsedMessage_defaultLevel(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".elapsed-level=DEBUG")
        .run(
            (context) -> {
              final LogAround annotation = mockLogAround(Level.DEFAULT, "foo", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.DEBUG);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.log(joinPoint, annotation);

              assertThat(capturedOutput).contains("DEBUG " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logElapsedMessage_customLevel(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".elapsed-level=DEBUG")
        .run(
            (context) -> {
              final LogAround annotation = mockLogAround(Level.INFO, "foo", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.DEBUG);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.log(joinPoint, annotation);

              assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logElapsedMessage_defaultMessage(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".elapsed-message=foo")
        .run(
            (context) -> {
              final LogAround annotation = mockLogAround(Level.INFO, "", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.log(joinPoint, annotation);

              assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logElapsedMessage_customMessage(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          final LogAround annotation = mockLogAround(Level.INFO, "foo", null);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.log(joinPoint, annotation);

          assertThat(capturedOutput).contains("INFO " + Foo.class.getName() + " - foo");
        });
  }

  @Test
  void logElapsedMessage_disabled(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              final LogAround annotation = mockLogAround(Level.INFO, "foo", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.log(joinPoint, annotation);

              assertThat(capturedOutput).doesNotContain("INFO " + Foo.class.getName() + " - foo");
            });
  }

  @Test
  void logElapsedMessage_loggerLevelDisabled(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          final LogAround annotation = mockLogAround(Level.DEBUG, "foo", null);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.log(joinPoint, annotation);

          assertThat(capturedOutput).doesNotContain("DEBUG " + Foo.class.getName() + " - foo");
        });
  }

  private MethodSignature mockMethodSignature(
      final Class<?> declaringClass, final String methodName, Class<?>... methodParameterTypes)
      throws NoSuchMethodException {
    final MethodSignature methodSignature = mock(MethodSignature.class);

    when(methodSignature.getDeclaringType()).thenReturn(declaringClass);
    when(methodSignature.getMethod())
        .thenReturn(declaringClass.getDeclaredMethod(methodName, methodParameterTypes));
    when(methodSignature.getReturnType()).thenReturn(String.class);

    return methodSignature;
  }

  private ProceedingJoinPoint mockJoinPoint(final MethodSignature methodSignature) {
    final ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);

    when(joinPoint.getSignature()).thenReturn(methodSignature);

    return joinPoint;
  }

  private LogAround mockLogAround(
      final Level level,
      final String message,
      final Class<? extends Throwable>[] ignoreExceptions) {
    final LogAround annotation = mock(LogAround.class);

    when(annotation.level()).thenReturn(level);
    when(annotation.exitedAbnormallyLevel()).thenReturn(level);
    when(annotation.enteringMessage()).thenReturn(message);
    when(annotation.exitedMessage()).thenReturn(message);
    when(annotation.exitedAbnormallyMessage()).thenReturn(message);
    when(annotation.elapsedMessage()).thenReturn(message);
    when(annotation.ignoreExceptions()).thenReturn(ignoreExceptions);

    return annotation;
  }

  private LogAround mockLogAroundForExitAbnormally(
      final Level level,
      final String message,
      final Class<? extends Throwable>[] ignoreExceptions) {
    final LogAround annotation = mock(LogAround.class);

    when(annotation.level()).thenReturn(Level.DEFAULT);
    when(annotation.exitedAbnormallyLevel()).thenReturn(level);
    when(annotation.enteringMessage()).thenReturn("");
    when(annotation.exitedMessage()).thenReturn("");
    when(annotation.exitedAbnormallyMessage()).thenReturn(message);
    when(annotation.elapsedMessage()).thenReturn("");
    when(annotation.ignoreExceptions()).thenReturn(ignoreExceptions);

    return annotation;
  }
}
