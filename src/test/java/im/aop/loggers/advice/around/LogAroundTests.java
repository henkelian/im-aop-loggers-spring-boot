package im.aop.loggers.advice.around;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import im.aop.loggers.advice.before.LogBefore;
import im.aop.loggers.logging.Level;

/**
 * Tests for {@link LogAround}.
 *
 * @author Andy Lian
 */
class LogAroundTests {

  @Test
  void declaringClass_defaultValue() {
    @LogBefore
    class Local {};

    final LogBefore annotation = Local.class.getAnnotation(LogBefore.class);
    assertThat(annotation.declaringClass()).isEqualTo(void.class);
  }

  @Test
  void declaringClass_customValue() {
    @LogBefore(declaringClass = Local.class)
    class Local {};

    final LogBefore annotation = Local.class.getAnnotation(LogBefore.class);
    assertThat(annotation.declaringClass()).isEqualTo(Local.class);
  }

  @Test
  void level_defaultValue() {
    @LogAround
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.level()).isEqualTo(Level.DEFAULT);
  }

  @Test
  void level_customValue() {
    @LogAround(level = Level.DEBUG)
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.level()).isEqualTo(Level.DEBUG);
  }

  @Test
  void enteringMessage_defaultValue() {
    @LogAround
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.enteringMessage()).isEmpty();
  }

  @Test
  void enteringMessage_customValue() {
    @LogAround(enteringMessage = "foo")
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.enteringMessage()).isEqualTo("foo");
  }

  @Test
  void exitedMessage_defaultValue() {
    @LogAround
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.exitedMessage()).isEmpty();
  }

  @Test
  void exitedMessage_customValue() {
    @LogAround(exitedMessage = "foo")
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.exitedMessage()).isEqualTo("foo");
  }

  @Test
  void exitedAbnormallyLevel_defaultValue() {
    @LogAround
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.exitedAbnormallyLevel()).isEqualTo(Level.DEFAULT);
  }

  @Test
  void exitedAbnormallyLevel_customValue() {
    @LogAround(exitedAbnormallyLevel = Level.DEBUG)
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.exitedAbnormallyLevel()).isEqualTo(Level.DEBUG);
  }

  @Test
  void exitedAbnormallyMessage_defaultValue() {
    @LogAround
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.exitedAbnormallyMessage()).isEmpty();
  }

  @Test
  void exitedAbnormallyMessage_customValue() {
    @LogAround(exitedAbnormallyMessage = "foo")
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.exitedAbnormallyMessage()).isEqualTo("foo");
  }

  @Test
  void elapsedMessage_defaultValue() {
    @LogAround
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.elapsedMessage()).isEmpty();
  }

  @Test
  void elapsedMessage_customValue() {
    @LogAround(elapsedMessage = "foo")
    class Local {};

    final LogAround annotation = Local.class.getAnnotation(LogAround.class);
    assertThat(annotation.elapsedMessage()).isEqualTo("foo");
  }
}
