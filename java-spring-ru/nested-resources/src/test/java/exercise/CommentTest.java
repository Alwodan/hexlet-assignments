package exercise;

import com.fasterxml.jackson.databind.ObjectMapper;
import exercise.dto.CommentDto;
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

import exercise.repository.CommentRepository;
import exercise.model.Comment;

import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DBRider
@DataSet("posts.yml")

public class CommentTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CommentRepository commentRepository;

    private static ObjectMapper mapper = new ObjectMapper();

    // Проверяем вывод всех комментариев конкретного поста
    @Test
    void testGetAllCommentsForPost() throws Exception {
        MockHttpServletResponse response = mockMvc
            .perform(get("/posts/1/comments"))
            .andReturn()
            .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        // Должны выводится только комментарии нужного поста
        assertThat(response.getContentAsString()).contains("Great Post!");
        assertThat(response.getContentAsString()).contains("Awesome!");
        // Комментарии другого поста выводится не должны
        assertThat(response.getContentAsString()).doesNotContain("So so");
    }

    // Проверяем вывод конкретного комментария конкретного поста
    @Test
    void testGetCommentForPost() throws Exception {
        MockHttpServletResponse response = mockMvc
            .perform(get("/posts/1/comments/1"))
            .andReturn()
            .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains("Great Post!");

        // Комментарий, принадлежащий другому посту, выводиться не должен
        // Комментарий с id=3 принадлежит посту с id=2
        MockHttpServletResponse response2 = mockMvc
            .perform(get("/posts/1/comments/3"))
            .andReturn()
            .getResponse();

        assertThat(response2.getStatus()).isEqualTo(404);
    }

    // Проверяем создание комментария для поста
    @Test
    void testCreateCommentForPost() throws Exception {
        CommentDto dto = new CommentDto("Test comment");

        mockMvc.perform(
                post("/posts/3/comments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk());


        // Проверяем, что созданный комментарий выводится у поста
        MockHttpServletResponse response = mockMvc
            .perform(get("/posts/3/comments"))
            .andReturn()
            .getResponse();

        assertThat(response.getContentAsString()).contains("Test comment");
        // Проверяем, что комментарий добавился в базу и общее количество комментариев увеличилось
        assertThat(commentRepository.count()).isEqualTo(6L);
    }

    // Проверяем обновление комментария для поста
    @Test
    void testUpdateComment() throws Exception {
        CommentDto dto = new CommentDto("Updated comment");

        mockMvc.perform(
                patch("/posts/2/comments/4")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        // Проверяем, что выводится обновлённый комментарий
        MockHttpServletResponse response = mockMvc
            .perform(get("/posts/2/comments"))
            .andReturn()
            .getResponse();

        assertThat(response.getContentAsString()).contains("Updated comment");
        // Проверяем, что комментарий обновился и в базе
        Comment updatedComment = commentRepository.findById(4L).get();
        assertThat(updatedComment.getContent()).isEqualTo(dto.content());
    }

    // Проверяем обновление комментария для поста
    // Если комментарий не существует или принадлежит другому посту, должен вернуться 404
    void testUpdateIncorrectComment() throws Exception {
        CommentDto dto = new CommentDto("Test comment");

        mockMvc.perform(
                patch("/posts/1/comments/4")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(dto)))
            .andExpect(status().isNotFound());
    }

    // Проверяем удаление комментария у поста
    @Test
    void testDeleteComment() throws Exception {
        mockMvc.perform(delete("/posts/1/comments/1"))
            .andExpect(status().isOk());

        // Проверяем, что комментарий удалился и больше не выводится для поста
        MockHttpServletResponse response = mockMvc
            .perform(get("/posts/1/comments"))
            .andReturn()
            .getResponse();

        assertThat(response.getContentAsString()).doesNotContain("Great Post!");
        // Проверяем, что комментарий удалился из базы
        assertThat(commentRepository.existsById(1L)).isFalse();
    }

    // Проверяем удаление комментария у поста
    // Если комментарий не существует или принадлежит другому посту, должен вернуться 404
    @Test
    void testDeleteIncorrectComment() throws Exception {
        mockMvc.perform(delete("/posts/1/comments/3"))
            .andExpect(status().isNotFound());
    }
}
