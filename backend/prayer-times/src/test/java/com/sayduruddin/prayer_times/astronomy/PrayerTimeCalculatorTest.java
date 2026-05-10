package com.sayduruddin.prayer_times.astronomy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PrayerTimeCalculatorTest {

    @Test
    void calculateSolarNoonFor5thMay2026() {
        double result = PrayerTimeCalculator.calculateSolarNoon(3.25, -1.9);
        assertEquals(724.35, result);
    }

    @Test
    void formatMinutesToTime() {
        String result = PrayerTimeCalculator.formatMinutesToTime(724.35);
        assertEquals("12:04", result);
    }

    @Test
    void calculateHourAngleForSunriseKeighley5thMay2026() {
        double sunriseAngle = -0.833;
//        double latitude = 53.8684021;
        double latitude = 53.8675;
        double actualLongitude = -1.9069;
        // TODO: add in a function that calculates JD for you
        double T = SolarPosition.calculateJulianCentury(2461165.5);

        double obliquity = SolarPosition.calculateObliquity(T);
        double meanLongitude = SolarPosition.calculateMeanLongitude(T);
        double omega =  SolarPosition.calculateOmega(T);
        double meanAnomaly = SolarPosition.calculateMeanAnomaly(T);

        double equationOfCentre = SolarPosition.calculateEquationOfCentre(meanAnomaly);
        double trueLongitude = SolarPosition.calculateTrueLongitude(meanLongitude, equationOfCentre);

        double appLongitude =  SolarPosition.calculateApparentLongitude(trueLongitude, omega);

        double declination = SolarPosition.calculateDeclination(obliquity, appLongitude);


        double result = PrayerTimeCalculator.calculateHourAngle(sunriseAngle, latitude, declination);
//        assertEquals(114.24957096919297, result, 1e-5);

//        double actualLongitude = -1.9078;

        // mean longitude required for equation of time.
        double EqT = SolarPosition.calculateEquationOfTime(T, obliquity, meanAnomaly, meanLongitude);
        double solarNoon = PrayerTimeCalculator.calculateSolarNoon(EqT, actualLongitude);

        double offsetFromSolarNoon = result * 4;
        double sunriseInMinutes = solarNoon - offsetFromSolarNoon;

        System.out.println(PrayerTimeCalculator.formatMinutesToTime(sunriseInMinutes));
        System.out.println("T: " + T);
        System.out.println("Mean Longitude: " + meanLongitude);
        System.out.println("Mean Anomaly: " + meanAnomaly);
        System.out.println("Obliquity: " + obliquity);
        System.out.println("Omega: " + omega);
        System.out.println("Apparent Longitude: " + SolarPosition.calculateApparentLongitude(meanLongitude, omega));
        System.out.println("Declination: " + declination);
        System.out.println("EqT: " + EqT);
        System.out.println("Solar Noon UTC minutes: " + solarNoon);
        System.out.println("Solar Noon UTC: " + PrayerTimeCalculator.formatMinutesToTime(solarNoon));
        System.out.println("Hour Angle: " + result);
        System.out.println("Offset minutes: " + (result * 4));
        System.out.println("Sunrise UTC minutes: " + sunriseInMinutes);
        System.out.println("Sunrise UTC: " + PrayerTimeCalculator.formatMinutesToTime(sunriseInMinutes));
    }

}
