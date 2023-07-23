package exercise;

import com.fasterxml.jackson.databind.ObjectMapper;
import exercise.dto.UserDto;
import exercise.model.User;
import exercise.repository.UserRepository;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional

public class AppTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository repository;

    @Autowired
    TestUtils testUtils;

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    void testGetUsers() throws Exception {
        testUtils.addDefaultUser();

        MockHttpServletResponse response = mockMvc
            .perform(get("/users"))
            .andReturn()
            .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains("fname", "lname", "test@mail.com");
    }

    @Test
    void testCreateUser() throws Exception {
        assertThat(repository.count()).isEqualTo(0);
        testUtils.addDefaultUser().andExpect(status().isCreated());
        assertThat(repository.count()).isEqualTo(1);
    }

    @Test
    void testGetUser() throws Exception {
        testUtils.addDefaultUser();
        User expectedUser = repository.findAll().iterator().next();

        MockHttpServletResponse response = mockMvc
            .perform(get("/users/{id}", expectedUser.getId()))
            .andReturn()
            .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains(expectedUser.getFirstName());
        assertThat(response.getContentAsString()).contains(expectedUser.getLastName());
    }

    @Test
    void testUpdateUser() throws Exception {
        testUtils.addDefaultUser();
        var id = testUtils.getUserId(TestUtils.TEST_EMAIL);
        final var userDto = new UserDto("new name", "new last name", "a@a.com");

        mockMvc.perform(
                patch("/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());


        assertThat(repository.findById(id)).isNotNull();
        assertThat(repository.findByEmail(TestUtils.TEST_EMAIL)).isNull();
        assertThat(repository.findByEmail("a@a.com")).isNotNull();
    }

    @Test
    void testUpdateUserWithNonExistedId() throws Exception {
        testUtils.addDefaultUser();
        var id = testUtils.getUserId(TestUtils.TEST_EMAIL);
        var notExistingId = id + 1;
        final var userDto = new UserDto("new name", "new last name", "a@a.com");

        mockMvc.perform(
                patch("/users/{id}", notExistingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().isNotFound());

        assertThat(repository.findByEmail("a@a.com")).isNull();
    }

    @Test
    void testDeleteUser() throws Exception {
        testUtils.addDefaultUser();
        var id = testUtils.getUserId(TestUtils.TEST_EMAIL);

        mockMvc.perform(delete("/users/{id}", id))
            .andExpect(status().isOk());

        assertThat(repository.count()).isEqualTo(0);
    }

    @Test
    void testSwaggerUiPage() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
            .andExpect(status().isOk());
    }
}
