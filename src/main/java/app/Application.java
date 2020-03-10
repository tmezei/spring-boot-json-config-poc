package app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// The default name of the PropertySource is "default", see the factory class
// CONFIG_FILE can be a system property or an environment variable:
// CONFIG_FILE=file:///path/to/config.json java -jar app.jar
@PropertySource(value = "${CONFIG_FILE}", factory = JsonPropertySourceFactory.class)
// Could use classpath scanning instead: @ConfigurationPropertiesScan(...)
@EnableConfigurationProperties(Config.class)
@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @RestController
  static class Endpoints {

    private final Config config;
    private final Environment env;
    private final int value;
    private final String appName;

    // @Autowire-ing all of these would've worked but yuck
    Endpoints(
        Config config,
        Environment env,
        @Value("${CHILD_ITEM.NUM_VALUE}") int value,
        @Value("${APP_NAME}") String appName) {
      this.config = config;
      this.env = env;
      this.value = value;
      this.appName = appName;
    }

    @GetMapping("/")
    public Map<String, Object> env() {
      return Map.of(
        "@Value APP_NAME", appName,
        "@Value CHILD_ITEM.NUM_VALUE", value,
        "@ConfigurationProperties appName", config.getAppName(),
        "@ConfigurationProperties config.childItem.numValue", config.getChildItem().getNumValue(),
        "Environment CHILD_ITEM.NUM_VALUE", env.getProperty("CHILD_ITEM.NUM_VALUE", "not found")
      );
    }
  }

}
