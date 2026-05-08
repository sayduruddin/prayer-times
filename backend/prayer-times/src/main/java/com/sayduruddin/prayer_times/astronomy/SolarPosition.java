package com.sayduruddin.prayer_times.astronomy;

public class SolarPosition {
    // denoted as T
    public static double calculateJulianCentury(double JulianDay) {
        return (JulianDay - 2451545.0) / 36525;
    };

    // denoted as Lo
    public static double calculateMeanLongitude(double julianCentury) {
        double longitude = 280.46646 + ( 36000.76983 * julianCentury );
        double moddedLongitude = longitude % 360;

        // if the date entered is before the year 2000, which is when the Julian Century
        // is calculated relative to, then longitude will be negative.
        // add 360 to turn into positive.
        if (moddedLongitude < 0) {
            moddedLongitude += 360;
        }

        return moddedLongitude;
    };

    // denoted as M
    public static double calculateMeanAnomaly(double julianCentury) {
        double anomaly = 357.52911 + ( 35999.05029 * julianCentury );

        double moddedAnomaly = anomaly % 360;

        if (moddedAnomaly < 0) {
            moddedAnomaly += 360;
        }

        return moddedAnomaly;
    }

    // denoted as C
    public static double calculateEquationOfCentre(double meanAnomaly) {
        double meanAnomalyInRadians = Math.toRadians(meanAnomaly);

        return 1.914602 * Math.sin(meanAnomalyInRadians) + 0.019993 * Math.sin(2 * meanAnomalyInRadians) + 0.000289 * Math.sin(3 * meanAnomalyInRadians);
    }

    // denoted as a lambda
    public static double calculateTrueLongitude(double meanLongitude, double equationOfCentre) {
        double trueLongitude = (meanLongitude + equationOfCentre) % 360;
        if (trueLongitude < 0) {
            trueLongitude += 360;
        }
        return trueLongitude;
    }

    // TODO: calculate the declination and equation of time
}
