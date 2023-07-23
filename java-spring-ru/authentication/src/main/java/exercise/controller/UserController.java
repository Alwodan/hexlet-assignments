package exercise.controller;

import exercise.dto.UserDto;
import exercise.model.User;
import exercise.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    // Кодировщик BCrypt
    // Используйте для хеширования пароля
    private final PasswordEncoder encoder;

    @GetMapping(path = "")
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    // BEGIN
    @PostMapping(path = "")
    public User createUser(@RequestBody UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.username());
        user.setEmail(userDto.email());
        user.setPassword(encoder.encode(userDto.password()));

        return userRepository.save(user);
    }
    // END
}
