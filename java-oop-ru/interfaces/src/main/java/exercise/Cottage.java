package exercise;

// BEGIN
public class Cottage implements Home {

    double area;
    int floor;

    public Cottage(double area, int floor) {
        this.area = area;
        this.floor = floor;
    }

    @Override
    public double getArea() {
        return area;
    }

    @Override
    public int compareTo(Home another) {
        return Double.compare(getArea(), another.getArea());
    }

    public String toString() {
        return floor + " этажный коттедж площадью " + getArea() + " метров";
    }
}
// END
