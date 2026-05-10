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
    double FifthMay2026Omega = 335.5983740451745;
    double FifthMay2026Obliquity = 23.435865879278616;

    // all values for answers are cross checked against https://gml.noaa.gov/grad/solcalc/azel.html.
    // if the values are close enough, it works for me
    // TODO: move all 24th May 1985 helper values into their own helper object.

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
    void calculateEquationOfCentre5thMay2026() {
        double meanAnomaly = 119.49662033388086;
        double result = SolarPosition.calculateEquationOfCentre(meanAnomaly);

        assertEquals(FifthMay2026EqOfC, result, 1e-9);
    }

    @Test
    void calculateEquationOfCentre24thMay1985() {
        double meanAnomaly = 138.85880685708526;
        double result = SolarPosition.calculateEquationOfCentre(meanAnomaly);

        assertEquals(1.2400782288112706, result, 1e-9);
    }

    @Test
    void calculateTrueLongitudeFor5thMay2026() {
        double meanLongitude = 42.88688846036894;
        double equationOfCentre = 1.6493042343154045;

        double result = SolarPosition.calculateTrueLongitude(meanLongitude, equationOfCentre);

        assertEquals(FifthMay2026TrueLong, result, 1e-9);
    }

    @Test
    void calculateTrueLongitudeFor24thMay1985() {
        double meanLongitude = 61.54496984353227;
        double equationOfCentre = 1.2398370232704647;

        double result = SolarPosition.calculateTrueLongitude(meanLongitude, equationOfCentre);

        assertEquals(62.78480686680274, result, 1e-9);
    }

    @Test
    void calculateObliquityFor5thMay2026() {
        double result = SolarPosition.calculateObliquity(FifthMay2026JulianCentury);

        assertEquals(23.435865879278616, result);
    }

    @Test
    void calculateObliquityFor24thMay1985() {
        double JD = JulianDate.calculateJulianDay(1985, 5, 24);
        double T = SolarPosition.calculateJulianCentury(JD);

        double result = SolarPosition.calculateObliquity(T);

        assertEquals(23.441190734080863, result, 1e-9);
    }

    @Test
    void calculateOmegaFor5thMay2026() {
        double result = Math.round(SolarPosition.calculateOmega(FifthMay2026JulianCentury));
        assertEquals(Math.round(335.5983740451745), result);
    }

    @Test
    void calculateOmegaFor24thMay1985() {
        double JD = JulianDate.calculateJulianDay(1985, 5, 24);
        double T = SolarPosition.calculateJulianCentury(JD);

        double result = Math.round(SolarPosition.calculateOmega(T));

        assertEquals(Math.round(47.57477420944559), result);
    }

    @Test
    void calculateApparentLongitudeFor5thMay2026() {
        double result = SolarPosition.calculateApparentLongitude(FifthMay2026TrueLong, FifthMay2026Omega);

        assertEquals(44.53247745739062, result, 1e-4);
    }

    @Test
    void calculateApparentLongitudeFor24thMay1985() {
        double JD = JulianDate.calculateJulianDay(1985, 5, 24);
        double T = SolarPosition.calculateJulianCentury(JD);

        double meanLongitude = SolarPosition.calculateMeanLongitude(T);
        double meanAnomaly = SolarPosition.calculateMeanAnomaly(T);
        double eqC = SolarPosition.calculateEquationOfCentre(meanAnomaly);

        double trueLongitude = SolarPosition.calculateTrueLongitude(meanLongitude, eqC);
        double omega = SolarPosition.calculateOmega(T);

        double result = SolarPosition.calculateApparentLongitude(trueLongitude, omega);

        assertEquals(62.77582967523045, result, 1e-4);
    }

    @Test
    void calculateDeclinationFor5thMay2026() {
        double obliquity = SolarPosition.calculateObliquity(FifthMay2026JulianCentury);
        double apparentLongitude = SolarPosition.calculateApparentLongitude(FifthMay2026TrueLong, FifthMay2026Omega);

        double result = SolarPosition.calculateDeclination(obliquity, apparentLongitude);

        assertEquals(16.19623499470435, result, 1e-3);
    }

    @Test
    void calculateDeclinationFor24thMay1985() {
        double JD = JulianDate.calculateJulianDay(1985, 5, 24);
        double T = SolarPosition.calculateJulianCentury(JD);
        double obliquity = SolarPosition.calculateObliquity(T);

        double meanLongitude = SolarPosition.calculateMeanLongitude(T);
        double meanAnomaly = SolarPosition.calculateMeanAnomaly(T);
        double eqC = SolarPosition.calculateEquationOfCentre(meanAnomaly);

        double trueLongitude = SolarPosition.calculateTrueLongitude(meanLongitude, eqC);
        double omega = SolarPosition.calculateOmega(T);

        double apparentLongitude = SolarPosition.calculateApparentLongitude(trueLongitude, omega);

        double result = SolarPosition.calculateDeclination(obliquity, apparentLongitude);

        assertEquals(20.716231256060126, result, 1e-3);
    }

    @Test
    void calculateEqTFor5thMay2026() {
        double result = SolarPosition.calculateEquationOfTime(FifthMay2026JulianCentury, FifthMay2026Obliquity, FifthMay2026MeanAnomaly, FifthMay2026MeanLongitude);

        assertEquals(3.2509624764111673, result,1e-3);
    }

    @Test
    void calculateEqTFor14thMay1985() {
        double JD = JulianDate.calculateJulianDay(1985, 5, 24);
        double T = SolarPosition.calculateJulianCentury(JD);
        double obliquity = SolarPosition.calculateObliquity(T);

        double meanAnomaly = SolarPosition.calculateMeanAnomaly(T);
        double meanLongitude = SolarPosition.calculateMeanLongitude(T);

        double result = SolarPosition.calculateEquationOfTime(T, obliquity, meanAnomaly, meanLongitude);

        assertEquals(3.26056528057528, result, 1e-3);
    }

}
