package exercise;

import com.fasterxml.jackson.databind.ObjectMapper;
import exercise.dto.UserDto;
import exercise.model.User;
import exercise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
public class TestUtils {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private UserRepository repository;

    private ObjectMapper mapper = new ObjectMapper();

    public static final String TEST_EMAIL = "test@mail.com";

    private final UserDto testDto = new UserDto(
            "fname",
            "lname",
            TEST_EMAIL
    );

    public long getUserId(String email) {
        User user = repository.findByEmail(email);
        return (long) user.getId();
    }

    public ResultActions addDefaultUser() throws Exception {
        return mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testDto)));

    }
}
