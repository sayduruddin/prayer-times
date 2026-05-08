package com.sayduruddin.prayer_times.astronomy;

// This class will convert a date from the Gregorian calendar into a Julian Day number
public class JulianDate {
    public static double calculateJulianDay(int year, int month, int day) {
        if (day < 1 || day > 31) {
            throw new IllegalArgumentException("Day must be between 1 and 31");
        }
        if (year < 1) {
            throw new IllegalArgumentException("Year must be a positive number");
        }
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }

        int a = ((14 - month) / 12);
        int y = year + 4800 - a;
        int m = month + 12 * a - 3;

        double JDAtNoon = day + (153 * m + 2) / 5 + 365 * y + (y / 4) - (y / 100) + (y / 400) - 32045;

        return JDAtNoon - 0.5;
    }
}
