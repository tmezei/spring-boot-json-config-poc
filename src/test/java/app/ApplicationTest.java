package app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "CONFIG_FILE=classpath:unit_test.json") // CONFIG_FILE reference
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class ApplicationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper mapper;

  @Test
  public void parsesApplicationConfig() throws Exception {
    var expected = Map.of(
        "@Value APP_NAME", "JSON Config Test App",
        "@Value CHILD_ITEM.NUM_VALUE", 895,
        "Environment CHILD_ITEM.NUM_VALUE", "895",
        "@ConfigurationProperties config.childItem.numValue", 895,
        "@ConfigurationProperties appName", "JSON Config Test App"
    );

    mockMvc
        .perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(content().json(mapper.writeValueAsString(expected)));
  }
}
