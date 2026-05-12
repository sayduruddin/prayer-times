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
        double julianDay = JulianDate.calculateJulianDay(2026, 5, 5); // should be 2461165.5
        double T = SolarPosition.calculateJulianCentury(julianDay);

        double obliquity = SolarPosition.calculateObliquity(T);
        double meanLongitude = SolarPosition.calculateMeanLongitude(T);
        double omega =  SolarPosition.calculateOmega(T);
        double meanAnomaly = SolarPosition.calculateMeanAnomaly(T);

        double equationOfCentre = SolarPosition.calculateEquationOfCentre(meanAnomaly);
        double trueLongitude = SolarPosition.calculateTrueLongitude(meanLongitude, equationOfCentre);

        double appLongitude =  SolarPosition.calculateApparentLongitude(trueLongitude, omega);

        double declination = SolarPosition.calculateDeclination(obliquity, appLongitude);


        double result = PrayerTimeCalculator.calculateHourAngle(sunriseAngle, latitude, declination);
        assertEquals(115.05663895742978, result, 1e-5);

        // mean longitude required for equation of time.
        double EqT = SolarPosition.calculateEquationOfTime(T, obliquity, meanAnomaly, meanLongitude);
        double solarNoon = PrayerTimeCalculator.calculateSolarNoon(EqT, actualLongitude);

        double offsetFromSolarNoon = result * 4;
        double sunriseInMinutes = solarNoon - offsetFromSolarNoon;

        System.out.println("Sunrise for 5th May 2026 BST: " + PrayerTimeCalculator.formatMinutesToTime(sunriseInMinutes + 60));

        double maghribInMinutes = solarNoon + offsetFromSolarNoon;
//        System.out.println("Solar noon and eqt: " + PrayerTimeCalculator.formatMinutesToTime(solarNoon) + " " + EqT);
        System.out.println("Maghrib time for 5th May 2026 BST: " + PrayerTimeCalculator.formatMinutesToTime(maghribInMinutes + 60));

        double fajrH = PrayerTimeCalculator.calculateHourAngle(-18, latitude, declination);
        double fajrInMinutes = solarNoon - (fajrH * 4);
        System.out.println("Fajr time for 5th May 2026 BST: " + PrayerTimeCalculator.formatMinutesToTime(fajrInMinutes + 60));

        double ishaH = PrayerTimeCalculator.calculateHourAngle(-15, latitude, declination);
        double ishaInMinutes = solarNoon + ( ishaH * 4);
        System.out.println("Isha time for 5th May 2026 BST: " + PrayerTimeCalculator.formatMinutesToTime(ishaInMinutes + 60));

        double asrAngle = PrayerTimeCalculator.calculateAsrAngle(latitude, declination, 2);
        double asrH = PrayerTimeCalculator.calculateHourAngle(asrAngle, latitude, declination);
        double asrInMinutes = solarNoon + ( asrH * 4 );
        System.out.println("Asr time for 5th May 2026 BST: " + PrayerTimeCalculator.formatMinutesToTime(asrInMinutes + 60));

//        System.out.println("Asr angle: " + asrAngle);
//        System.out.println("Asr H: " + asrH);
//        System.out.println("Asr offset minutes: " + (asrH * 4));
//        System.out.println("Solar noon: " + solarNoon);
//        System.out.println("Asr raw UTC minutes: " + asrInMinutes);
    }

}
