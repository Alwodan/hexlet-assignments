package exercise.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Имя пользователя, которое будет использоваться при аутентификации
    private String username;

    private String email;

    // Поле, которое содержит пароль пользователя
    // С этим паролем будет сравниваться пароль, введённый при аутентификации
    // Чтобы не показывать пароль пользователя, используем аннотацию JsonIgnore
    // Это поле jackson будет игнорировать при сериализации объекта пользователя в JSON строку
    @JsonIgnore
    private String password;
}
