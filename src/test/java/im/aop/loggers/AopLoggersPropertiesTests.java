package im.aop.loggers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

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
  void enteringMessage_defaultValue() {
    runner.run(
        (context) -> {
          final AopLoggersProperties properties = context.getBean(AopLoggersProperties.class);
          assertThat(properties.getEnteringMessage())
              .isEqualTo(AopLoggersProperties.DEFAULT_ENTERING_MESSAGE);
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
  void exitedMessage_defaultValue() {
    runner.run(
        (context) -> {
          final AopLoggersProperties properties = context.getBean(AopLoggersProperties.class);
          assertThat(properties.getExitedMessage())
              .isEqualTo(AopLoggersProperties.DEFAULT_EXITED_MESSAGE);
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
  void exitedAbnormallyMessage_defaultValue() {
    runner.run(
        (context) -> {
          final AopLoggersProperties properties = context.getBean(AopLoggersProperties.class);
          assertThat(properties.getExitedAbnormallyMessage())
              .isEqualTo(AopLoggersProperties.DEFAULT_EXITED_ABNORMALLY_MESSAGE);
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
  void elapsedMessage_defaultValue() {
    runner.run(
        (context) -> {
          final AopLoggersProperties properties = context.getBean(AopLoggersProperties.class);
          assertThat(properties.getElapsedMessage())
              .isEqualTo(AopLoggersProperties.DEFAULT_ELAPSED_MESSAGE);
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
}
