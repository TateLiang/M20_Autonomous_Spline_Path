package com.company;

public class Main {

    public static void main(String[] args) {
        Point p0 = new Point(2, 3);
        Point p1 = new Point(5, 5);
        Vector v0 = new Vector(5, 3);
        Vector v1 = new Vector(3, 2);
        Spline cubicHermite = new Spline(p0, p1, v0, v1);

        double length = cubicHermite.getLength();
        System.out.println("Arc Length: " + length);
        System.out.println("Linear Length: " + cubicHermite.s);
        System.out.println("Theta rotation: " + cubicHermite.theta_rotation);
        System.out.println("Coefficients: ");
        System.out.println(cubicHermite.getCoefficients());
        System.out.println();

        System.out.println("Sample points: ");
        for (double k = 0; k <= 1.0; k += .05) {
            System.out.println(cubicHermite.getDistanceTravelled(k) + ", " + cubicHermite.getRawY(k));
        }
        System.out.println();
        System.out.println("Sample points rotated: ");
        for (double k = 0; k <= 1.0; k += .05) {
            System.out.println(cubicHermite.getRotatedCoordinates(k)[0] + ", " + cubicHermite.getRotatedCoordinates(k)[1]);
        }
    }
}

