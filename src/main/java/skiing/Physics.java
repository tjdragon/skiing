package skiing;

import static java.lang.Math.*;

public class Physics {
    public static final double G = 9.81;
    public static final double DEFAULT_LENGTH = 1.0;
    public static final double SKF = 0.4; // Snow kinetic friction

    // Returns the angle of the slope in radians
    public static double slopeAngle(final double height, final double length) {
        return atan(height / length);
    }

    // Returns the angle of the slope in radians
    public static double slopeAngle(final double height) {
        return slopeAngle(height, DEFAULT_LENGTH);
    }

    // Returns the minimum slope angle in radians required for an object to start moving down from no initial speed
    public static double minSlopeAngle(final double kineticFriction) {
        return atan(kineticFriction);
    }

    // Returns the slope length considering the height and length
    public static double slopeLength(final double height, final double length) {
        return sqrt(height * height + length * length);
    }

    // Returns the acceleration up or down for a given slope angle in radians
    public static double acceleration(final double slopeAngle, final boolean up) {
        final double factor = up ? -1.0 : 1.0;
        return factor * G * sin(slopeAngle) - SKF * cos(slopeAngle);
    }

    // Returns the final speed given a distance and initial acceleration
    public static double finalSpeed(final double initialSpeed, final double acceleration, final double distance) {
        return sqrt(initialSpeed * initialSpeed + 2.0 * acceleration * distance);
    }

    // Returns the distance travelled knowing initial and final speeds and acceleration
    public static double distance(final double initialSpeed, final double finalSpeed, final double acceleration) {
        return (finalSpeed * finalSpeed - initialSpeed * initialSpeed) / 2.0 * acceleration;
    }

    // Converts radians to degrees
    public static double toDegrees(final double radians) {
        return radians * 180.0 / Math.PI;
    }

    private static void log(final Object o) {
        System.out.println("" + o);
    }

    public static void main(String[] args) {
        double height = 1;
        double length = 1;
        double minSlope = minSlopeAngle(SKF);
        double slopeAngle = slopeAngle(height, length);
        double slopeLength = slopeLength(height, length);
        double acceleration = acceleration(slopeAngle, false);
        double initialSpeed = 0.0;
        double finalSpeed = finalSpeed(initialSpeed, acceleration, slopeLength);
        log("height           : " + height);
        log("length           : " + length);
        log("min slope        : " + toDegrees(minSlope));
        log("slope angle      : " + toDegrees(slopeAngle));
        log("slope length     : " + slopeLength);
        log("acceleration down: " + acceleration);
        log("initial speed    : " + initialSpeed);
        log("final speed      :" + finalSpeed);
    }
}
