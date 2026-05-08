package com.sayduruddin.prayer_times.astronomy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SolarPositionTest {

    double FifthMay2026JulianCentury = 0.2633949349760438;
    double FifthMay2026MeanLongitude = 42.88688846036894;
    double FifthMay2026MeanAnomaly = 119.49662033388086;
    double FifthMay2026EqOfC = 1.6492966180538637;
    double FifthMay2026TrueLong = 44.53619269468434;

    @Test
    void calculateJulianCentury() {
        double result = SolarPosition.calculateJulianCentury(2461165.5);
        assertEquals(FifthMay2026JulianCentury, result, 1e-12);
        // the delta is the value that 2 numbers can be off by, in this case,
        // it fails if the values are not the same past the 12th decimal
    }

    @Test
    void calculateMeanLongitudeFor5thMay2026() {
        double T = SolarPosition.calculateJulianCentury(2461165.5);
        double result = SolarPosition.calculateMeanLongitude(T);
        assertEquals(FifthMay2026MeanLongitude, result, 1e-9);
    }

    @Test
    void calculateMeanLongitudeFor24thMay1985() {
        double JD = JulianDate.calculateJulianDay(1985, 5, 24);
        double T = SolarPosition.calculateJulianCentury(JD);
        double result = SolarPosition.calculateMeanLongitude(T);
        assertEquals(61.54496984353227, result, 1e-9);
    }

    @Test
    void calculateMeanAnomalyFor5thMay2026() {
        double T = SolarPosition.calculateJulianCentury(2461165.5);
        double result = SolarPosition.calculateMeanAnomaly(T);

        assertEquals(FifthMay2026MeanAnomaly, result, 1e-9);
    }

    @Test
    void calculateMeanAnomalyFor24thMay1985() {
        double JD = JulianDate.calculateJulianDay(1985, 5, 24);
        double T = SolarPosition.calculateJulianCentury(JD);
        double result = SolarPosition.calculateMeanAnomaly(T);

        assertEquals(138.85880685708526, result, 1e-9);
    }

    @Test
    void calculateEquationOfCentre5thMay2026 () {
        double meanAnomaly = 119.49662033388086;
        double result = SolarPosition.calculateEquationOfCentre(meanAnomaly);

        assertEquals(FifthMay2026EqOfC, result, 1e-9);
    }

    @Test
    void calculateEquationOfCentre24thMay1985 () {
        double meanAnomaly = 138.85880685708526;
        double result = SolarPosition.calculateEquationOfCentre(meanAnomaly);

        assertEquals(1.2400782288112706, result, 1e-9);
    }

    @Test
    void calculateTrueLongitudeFor5thMay2026 () {
        double meanLongitude = 42.88688846036894;
        double equationOfCentre = 1.6493042343154045;

        double result = SolarPosition.calculateTrueLongitude(meanLongitude, equationOfCentre);

        assertEquals(FifthMay2026TrueLong, result, 1e-9);
    }

    @Test
    void calculateTrueLongitudeFor24thMay1985 () {
        double meanLongitude = 61.54496984353227;
        double equationOfCentre = 1.2398370232704647;

        double result = SolarPosition.calculateTrueLongitude(meanLongitude, equationOfCentre);

        assertEquals(62.78480686680274, result, 1e-9);
    }
}
