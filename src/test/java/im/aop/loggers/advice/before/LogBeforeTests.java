package im.aop.loggers.advice.before;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.slf4j.event.Level;

/**
 * Tests for {@link LogBefore}.
 *
 * @author Andy Lian
 */
class LogBeforeTests {

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
    @LogBefore
    class Local {};

    final LogBefore annotation = Local.class.getAnnotation(LogBefore.class);
    assertThat(annotation.level()).isEqualTo(Level.INFO);
  }

  @Test
  void level_customValue() {
    @LogBefore(level = Level.DEBUG)
    class Local {};

    final LogBefore annotation = Local.class.getAnnotation(LogBefore.class);
    assertThat(annotation.level()).isEqualTo(Level.DEBUG);
  }

  @Test
  void enteringMessage_defaultValue() {
    @LogBefore
    class Local {};

    final LogBefore annotation = Local.class.getAnnotation(LogBefore.class);
    assertThat(annotation.enteringMessage()).isEmpty();
  }

  @Test
  void enteringMessage_customValue() {
    @LogBefore(enteringMessage = "foo")
    class Local {};

    final LogBefore annotation = Local.class.getAnnotation(LogBefore.class);
    assertThat(annotation.enteringMessage()).isEqualTo("foo");
  }
}
