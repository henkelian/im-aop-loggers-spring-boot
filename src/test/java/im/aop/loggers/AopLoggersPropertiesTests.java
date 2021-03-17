package im.aop.loggers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import im.aop.loggers.logging.Level;

/**
 * Tests for {@link AopLoggersProperties}.
 *
 * @author Andy Lian
 */
class AopLoggersPropertiesTests {

  @TestConfiguration(proxyBeanMethods = false)
  @EnableConfigurationProperties({AopLoggersProperties.class})
  static class AopLoggersPropertiesTestConfiguration {}

  private ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withUserConfiguration(AopLoggersPropertiesTestConfiguration.class);

  @Test
  void enabled_defaultValue() {
    runner.run(
        (context) -> {
          final AopLoggersProperties properties = context.getBean(AopLoggersProperties.class);
          assertThat(properties.isEnabled()).isTrue();
        });
  }

  @Test
  void enabled_withPropertyValue() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".enabled=false")
        .run(
            (context) -> {
              final AopLoggersProperties properties = context.getBean(AopLoggersProperties.class);
              assertThat(properties.isEnabled()).isFalse();
            });
  }

  @Test
  void enteringLevel_defaultValue() {
    runner.run(
        (context) -> {
          final AopLoggersProperties properties = context.getBean(AopLoggersProperties.class);
          assertThat(properties.getEnteringLevel()).isEqualTo(Level.DEBUG);
        });
  }

  @Test
  void enteringLevel_withPropertyValue() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".entering-level=TRACE")
        .run(
            (context) -> {
              final AopLoggersProperties properties = context.getBean(AopLoggersProperties.class);
              assertThat(properties.getEnteringLevel()).isEqualTo(Level.TRACE);
            });
  }

  @Test
  void enteringMessage_defaultValue() {
    runner.run(
        (context) -> {
          final AopLoggersProperties properties = context.getBean(AopLoggersProperties.class);
          assertThat(properties.getEnteringMessage())
              .isEqualTo("Entering [{method}] with parameters [{parameters}]");
        });
  }

  @Test
  void enteringMessage_withPropertyValue() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".entering-message=foo")
        .run(
            (context) -> {
              final AopLoggersProperties properties = context.getBean(AopLoggersProperties.class);
              assertThat(properties.getEnteringMessage()).isEqualTo("foo");
            });
  }

  @Test
  void exitedLevel_defaultValue() {
    runner.run(
        (context) -> {
          final AopLoggersProperties properties = context.getBean(AopLoggersProperties.class);
          assertThat(properties.getExitedLevel()).isEqualTo(Level.DEBUG);
        });
  }

  @Test
  void exitedLevel_withPropertyValue() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".exited-level=TRACE")
        .run(
            (context) -> {
              final AopLoggersProperties properties = context.getBean(AopLoggersProperties.class);
              assertThat(properties.getExitedLevel()).isEqualTo(Level.TRACE);
            });
  }

  @Test
  void exitedMessage_defaultValue() {
    runner.run(
        (context) -> {
          final AopLoggersProperties properties = context.getBean(AopLoggersProperties.class);
          assertThat(properties.getExitedMessage())
              .isEqualTo("[{method}] exited normally with return value [{return-value}]");
        });
  }

  @Test
  void exitedMessage_withPropertyValue() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".exited-message=foo")
        .run(
            (context) -> {
              final AopLoggersProperties properties = context.getBean(AopLoggersProperties.class);
              assertThat(properties.getExitedMessage()).isEqualTo("foo");
            });
  }

  @Test
  void exitedAbnormallyLevel_defaultValue() {
    runner.run(
        (context) -> {
          final AopLoggersProperties properties = context.getBean(AopLoggersProperties.class);
          assertThat(properties.getExitedAbnormallyLevel()).isEqualTo(Level.ERROR);
        });
  }

  @Test
  void exitedAbnormallyLevel_withPropertyValue() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".exited-abnormally-level=TRACE")
        .run(
            (context) -> {
              final AopLoggersProperties properties = context.getBean(AopLoggersProperties.class);
              assertThat(properties.getExitedAbnormallyLevel()).isEqualTo(Level.TRACE);
            });
  }

  @Test
  void exitedAbnormallyMessage_defaultValue() {
    runner.run(
        (context) -> {
          final AopLoggersProperties properties = context.getBean(AopLoggersProperties.class);
          assertThat(properties.getExitedAbnormallyMessage())
              .isEqualTo("[{method}] exited abnormally with exception [{exception}]");
        });
  }

  @Test
  void exitedAbnormallyMessage_withPropertyValue() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".exited-abnormally-message=foo")
        .run(
            (context) -> {
              final AopLoggersProperties properties = context.getBean(AopLoggersProperties.class);
              assertThat(properties.getExitedAbnormallyMessage()).isEqualTo("foo");
            });
  }

  @Test
  void ignoreExceptions_defaultValue() {
    runner.run(
        (context) -> {
          final AopLoggersProperties properties = context.getBean(AopLoggersProperties.class);
          assertThat(properties.getIgnoreExceptions()).isNull();
        });
  }

  @Test
  void ignoreExceptions_withPropertyValue() {
    runner
        .withPropertyValues(
            AopLoggersProperties.PREFIX + ".ignore-exceptions[0]=java.lang.RuntimeException")
        .run(
            (context) -> {
              final AopLoggersProperties properties = context.getBean(AopLoggersProperties.class);
              assertThat(properties.getIgnoreExceptions()).containsExactly(RuntimeException.class);
            });
  }

  @Test
  void elapsedLevel_defaultValue() {
    runner.run(
        (context) -> {
          final AopLoggersProperties properties = context.getBean(AopLoggersProperties.class);
          assertThat(properties.getElapsedLevel()).isEqualTo(Level.DEBUG);
        });
  }

  @Test
  void elapsedLevel_withPropertyValue() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".elapsed-level=TRACE")
        .run(
            (context) -> {
              final AopLoggersProperties properties = context.getBean(AopLoggersProperties.class);
              assertThat(properties.getElapsedLevel()).isEqualTo(Level.TRACE);
            });
  }

  @Test
  void elapsedMessage_defaultValue() {
    runner.run(
        (context) -> {
          final AopLoggersProperties properties = context.getBean(AopLoggersProperties.class);
          assertThat(properties.getElapsedMessage()).isEqualTo("[{method}] elapsed [{elapsed}]");
        });
  }

  @Test
  void elapsedMessage_withPropertyValue() {
    runner
        .withPropertyValues(AopLoggersProperties.PREFIX + ".elapsed-message=foo")
        .run(
            (context) -> {
              final AopLoggersProperties properties = context.getBean(AopLoggersProperties.class);
              assertThat(properties.getElapsedMessage()).isEqualTo("foo");
            });
  }
}
