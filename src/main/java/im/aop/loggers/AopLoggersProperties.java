package im.aop.loggers;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import im.aop.loggers.logging.Level;

@Validated
@ConfigurationProperties(prefix = AopLoggersProperties.PREFIX)
public class AopLoggersProperties {

  public static final String PREFIX = "im.aop.loggers";

  private boolean enabled = true;

  private Level enteringLevel = Level.DEBUG;

  @NotBlank private String enteringMessage = "Entering [{method}] with parameters [{parameters}]";

  @NotNull private Level exitedLevel = Level.DEBUG;

  @NotBlank
  private String exitedMessage = "[{method}] exited normally with return value [{return-value}]";

  @NotNull private Level exitedAbnormallyLevel = Level.ERROR;

  @NotBlank
  private String exitedAbnormallyMessage =
      "[{method}] exited abnormally with exception [{exception}]";

  @NotNull private Level elapsedLevel = Level.DEBUG;

  @NotBlank private String elapsedMessage = "[{method}] elapsed [{elapsed}]";

  private Class<? extends Throwable>[] ignoreExceptions;

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public Level getEnteringLevel() {
    return enteringLevel;
  }

  public void setEnteringLevel(Level enteringLevel) {
    this.enteringLevel = enteringLevel;
  }

  public String getEnteringMessage() {
    return enteringMessage;
  }

  public void setEnteringMessage(String enteringMessage) {
    this.enteringMessage = enteringMessage;
  }

  public Level getExitedLevel() {
    return exitedLevel;
  }

  public void setExitedLevel(Level exitedLevel) {
    this.exitedLevel = exitedLevel;
  }

  public String getExitedMessage() {
    return exitedMessage;
  }

  public void setExitedMessage(String exitedMessage) {
    this.exitedMessage = exitedMessage;
  }

  public Level getExitedAbnormallyLevel() {
    return exitedAbnormallyLevel;
  }

  public void setExitedAbnormallyLevel(Level exitedAbnormallyLevel) {
    this.exitedAbnormallyLevel = exitedAbnormallyLevel;
  }

  public String getExitedAbnormallyMessage() {
    return exitedAbnormallyMessage;
  }

  public void setExitedAbnormallyMessage(String exitedAbnormallyMessage) {
    this.exitedAbnormallyMessage = exitedAbnormallyMessage;
  }

  public Level getElapsedLevel() {
    return elapsedLevel;
  }

  public void setElapsedLevel(Level elapsedLevel) {
    this.elapsedLevel = elapsedLevel;
  }

  public String getElapsedMessage() {
    return elapsedMessage;
  }

  public void setElapsedMessage(String elapsedMessage) {
    this.elapsedMessage = elapsedMessage;
  }

  public Class<? extends Throwable>[] getIgnoreExceptions() {
    return ignoreExceptions;
  }

  public void setIgnoreExceptions(Class<? extends Throwable>[] ignoreExceptions) {
    this.ignoreExceptions = ignoreExceptions;
  }
}
