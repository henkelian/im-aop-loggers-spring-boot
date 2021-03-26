package im.aop.loggers.advice.around;

import static org.assertj.core.api.Assertions.assertThat;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import im.aop.loggers.AopLoggersProperties;

/**
 * Tests for {@link LogAroundAdvice}.
 *
 * @author Andy Lian
 */
@ExtendWith(OutputCaptureExtension.class)
class LogAroundAdviceTests {

  private ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withUserConfiguration(LogAroundAdviceTestConfiguration.class)
          .withBean(LogAroundAdvice.class)
          .withBean(AopLoggersProperties.class);

  @EnableAspectJAutoProxy
  @TestConfiguration(proxyBeanMethods = false)
  static class LogAroundAdviceTestConfiguration {

    @Bean
    public LogAroundService logAroundService(final AopLoggersProperties aopLoggersProperties) {
      return new LogAroundService(aopLoggersProperties) {

        @Override
        public Object logAround(ProceedingJoinPoint joinPoint, LogAround logAround) throws Throwable {
          LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType())
              .info("{}", joinPoint);

          return joinPoint.proceed();
        }
      };
    }
  }

  static class TestMethodContext {

    @LogAround
    public void methodWithoutParameter() {}

    @LogAround
    public void methodWithParameter(String foo) {}

    @LogAround
    public String methodWithResult() {
      return "foo";
    }

    @LogAround
    @Override
    public String toString() {
      return super.toString();
    }
  }

  @Test
  void methodWithoutParameter_methodContext(final CapturedOutput capturedOutput) {
    runner
        .withBean(TestMethodContext.class)
        .run(
            context -> {
              final TestMethodContext methodContext = context.getBean(TestMethodContext.class);
              methodContext.methodWithoutParameter();

              assertThat(capturedOutput)
                  .contains(
                      "execution(void "
                          + TestMethodContext.class.getName()
                          + ".methodWithoutParameter())");
            });
  }

  @Test
  void methodWithParameter_methodContext(final CapturedOutput capturedOutput) {
    runner
        .withBean(TestMethodContext.class)
        .run(
            context -> {
              final TestMethodContext methodContext = context.getBean(TestMethodContext.class);
              methodContext.methodWithParameter("foo");

              assertThat(capturedOutput)
                  .contains(
                      "execution(void "
                          + TestMethodContext.class.getName()
                          + ".methodWithParameter(String))");
            });
  }

  @Test
  void methodWithResult_methodContext(final CapturedOutput capturedOutput) {
    runner
        .withBean(TestMethodContext.class)
        .run(
            context -> {
              final TestMethodContext methodContext = context.getBean(TestMethodContext.class);
              methodContext.methodWithResult();

              assertThat(capturedOutput)
                  .contains(
                      "execution(String "
                          + TestMethodContext.class.getName()
                          + ".methodWithResult())");
            });
  }

  @Test
  void methodWithResult_methodContext_willReturnValue(final CapturedOutput capturedOutput) {
    runner
        .withBean(TestMethodContext.class)
        .run(
            context -> {
              final TestMethodContext methodContext = context.getBean(TestMethodContext.class);
              assertThat(methodContext.methodWithResult()).isEqualTo("foo");
            });
  }

  @Test
  void toString_methodContext(final CapturedOutput capturedOutput) {
    runner
        .withBean(TestMethodContext.class)
        .run(
            context -> {
              final TestMethodContext methodContext = context.getBean(TestMethodContext.class);
              methodContext.toString();

              assertThat(capturedOutput)
                  .contains(
                      "execution(String " + TestMethodContext.class.getName() + ".toString())");
            });
  }

  @LogAround
  static class TestClassContext {

    public void methodWithoutParameter() {}

    public void methodWithParameter(String foo) {}

    public String methodWithResult() {
      return "foo";
    }

    @Override
    public String toString() {
      return super.toString();
    }
  }

  @Test
  void methodWithoutParameter_classContext(final CapturedOutput capturedOutput) {
    runner
        .withBean(TestClassContext.class)
        .run(
            context -> {
              final TestClassContext classContext = context.getBean(TestClassContext.class);
              classContext.methodWithoutParameter();

              assertThat(capturedOutput)
                  .contains(
                      "execution(void "
                          + TestClassContext.class.getName()
                          + ".methodWithoutParameter())");
            });
  }

  @Test
  void methodWithParameter_classContext(final CapturedOutput capturedOutput) {
    runner
        .withBean(TestClassContext.class)
        .run(
            context -> {
              final TestClassContext classContext = context.getBean(TestClassContext.class);
              classContext.methodWithParameter("foo");

              assertThat(capturedOutput)
                  .contains(
                      "execution(void "
                          + TestClassContext.class.getName()
                          + ".methodWithParameter(String))");
            });
  }

  @Test
  void methodWithResult_classContext(final CapturedOutput capturedOutput) {
    runner
        .withBean(TestClassContext.class)
        .run(
            context -> {
              final TestClassContext classContext = context.getBean(TestClassContext.class);
              classContext.methodWithResult();

              assertThat(capturedOutput)
                  .contains(
                      "execution(String "
                          + TestClassContext.class.getName()
                          + ".methodWithResult())");
            });
  }

  @Test
  void methodWithResult_classContext_willReturnValue(final CapturedOutput capturedOutput) {
    runner
        .withBean(TestClassContext.class)
        .run(
            context -> {
              final TestClassContext classContext = context.getBean(TestClassContext.class);
              assertThat(classContext.methodWithResult()).isEqualTo("foo");
            });
  }

  @Test
  void toString_classContext(final CapturedOutput capturedOutput) {
    runner
        .withBean(TestClassContext.class)
        .run(
            context -> {
              final TestClassContext classContext = context.getBean(TestClassContext.class);
              classContext.toString();

              assertThat(capturedOutput)
                  .doesNotContain(
                      "execution(String " + TestClassContext.class.getName() + ".toString())");
            });
  }

  @Test
  void codeCoverage_publicMethod() {
    runner.run(
        (context) -> {
          final LogAroundAdvice logAroundAdvice = context.getBean(LogAroundAdvice.class);
          logAroundAdvice.publicMethod();
        });
  }

  @Test
  void codeCoverage_toStringMethod() {
    runner.run(
        (context) -> {
          final LogAroundAdvice logAroundAdvice = context.getBean(LogAroundAdvice.class);
          logAroundAdvice.toStringMethod();
        });
  }

  @Test
  void codeCoverage_logAroundMethodContext() {
    runner.run(
        (context) -> {
          final LogAroundAdvice logAroundAdvice = context.getBean(LogAroundAdvice.class);
          logAroundAdvice.logAroundMethodContext(null);
        });
  }

  @Test
  void codeCoverage_logAroundClassContext() {
    runner.run(
        (context) -> {
          final LogAroundAdvice logAroundAdvice = context.getBean(LogAroundAdvice.class);
          logAroundAdvice.logAroundClassContext(null);
        });
  }
}
