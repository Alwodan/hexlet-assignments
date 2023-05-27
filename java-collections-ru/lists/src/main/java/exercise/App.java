package exercise;

import java.util.ArrayList;
import java.util.List;

// BEGIN
public class App {
    //тут лучше было бы использовать LinkedList, но я так понимаю домашнее задание требует
    //реализации именно через ArrayList
    public static boolean scrabble(String mess, String word) {
        word = word.toLowerCase();
        mess = mess.toLowerCase();
        List<String> chars = new ArrayList<>(List.of(mess.split("", 0)));
        for (int i = 0; i < word.length(); i++) {
            if (chars.contains(String.valueOf(word.charAt(i)))) {
                chars.remove(String.valueOf(word.charAt(i)));
            } else {
                return false;
            }
        }
        return true;
    }
}
//END
