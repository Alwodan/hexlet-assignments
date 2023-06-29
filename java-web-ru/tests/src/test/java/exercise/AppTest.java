package exercise;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import io.javalin.Javalin;
import io.ebean.DB;

import exercise.domain.User;
import exercise.domain.query.QUser;
import io.ebean.Database;

class AppTest {

    private static Javalin app;
    private static String baseUrl;

    // BEGIN
    @BeforeAll
    public static void beforeAll() {
        app = App.getApp();
        app.start(0);
        int port = app.port();
        baseUrl = "http://localhost:" + port;
    }

    @AfterAll
    public static void afterAll() {
        app.stop();
    }
    // END

    // Между тестами база данных очищается
    // Благодаря этому тесты не влияют друг на друга
    @BeforeEach
    void beforeEach() {
        Database db = DB.getDefault();
        db.truncate("users");
        User existingUser = new User("Wendell", "Legros", "a@a.com", "123456");
        existingUser.save();
    }

    @Test
    void testUsers() {

        // Выполняем GET запрос на адрес http://localhost:port/users
        HttpResponse<String> response = Unirest
            .get(baseUrl + "/users")
            .asString();
        // Получаем тело ответа
        String content = response.getBody();

        // Проверяем код ответа
        assertThat(response.getStatus()).isEqualTo(200);
        // Проверяем, что страница содержит определенный текст
        assertThat(response.getBody()).contains("Wendell Legros");
    }

    @Test
    void testNewUser() {

        HttpResponse<String> response = Unirest
            .get(baseUrl + "/users/new")
            .asString();

        assertThat(response.getStatus()).isEqualTo(200);
    }

    // BEGIN
    @Test
    void testNewValidUser() {

        HttpResponse<String> response = Unirest
                .post(baseUrl + "/users")
                .field("firstName", "Andrew")
                .field("lastName", "Tate")
                .field("email", "aboba@gmail.com")
                .field("password", "2222222222")
                .asString();

        assertThat(response.getStatus()).isEqualTo(302);

        User newUser = new QUser().lastName.equalTo("Tate").findOne();

        assertThat(newUser).isNotNull();

        assertThat(newUser.getFirstName()).isEqualTo("Andrew");
        assertThat(newUser.getEmail()).isEqualTo("aboba@gmail.com");
        assertThat(newUser.getPassword()).isEqualTo("2222222222");
    }

    @Test
    void testNewInvalidUser() {

        HttpResponse<String> response = Unirest
                .post(baseUrl + "/users")
                .field("firstName", "")
                .field("lastName", "")
                .field("email", "aboba")
                .field("password", "h")
                .asString();

        assertThat(response.getStatus()).isEqualTo(422);

        User newUser = new QUser().email.equalTo("aboba").findOne();

        assertThat(newUser).isNull();
    }
    // END
}
