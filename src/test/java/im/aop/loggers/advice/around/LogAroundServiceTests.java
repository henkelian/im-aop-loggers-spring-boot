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
import org.slf4j.event.Level;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import im.aop.loggers.AopLoggersProperties;

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
  void logEnteringMessage(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenReturn("foo");

          final LogAround annotation = mockLogAround(Level.INFO, "", "", Level.ERROR, "", "", null);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.log(joinPoint, annotation);

          assertThat(capturedOutput)
              .contains(
                  "INFO "
                      + Foo.class.getName()
                      + " - Entering [void foo()] with parameters [none]");
        });
  }

  @Test
  void logEnteringMessage_isDisabled(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              when(joinPoint.proceed()).thenReturn("foo");

              final LogAround annotation =
                  mockLogAround(Level.INFO, "", "", Level.ERROR, "", "", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.log(joinPoint, annotation);

              assertThat(capturedOutput)
                  .doesNotContain(
                      "INFO "
                          + Foo.class.getName()
                          + " - Entering [void foo()] with parameters [none]");
            });
  }

  @Test
  void logExitedMessage(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenReturn("foo");

          final LogAround annotation = mockLogAround(Level.INFO, "", "", Level.ERROR, "", "", null);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.log(joinPoint, annotation);

          assertThat(capturedOutput)
              .contains(
                  "INFO "
                      + Foo.class.getName()
                      + " - [void foo()] exited normally with return value [foo]");
        });
  }

  @Test
  void logExitedMessage_isDisabled(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              when(joinPoint.proceed()).thenReturn("foo");

              final LogAround annotation =
                  mockLogAround(Level.INFO, "", "", Level.ERROR, "", "", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.log(joinPoint, annotation);

              assertThat(capturedOutput)
                  .doesNotContain(
                      "INFO "
                          + Foo.class.getName()
                          + " - [void foo()] exited normally with return value [foo]");
            });
  }

  @Test
  void logExitedAbnormallyMessage(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenThrow(new RuntimeException("foo"));

          final LogAround annotation = mockLogAround(Level.INFO, "", "", Level.ERROR, "", "", null);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.log(joinPoint, annotation);

          assertThat(capturedOutput)
              .contains(
                  "ERROR "
                      + Foo.class.getName()
                      + " - [void foo()] exited abnormally with exception [type=RuntimeException, message=foo]");
        });
  }

  @Test
  void logExitedAbnormallyMessage_isDisabled(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              when(joinPoint.proceed()).thenThrow(new RuntimeException("foo"));

              final LogAround annotation =
                  mockLogAround(Level.INFO, "", "", Level.ERROR, "", "", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.log(joinPoint, annotation);

              assertThat(capturedOutput)
                  .doesNotContain(
                      "ERROR "
                          + Foo.class.getName()
                          + " - [void foo()] exited abnormally with exception [type=RuntimeException, message=foo]");
            });
  }

  @Test
  void logExitedAbnormallyMessage_isGlobalIgnoredException(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(
            AopLoggersProperties.PREFIX + ".ignore-exceptions=java.lang.RuntimeException")
        .run(
            (context) -> {
              when(joinPoint.proceed()).thenThrow(new RuntimeException("foo"));

              final LogAround annotation =
                  mockLogAround(Level.INFO, "", "", Level.ERROR, "", "", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.log(joinPoint, annotation);

              assertThat(capturedOutput)
                  .doesNotContain(
                      "ERROR "
                          + Foo.class.getName()
                          + " - [void foo()] exited abnormally with exception [type=RuntimeException, message=foo]");
            });
  }

  @Test
  void logExitedAbnormallyMessage_isIgnoreException(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenThrow(new RuntimeException("foo"));

          final LogAround annotation =
              mockLogAround(
                  Level.INFO, "", "", Level.ERROR, "", "", Arrays.array(RuntimeException.class));
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.log(joinPoint, annotation);

          assertThat(capturedOutput)
              .doesNotContain(
                  "ERROR "
                      + Foo.class.getName()
                      + " - [void foo()] exited abnormally with exception [type=RuntimeException, message=foo]");
        });
  }

  @Test
  void logExitedAbnormallyMessage_notIgnoreException(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          when(joinPoint.proceed()).thenThrow(new RuntimeException("foo"));

          final LogAround annotation =
              mockLogAround(
                  Level.INFO,
                  "",
                  "",
                  Level.ERROR,
                  "",
                  "",
                  Arrays.array(ClassNotFoundException.class));
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.log(joinPoint, annotation);

          assertThat(capturedOutput)
              .contains(
                  "ERROR "
                      + Foo.class.getName()
                      + " - [void foo()] exited abnormally with exception [type=RuntimeException, message=foo]");
        });
  }

  @Test
  void logElapsedMessage(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          final LogAround annotation = mockLogAround(Level.INFO, "", "", Level.ERROR, "", "", null);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.INFO);

          final LogAroundService service = context.getBean(LogAroundService.class);
          service.log(joinPoint, annotation);

          assertThat(capturedOutput)
              .contains("INFO " + Foo.class.getName() + " - [void foo()] elapsed [PT");
        });
  }

  @Test
  void logElapsedMessage_isDisabled(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              final LogAround annotation =
                  mockLogAround(Level.INFO, "", "", Level.ERROR, "", "", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.INFO);

              final LogAroundService service = context.getBean(LogAroundService.class);
              service.log(joinPoint, annotation);

              assertThat(capturedOutput)
                  .doesNotContain("INFO " + Foo.class.getName() + " - [void foo()] elapsed [PT");
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
      final String enteringMessage,
      final String exitedMessage,
      final Level exitedAbnormallyLevel,
      final String exitedAbnormallyMessage,
      final String elapsedMessage,
      final Class<? extends Throwable>[] ignoreExceptions) {
    final LogAround annotation = mock(LogAround.class);

    when(annotation.level()).thenReturn(Level.INFO);
    when(annotation.enteringMessage()).thenReturn("");
    when(annotation.exitedMessage()).thenReturn("");
    when(annotation.exitedAbnormallyLevel()).thenReturn(Level.ERROR);
    when(annotation.exitedAbnormallyMessage()).thenReturn("");
    when(annotation.elapsedMessage()).thenReturn("");
    when(annotation.ignoreExceptions()).thenReturn(ignoreExceptions);

    return annotation;
  }
}
