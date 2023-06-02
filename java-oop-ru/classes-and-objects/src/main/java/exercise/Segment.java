package exercise;

// BEGIN
public class Segment {
    Point beginPoint;
    Point endPoint;

    public Segment(Point beginPoint, Point endPoint) {
        this.beginPoint = beginPoint;
        this.endPoint = endPoint;
    }

    public Point getMidPoint() {
        int x1 = beginPoint.getX();
        int y1 = beginPoint.getY();
        int x2 = endPoint.getX();
        int y2 = endPoint.getY();

        return new Point((x1 + x2) / 2, (y1 + y2) / 2);
    }

    public Point getBeginPoint() {
        return beginPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }
}
// END
