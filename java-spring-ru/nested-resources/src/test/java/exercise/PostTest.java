package exercise;

import com.fasterxml.jackson.databind.ObjectMapper;
import exercise.dto.PostDto;
import exercise.model.Post;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import exercise.repository.PostRepository;

import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DBRider
@DataSet("posts.yml")

public class PostTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PostRepository postRepository;

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    void testGetPosts() throws Exception {
        MockHttpServletResponse response = mockMvc
            .perform(get("/posts"))
            .andReturn()
            .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString())
            .contains("The Moving Toyshop", "Why is it that when one man builds a wall");
        assertThat(response.getContentAsString())
            .contains("The Monkey`s Raincoat", "Do the dead frighten you?");
    }

    @Test
    void testGetPost() throws Exception {
        MockHttpServletResponse response = mockMvc
            .perform(get("/posts/4"))
            .andReturn()
            .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains("The Monkey`s Raincoat");
        assertThat(response.getContentAsString()).contains("Do the dead frighten you");
    }

    @Test
    void testCreatePost() throws Exception {
        PostDto dto = new PostDto("Test post", "Test body");

        mockMvc.perform(
                post("/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(dto)))
            .andExpect(status().isOk());

        MockHttpServletResponse response = mockMvc
            .perform(get("/posts"))
            .andReturn()
            .getResponse();

        assertThat(response.getContentAsString()).contains("Test post", "Test body");
        assertThat(postRepository.existsByTitle("Test post")).isTrue();
    }

    @Test
    void testUpdatePost() throws Exception {
        PostDto dto = new PostDto("Updated post", "Updated body");

        mockMvc.perform(
                patch("/posts/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(dto)))
            .andExpect(status().isOk());

        MockHttpServletResponse response = mockMvc
            .perform(get("/posts"))
            .andReturn()
            .getResponse();

        assertThat(response.getContentAsString()).contains("Updated post", "Updated body");
        assertThat(response.getContentAsString())
            .doesNotContain("The Moving Toyshop", "Why is it that when one man builds a wall");

        Post updatedPost = postRepository.findById(1L).orElse(null);
        assertThat(updatedPost).isNotNull();
        assertThat(updatedPost.getTitle()).isEqualTo(dto.title());
        assertThat(updatedPost.getBody()).isEqualTo(dto.body());
    }

    @Test
    void testDeletePost() throws Exception {
        MockHttpServletResponse responsePost = mockMvc
            .perform(delete("/posts/1"))
            .andReturn()
            .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(200);

        MockHttpServletResponse response = mockMvc
            .perform(get("/posts"))
            .andReturn()
            .getResponse();

        assertThat(response.getContentAsString())
            .doesNotContain("The Moving Toyshop", "Why is it that when one man builds a wall");
        assertThat(postRepository.existsById(1L)).isFalse();
    }
}
