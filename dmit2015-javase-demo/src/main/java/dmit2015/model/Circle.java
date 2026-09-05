package dmit2015.model;

/**
 * This class models a Circle shape.
 *
 * @author Sam Wu
 * @version 2026.09.06
 */
public class Circle {
    // Define a field to track the radius
    private double radius;

    // Define getters/setters to encapsulate access to data fields

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    // Define a default constructor to create circle with a radius of 1

    public Circle() {
        radius = 1;
    }
    // Define a constructor to create a circle with a specific radius

    public Circle(double radius) {
//        this.radius = radius;
        setRadius(radius);
    }

    // Define instance-level methods to calculate area of circle
    public double area() {
        return Math.PI * radius * radius;
    }

    static void main(String[] args) {
        Circle currentCircle = new Circle();
        currentCircle.setRadius(5);
        System.out.printf("Radius: %s, Area = %.2f",
                currentCircle.getRadius(),
                currentCircle.area());
    }

}
