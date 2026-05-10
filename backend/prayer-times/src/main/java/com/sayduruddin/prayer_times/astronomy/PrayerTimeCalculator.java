package com.sayduruddin.prayer_times.astronomy;

public class PrayerTimeCalculator {
    public static double calculateSolarNoon(double EqT, double longitude) {
        // SN = Basetime + (timezone * 60) - (4 * longitude) - (EqT)
        // returns everything in UTC if you omit the timezone consideration
        // (TimezoneOffset * 60 (in mins)) - not sure if we actually need this?
        // (4 * Longitude (needs to have negative and positive values for West and East))
        // - EqT - if in minutes then convert everything else to mins, otherwise EqT/60

        // TODO: Factor in timezone somewhere as this only returns UTC time
        return 720 - ( 4 * longitude ) - EqT;
    }

    public static String formatMinutesToTime(double minutes) {
        long totalMinutes = (Math.round(minutes) + 1440) % 1440;
        long hours = totalMinutes / 60;
        long mins = totalMinutes % 60;
        return String.format("%02d:%02d", hours, mins);
    }
}
