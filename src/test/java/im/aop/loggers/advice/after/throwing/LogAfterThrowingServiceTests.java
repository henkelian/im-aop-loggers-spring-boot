package im.aop.loggers.advice.after.throwing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.aspectj.lang.JoinPoint;
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
import im.aop.loggers.advice.after.returning.LogAfterReturningService;

/**
 * Tests for {@link LogAfterReturningService}.
 *
 * @author Andy Lian
 */
@ExtendWith(OutputCaptureExtension.class)
class LogAfterThrowingServiceTests {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withUserConfiguration(AopLoggersPropertiesTestConfiguration.class)
          .withBean(LogAfterThrowingService.class);

  @TestConfiguration(proxyBeanMethods = false)
  @EnableConfigurationProperties({AopLoggersProperties.class})
  static class AopLoggersPropertiesTestConfiguration {}

  static class Foo {

    void foo() {}
  }

  private MethodSignature methodSignature;

  private JoinPoint joinPoint;

  @BeforeEach
  void beforeEach() throws NoSuchMethodException {
    methodSignature = mockMethodSignature(Foo.class, "foo");
    joinPoint = mockJoinPoint(methodSignature);
  }

  @Test
  void logExitedAbnormallyMessage(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          final LogAfterThrowing annotation = mockLogAfterThrowing(Level.ERROR, "", null);
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.ERROR);

          final LogAfterThrowingService service = context.getBean(LogAfterThrowingService.class);
          service.log(joinPoint, annotation, new RuntimeException("foo"));

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
              final LogAfterThrowing annotation = mockLogAfterThrowing(Level.ERROR, "", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.ERROR);

              final LogAfterThrowingService service =
                  context.getBean(LogAfterThrowingService.class);
              service.log(joinPoint, annotation, new RuntimeException("foo"));

              assertThat(capturedOutput)
                  .doesNotContain(
                      "ERROR "
                          + Foo.class.getName()
                          + " - [void foo()] exited abnormally with exception [type=RuntimeException, message=foo]");
            });
  }

  @Test
  void logExitedAbnormallyMessage_isGlobalIgnoreException(final CapturedOutput capturedOutput) {
    runner
        .withPropertyValues(
            AopLoggersProperties.PREFIX + ".ignore-exceptions=java.lang.RuntimeException")
        .run(
            (context) -> {
              final LogAfterThrowing annotation = mockLogAfterThrowing(Level.ERROR, "", null);
              LoggingSystem.get(ClassLoader.getSystemClassLoader())
                  .setLogLevel(Foo.class.getName(), LogLevel.ERROR);

              final LogAfterThrowingService service =
                  context.getBean(LogAfterThrowingService.class);
              service.log(joinPoint, annotation, new RuntimeException("foo"));

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
          final LogAfterThrowing annotation =
              mockLogAfterThrowing(Level.ERROR, "", Arrays.array(RuntimeException.class));
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.ERROR);

          final LogAfterThrowingService service = context.getBean(LogAfterThrowingService.class);
          service.log(joinPoint, annotation, new RuntimeException("foo"));

          assertThat(capturedOutput)
              .doesNotContain(
                  "ERROR "
                      + Foo.class.getName()
                      + " - [void foo()] exited abnormally with exception [type=RuntimeException, message=foo]");
        });
  }

  @Test
  void logExitedAbnormallyMessage_notIgnoredException(final CapturedOutput capturedOutput) {
    runner.run(
        (context) -> {
          final LogAfterThrowing annotation =
              mockLogAfterThrowing(Level.ERROR, "", Arrays.array(ClassNotFoundException.class));
          LoggingSystem.get(ClassLoader.getSystemClassLoader())
              .setLogLevel(Foo.class.getName(), LogLevel.ERROR);

          final LogAfterThrowingService service = context.getBean(LogAfterThrowingService.class);
          service.log(joinPoint, annotation, new RuntimeException("foo"));

          assertThat(capturedOutput)
              .contains(
                  "ERROR "
                      + Foo.class.getName()
                      + " - [void foo()] exited abnormally with exception [type=RuntimeException, message=foo]");
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

  private JoinPoint mockJoinPoint(final MethodSignature methodSignature) {
    final JoinPoint joinPoint = mock(JoinPoint.class);

    when(joinPoint.getSignature()).thenReturn(methodSignature);

    return joinPoint;
  }

  private LogAfterThrowing mockLogAfterThrowing(
      final Level level,
      final String exitedAbnormallyMessage,
      final Class<? extends Throwable>[] ignoreExceptions) {
    final LogAfterThrowing annotation = mock(LogAfterThrowing.class);

    when(annotation.level()).thenReturn(level);
    when(annotation.exitedAbnormallyMessage()).thenReturn(exitedAbnormallyMessage);
    when(annotation.ignoreExceptions()).thenReturn(ignoreExceptions);

    return annotation;
  }
}
