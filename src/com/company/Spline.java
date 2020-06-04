package com.company;


public class Spline {

    //coefficients
    private double a;
    private double b;
    private double c;

    //translations from axis
    private double x_translation;
    private double y_translation;
    double theta_rotation;

    //distance between endpoints
    double s;

    //some constants
    private double halfpi = Math.PI / 2;

    public Spline(Point p_0, Point p_1, Vector v_0, Vector v_1) {
        x_translation = p_0.x;
        y_translation = p_0.y;
        theta_rotation = Math.atan2(p_1.y - p_0.y, p_1.x - p_0.x);
        s = Math.sqrt((p_1.x - p_0.x)*(p_1.x - p_0.x) + (p_1.y - p_0.y)*(p_1.y - p_0.y));

        //make sure that the values are usable;
        assert v_0.x != 0: "Vector 1 vertical";
        assert v_1.x != 0: "Vector 2 vertical";

        //derivatives at endpoints
        double Pp0 = v_0.y / v_0.x;
        double Pps = v_1.y / v_1.x;

        //finding coefficients
        a = (Pp0 + Pps) / (s * s);
        b = -((2*Pp0) + Pps) / (s);
        c = Pp0;
    }

    public double getRawY(double percentageTravelled) {
        assert percentageTravelled <= 1 && percentageTravelled >= 0 : "percentage out of bounds";

        double deltaX = getDistanceTravelled(percentageTravelled);
        return a * deltaX * deltaX * deltaX + b * deltaX * deltaX + c * deltaX;
    }

    public double getRawHeading(double percentageTravelled) {
        assert percentageTravelled <= 1 && percentageTravelled >= 0 : "percentage out of bounds";

        double deltaX = getDistanceTravelled(percentageTravelled);
        return 3 * a * deltaX * deltaX + 2 * b * deltaX + c;
    }

    public double[] getRotatedCoordinates(double percentageTravelled) {
        assert percentageTravelled <= 1 && percentageTravelled >= 0 : "percentage out of bounds";

        double [] coordinates = new double[2];

        double deltaX = getDistanceTravelled(percentageTravelled);
        double deltaY = getRawY(percentageTravelled);

        double sinTheta = Math.sin(theta_rotation);
        double cosTheta = Math.cos(theta_rotation);

        coordinates[0] = deltaX * cosTheta - deltaY * sinTheta + x_translation;
        coordinates[1] = deltaX * sinTheta + deltaY * cosTheta + y_translation;
        return coordinates;
    }

    public double getRotatedAngle(double percentageTravelled) {
        double heading = Math.atan(getRawHeading(percentageTravelled)) + theta_rotation;

        //make sure angle is within pi and -pi
        while (heading >= Math.PI) {
            heading -= 2.0 * Math.PI;
        }
        while (heading < -Math.PI) {
            heading += 2.0 * Math.PI;
        }
        return heading;
    }

    public double getLength() {
        double n = 1000;
        double q;
        double dydq;
        double L = 0;
        double r;
        double rPrevious = Math.sqrt(1 + getRawHeading(0) * getRawHeading(0)) / n;

        for (int j = 1; j <= n; j++) {
            q = j / n;
            dydq = getRawHeading(q);

            r = Math.sqrt(1 + dydq * dydq) / n;
            L += (r + rPrevious) / 2;

            rPrevious = r;
        }
        return L * s;
    }

    public double getDistanceTravelled(double percentageTravelled) {
        return percentageTravelled * s;
    }

    public double getPercentageTravelled(double distanceTravelled) {
        double n = 1000;
        double q = 0;
        double dydq;
        double L = 0;
        double LPrevious = 0;
        double r;
        double rPrevious = Math.sqrt(1 + getRawHeading(0) * getRawHeading(0)) / n;

        double Sx = distanceTravelled / s;

        for (int j = 1; j <= n; j++) {
            q = j / n;
            dydq = getRawHeading(q);

            r = Math.sqrt(1 + dydq * dydq) / n;
            L += (r + rPrevious) / 2;

            if (L > Sx) {
                break;
            }

            LPrevious = L;
            rPrevious = r;
        }

        return q + (((Sx-LPrevious) / (L-LPrevious) -1) / n);
    }

    public String getCoefficients() {
        return "a = " + a + "; b = " + b + "; c = " + c;
    }
}
