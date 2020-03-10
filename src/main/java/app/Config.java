package app;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

// Cheating - this is just a meta-annotation
@ApplicationConfig
public class Config {

  @NotBlank
  private final String appName;
  @Length(min = 5)
  private final String jdbcConnectionString;
  @NotNull
  private final ChildItem childItem;

  public Config(String appName, String jdbcConnectionString, ChildItem childItem) {
    this.appName = appName;
    this.jdbcConnectionString = jdbcConnectionString;
    this.childItem = childItem;
  }

  public String getAppName() {
    return appName;
  }

  public String getJdbcConnectionString() {
    return jdbcConnectionString;
  }

  public ChildItem getChildItem() {
    return childItem;
  }

  public static class ChildItem {

    @Min(895) @Max(895)
    private final int numValue;

    public ChildItem(int numValue) {
      this.numValue = numValue;
    }

    public int getNumValue() {
      return numValue;
    }
  }
}
