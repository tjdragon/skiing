package skiing;

import static java.lang.Math.*;

public class Physics {
    public static final double G = 9.81;
    public static final double DEFAULT_LENGTH = 10.0;
    public static final double SKF = 0.05; // Snow kinetic friction

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
    public static double acceleration(final double slopeAngle, final double kineticFriction, final Orientation orientation) {
        final double factor = orientation == Orientation.UP ? -1.0 : 1.0;
        return factor * G * sin(slopeAngle) - kineticFriction * cos(slopeAngle);
    }

    // Returns the final speed given a distanceTravelled and initial acceleration
    public static double finalSpeed(final double initialSpeed, final double acceleration, final double distance) {
        return sqrt(initialSpeed * initialSpeed + 2.0 * acceleration * distance);
    }

    // Returns the distanceTravelled travelled knowing initial and final speeds and acceleration
    public static double distanceTravelled(final double initialSpeed, final double finalSpeed, final double acceleration) {
        final double t = (finalSpeed - initialSpeed) / acceleration;
        final double d = initialSpeed * t + acceleration * t * t / 2.0;
        return d;
    }

    public static double stoppingDistance(final double initialSpeed, final double kineticFriction) {
        return initialSpeed * initialSpeed / (2.0 * kineticFriction * G);
    }

    // Converts radians to degrees
    public static double toDegrees(final double radians) {
        return radians * 180.0 / Math.PI;
    }

    private static void log(final Object o) {
        System.out.println("" + o);
    }

    // Simple test case going down only
    private static void testCase1() {
        log("* TEST CASE 1 *");

        double height = 1;
        double length = 1;
        double minSlope = minSlopeAngle(SKF);
        double slopeAngle = slopeAngle(height, length);
        double slopeLength = slopeLength(height, length);
        double acceleration = acceleration(slopeAngle, SKF, Orientation.DOWN);
        double initialSpeed = 0.0;
        double finalSpeed = finalSpeed(initialSpeed, acceleration, slopeLength);
        log("height           : " + height);
        log("length           : " + length);
        log("min slope        : " + toDegrees(minSlope));
        log("slope angle      : " + toDegrees(slopeAngle));
        log("slope length     : " + slopeLength);
        log("acceleration down: " + acceleration);
        log("initial speed    : " + initialSpeed);
        log("final speed      : " + finalSpeed);
    }

    // Simple test case going down and up, same angle, no friction
    private static void testCase2() {
        log("* TEST CASE 2 *");

        double kineticFriction = 0.0;
        double height = 1;
        double length = 1;
        double minSlope = minSlopeAngle(kineticFriction);
        double slopeAngle = slopeAngle(height, length);
        double slopeLength = slopeLength(height, length);
        double acceleration = acceleration(slopeAngle, kineticFriction, Orientation.DOWN);
        double initialSpeed = 0.0;
        double finalSpeed = finalSpeed(initialSpeed, acceleration, slopeLength);
        // Now going back up
        acceleration = acceleration(slopeAngle, kineticFriction, Orientation.UP);
        initialSpeed = finalSpeed;
        finalSpeed = finalSpeed(initialSpeed, acceleration, slopeLength);

        log("height           : " + height);
        log("length           : " + length);
        log("min slope        : " + toDegrees(minSlope));
        log("slope angle      : " + toDegrees(slopeAngle));
        log("slope length     : " + slopeLength);
        log("acceleration down: " + acceleration);
        log("initial speed    : " + initialSpeed);
        log("final speed      : " + finalSpeed);
    }

    // Simple test case going down only, with friction and initial speed
    private static void testCase3() {
        log("* TEST CASE 3 *");

        double height = 1;
        double length = 3;
        double kineticFriction = 0.4;
        double minSlope = minSlopeAngle(kineticFriction);
        double slopeAngle = slopeAngle(height, length);
        double slopeLength = slopeLength(height, length);
        double acceleration = acceleration(slopeAngle, kineticFriction, Orientation.DOWN);
        double initialSpeed = 0.0;
        double finalSpeed = finalSpeed(initialSpeed, acceleration, slopeLength);
        double distance = distanceTravelled(initialSpeed, finalSpeed, acceleration);
        double stoppingDistance = stoppingDistance(initialSpeed, kineticFriction);
        log("height            : " + height);
        log("length            : " + length);
        log("min slope         : " + toDegrees(minSlope));
        log("slope angle       : " + toDegrees(slopeAngle));
        log("slope length      : " + slopeLength);
        log("acceleration down : " + acceleration);
        log("initial speed     : " + initialSpeed);
        log("final speed       : " + finalSpeed);
        log("distanceTravelled : " + distance);
        log("stoppingDistance  : " + stoppingDistance);
    }

    public static void main(String[] args) {
        testCase1();
        testCase2();
        testCase3();
    }
}
