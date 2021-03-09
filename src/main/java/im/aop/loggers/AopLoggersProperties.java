package im.aop.loggers;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = AopLoggersProperties.PREFIX)
public class AopLoggersProperties {

  public static final String PREFIX = "im.aop.loggers";

  static final String DEFAULT_ENTERING_MESSAGE =
      "Entering [{method}] with parameters [{parameters}]";

  static final String DEFAULT_EXITED_MESSAGE =
      "[{method}] exited normally with return value [{return-value}]";

  static final String DEFAULT_EXITED_ABNORMALLY_MESSAGE =
      "[{method}] exited abnormally with exception [{exception}]";

  static final String DEFAULT_ELAPSED_MESSAGE = "[{method}] elapsed [{elapsed}]";

  private boolean enabled = true;

  @NotBlank private String enteringMessage = DEFAULT_ENTERING_MESSAGE;

  @NotBlank private String exitedMessage = DEFAULT_EXITED_MESSAGE;

  @NotBlank private String exitedAbnormallyMessage = DEFAULT_EXITED_ABNORMALLY_MESSAGE;

  @NotBlank private String elapsedMessage = DEFAULT_ELAPSED_MESSAGE;

  private Class<? extends Throwable>[] ignoreExceptions;

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getEnteringMessage() {
    return enteringMessage;
  }

  public void setEnteringMessage(String enteringMessage) {
    this.enteringMessage = enteringMessage;
  }

  public String getExitedMessage() {
    return exitedMessage;
  }

  public void setExitedMessage(String exitedMessage) {
    this.exitedMessage = exitedMessage;
  }

  public String getExitedAbnormallyMessage() {
    return exitedAbnormallyMessage;
  }

  public void setExitedAbnormallyMessage(String exitedAbnormallyMessage) {
    this.exitedAbnormallyMessage = exitedAbnormallyMessage;
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
