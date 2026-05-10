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

}
