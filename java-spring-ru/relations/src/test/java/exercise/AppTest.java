package exercise;

import exercise.model.Article;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import com.github.database.rider.junit5.api.DBRider;
import com.github.database.rider.core.api.dataset.DataSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import exercise.repository.ArticleRepository;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@DBRider
// Файл с данными для тестов (фикстуры)
@DataSet("articles.yml")

public class AppTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ArticleRepository articleRepository;

    @Test
    void testRootPage() throws Exception {
        MockHttpServletResponse response = mockMvc
            .perform(get("/"))
            .andReturn()
            .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains("Welcome to Spring");
    }

    @Test
    void testGetArticles() throws Exception {
        MockHttpServletResponse response = mockMvc
            .perform(get("/articles"))
            .andReturn()
            .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains("The Moving Toyshop");
        assertThat(response.getContentAsString()).contains("Death Be Not Proud");
    }

    @Test
    void testGetArticle() throws Exception {
        var existingArticleName = "Dying of the Light";
        var existingArticleId = TestUtils.getArticleIdByName(mockMvc, existingArticleName);
        MockHttpServletResponse response = mockMvc
            .perform(get("/articles/{id}", existingArticleId))
            .andReturn()
            .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains("Dying of the Light");
        assertThat(response.getContentAsString()).contains("Give me honorable enemies rather than ambitious ones");
        assertThat(response.getContentAsString()).contains("Cinema");
    }

    @Test
    void testCreateArticle() throws Exception {
        String content = "{\"name\": \"Test article\", \"body\": \"Test body\", \"category\": {\"id\": \"5\"}}";
        mockMvc.perform(
                post("/articles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content))
                .andExpect(status().isOk());

        Article actualArticle = articleRepository.findByName("Test article");
        assertNotNull(actualArticle);
        assertThat(actualArticle.getBody()).isEqualTo("Test body");
        assertThat(actualArticle.getCategory().getId()).isEqualTo(5);

    }

    @Test
    void testUpdateArticle() throws Exception {
        var existingArticleName = "The Moving Toyshop";
        var existingArticleId = TestUtils.getArticleIdByName(mockMvc, existingArticleName);
        String content = "{\"name\": \"Updated article\", \"body\": \"Updated body\", \"category\": {\"id\": \"5\"}}";

        mockMvc.perform(
                patch("/articles/{id}", existingArticleId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content))
                .andExpect(status().isOk());

        Article actualArticle = articleRepository.findById(existingArticleId);
        assertThat(actualArticle.getName()).isEqualTo("Updated article");
        assertThat(actualArticle.getBody()).isEqualTo("Updated body");
        assertThat(actualArticle.getCategory().getId()).isEqualTo(5);
    }

    @Test
    void testDeleteArticle() throws Exception {
        var existingArticleName = "The Moving Toyshop";
        var existingArticleId = TestUtils.getArticleIdByName(mockMvc, existingArticleName);

        mockMvc.perform(delete("/articles/{id}", existingArticleId))
            .andExpect(status().isOk());

        assertNull(articleRepository.findById(existingArticleId));
    }
}
