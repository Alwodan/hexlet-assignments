package exercise;

// BEGIN
public class App {
    public static void printSquare(Circle circle) {
        try {
            double result = circle.getSquare();
            System.out.println(Math.round(result));
        } catch (NegativeRadiusException e) {
            System.out.println("Не удалось посчитать площадь");
        }
        System.out.println("Вычисление окончено");
    }
}
// END
