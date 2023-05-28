package exercise;

import java.util.List;

// BEGIN
public class App {
    public static int getCountOfFreeEmails(List<String> emails) {
        return (int) emails.stream()
                .filter(str -> str.contains("@gmail.com") || str.contains("@yandex.ru") || str.contains("@hotmail.com"))
                .count();
    }
}
// END
