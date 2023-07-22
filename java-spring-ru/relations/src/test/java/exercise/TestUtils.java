package exercise;

import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;


public class TestUtils {
    private static ObjectMapper mapper = new ObjectMapper();

    public static int getArticleIdByName(MockMvc mockMvc, String name) throws Exception {
        var response = mockMvc
                .perform(get("/articles"))
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();

        List<Map> articles = mapper.readValue(body, List.class);
        var existingArticle = articles.stream()
                .filter(article -> article.get("name").equals(name))
                .findAny()
                .get();

        return (int) existingArticle.get("id");
    }
}
