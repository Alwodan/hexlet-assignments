package exercise;

// BEGIN
interface Home extends Comparable<Home> {
    double getArea();
    int compareTo(Home another);
}
// END
