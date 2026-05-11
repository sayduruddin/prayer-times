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

    public static double calculateObliquity(double julianCentury) {
        double meanObliquityForJ2000Epoch = 23.439291111;
        // the rate of change is the degree of tilt change per Julian Century
        double rateOfChange = 0.013004167;
        return meanObliquityForJ2000Epoch - (rateOfChange * julianCentury);
    }

    // Earth doesn't spin perfectly, spins slightly, the wobble is called nutation.
    // Nutation has a cycle of about 18.6 years, Omega tracks where Earth is in the wobble cycle.
    public static double calculateOmega(double julianCentury) {
        double startingPositionOnJ2000 = 125.04;
        double wobbleCycleChangesPerCentury = 1934.136;
        double omega = (startingPositionOnJ2000 - (wobbleCycleChangesPerCentury * julianCentury));
        double moddedOmega = omega % 360;

        if (moddedOmega < 0) {
            moddedOmega += 360;
        }
        return moddedOmega;
    }

    public static double calculateApparentLongitude(double longitude, double omega) {
        double aberrationCorrection = 0.00569;
        double nutationCorrection = 0.00478 * Math.sin(Math.toRadians(omega));
        return longitude - aberrationCorrection - nutationCorrection;
    }

    public static double calculateDeclination(double obliquity, double apparentLongitude) {
        double sineObliquity = Math.sin(Math.toRadians(obliquity));
        double sineApparentLongitude = Math.sin(Math.toRadians(apparentLongitude));

        return Math.toDegrees(Math.asin(sineObliquity * sineApparentLongitude));
    }

    // this function calculates the difference between solar noon and 12:00 on your clock
    public static double calculateEquationOfTime(double julianCentury, double obliquity, double meanAnomaly, double meanLongitude) {
        // formula requires eccentricity to be calculated and a helper value of y from obliquity
        double eccentricityForJ2000 = 0.016708634;
        double eccentricityRate = 0.000042037;
        double eccentricity = eccentricityForJ2000 - (eccentricityRate * julianCentury);

        double obliquityRadians = Math.toRadians(obliquity);
        double meanAnomalyRadians = Math.toRadians(meanAnomaly);
        double meanLongitudeRadians = Math.toRadians(meanLongitude);
        double meanLongitudeRadiansDoubled = meanLongitudeRadians * 2;

        double y = Math.pow(Math.tan(obliquityRadians / 2), 2);

        double axialTiltCorrection = y * Math.sin(meanLongitudeRadiansDoubled);
        double ellipticalOrbitCorrection = 2 * eccentricity * Math.sin(meanAnomalyRadians);
        double tiltEclipseInteraction = 4 * eccentricity * y * Math.sin(meanAnomalyRadians) * Math.cos(meanLongitudeRadiansDoubled);
        double secondOrderTiltCorrection = 0.5 * Math.pow(y, 2) * Math.sin(4 * meanLongitudeRadians);
        double secondOrderEllipseCorrection = 1.25 * Math.pow(eccentricity, 2) * Math.sin(2 * meanAnomalyRadians);

        double firstCalculation = axialTiltCorrection - ellipticalOrbitCorrection + tiltEclipseInteraction - secondOrderTiltCorrection - secondOrderEllipseCorrection;

        return 4 * Math.toDegrees(firstCalculation);
    }


}
