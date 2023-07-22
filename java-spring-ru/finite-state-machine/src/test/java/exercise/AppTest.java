package exercise;

import com.fasterxml.jackson.databind.ObjectMapper;
import exercise.dto.PostDto;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import exercise.repository.PostRepository;
import exercise.model.Post;
import exercise.model.PostState;

import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DBRider
// Файл с данными для тестов (фикстуры)
@DataSet("articles.yml")

public class AppTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PostRepository postRepository;

    @Autowired
    TestUtils testUtils;

    ObjectMapper mapper = new ObjectMapper();

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
    void testGetPosts() throws Exception {
        MockHttpServletResponse response = mockMvc
            .perform(get("/posts"))
            .andReturn()
            .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        var content = response.getContentAsString();
        assertThat(content).contains("Death Be Not Proud");
        assertThat(content).contains("The Monkey`s Raincoat");
        // Выводим только опубликованные посты
        assertThat(content).doesNotContain("The Moving Toyshop");
    }

    @Test
    void testGetPost() throws Exception {
        var existingPostTitle = "The Monkey`s Raincoat";
        var existingPostId = testUtils.getPostId(existingPostTitle);

        MockHttpServletResponse response = mockMvc
            .perform(get("/posts/{id}", existingPostId))
            .andReturn()
            .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains(existingPostTitle);
        assertThat(response.getContentAsString()).contains("Do the dead frighten you?");
    }

    @Test
    void testCreatePost() throws Exception {
        PostDto dto = new PostDto();
        var title = "Test post";
        dto.setTitle(title);
        dto.setBody("Test body");

        mockMvc.perform(
                post("/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        // Проверяем, что пост есть в базе данных
        Post post = postRepository.findByTitle(title);
        assertThat(post).isNotNull();
        assertThat(post.getTitle()).isEqualTo(title);

        // Проверяем, что новый пост создаётся в начальном состоянии CREATED
        assertThat(post.getState()).isEqualTo(PostState.CREATED);
    }

    @Test
    void testPublishPost() throws Exception {
        var unpublishedPostTitle = "The Moving Toyshop";
        var unpublishedPostId = testUtils.getPostId(unpublishedPostTitle);

        mockMvc.perform(patch("/posts/{id}/publish", unpublishedPostId))
                .andExpect(status().isOk());

        Post post = postRepository.findById(unpublishedPostId).get();
        assertThat(post.getState()).isEqualTo(PostState.PUBLISHED);
    }

    @Test
    void testArchivePost() throws Exception {
        var unpublishedPostTitle = "The Moving Toyshop";
        var unpublishedPostId = testUtils.getPostId(unpublishedPostTitle);

        mockMvc.perform(patch("/posts/{id}/archive", unpublishedPostId))
            .andExpect(status().isOk());

        Post post1 = postRepository.findById(unpublishedPostId).get();
        assertThat(post1.getState()).isEqualTo(PostState.ARCHIVED);

        var publishedPostTitle = "Death Be Not Proud";
        var publishedPostId = testUtils.getPostId(publishedPostTitle);

        mockMvc.perform(patch("/posts/{id}/archive", publishedPostId))
            .andExpect(status().isOk());

        Post post2 = postRepository.findById(publishedPostId).get();
        assertThat(post2.getState()).isEqualTo(PostState.ARCHIVED);
    }

    @Test
    void testArchiveArchivedPost() throws Exception {
        var archivedPostTitle = "Dying of the Light";
        var archivedPostId = testUtils.getPostId(archivedPostTitle);

        mockMvc.perform(patch("/posts/{id}/archive", archivedPostId))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testPublishPublishedPost() throws Exception {
        var publishedPostTitle = "Dying of the Light";
        var publishedPostId = testUtils.getPostId(publishedPostTitle);

        mockMvc.perform(patch("/posts/{id}/publish", publishedPostId))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testPublishArchivedPost() throws Exception {
        var archivedPostTitle = "Dying of the Light";
        var archivedPostId = testUtils.getPostId(archivedPostTitle);

        mockMvc.perform(patch("/posts/{id}/publish", archivedPostId))
                .andExpect(status().isUnprocessableEntity());
    }
}
