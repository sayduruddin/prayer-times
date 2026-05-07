package com.sayduruddin.prayer_times.astronomy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

// all Julian Day calculations have been tested against values given at https://quasar.as.utexas.edu/BillInfo/JulianDateCalc.html
public class JulianDateTest {

    @Test
    void calculateJDFor5thMay2026() {
        double result = JulianDate.calculateJulianDay(2026, 5, 5);
        assertEquals(2461165.5, result);
    }

    @Test
    void calculateJDFor1stJan1954() {
        double result = JulianDate.calculateJulianDay(1954, 1, 17);
        assertEquals(2434759.5, result);
    }
}
