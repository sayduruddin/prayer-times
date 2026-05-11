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

    public static double calculateHourAngle(double angle, double latitude, double declination) {
        double angleRad = Math.toRadians(angle);
        double latRad = Math.toRadians(latitude);
        double declinationRad = Math.toRadians(declination);

        double numerator = Math.sin(angleRad) - (Math.sin(latRad) * Math.sin(declinationRad));
        double denominator = Math.cos(latRad) * Math.cos(declinationRad);

        double cosH = numerator / denominator;

        // need to add a guard for if cosH is greater than 1 or less than -1, see below multi line comment
        // TODO: implement the guard and use the rules for when in a high latitude location.
        return Math.toDegrees(Math.acos(cosH));
    }
    /*
        * if Math.acos(cosH) only accepts values between -1 and 1, it will return NaN.
        * In Keighley (53.9°N) in late June:

            The sun sets around 9:30pm
            It only dips to about 13° below the horizon at its lowest point
            If your Isha angle is -15°, the sun never reaches it — it starts rising again before getting that deep
            cosH comes out as -1.something — outside the valid range
        *
        *   the sun never reaches below 18 or 15 below the horizon in the summer, which is why
        *   we never get pitch black nights, we stay in a gray twilight.
        *   when the code detects the H is not a number which will
        *   mean we need to do one of 3 methods to determine the isha and fajr prayer times.
        *
        *   1. the 1/7th rule: we divide the total time between Sunset and Sunrise into 7 equal parts
        *       Isha = Sunset + (1/7th of the night)
        *       Fajr = Sunrise - (1/7th of the night)
        *
        *   2. The Nearest Latitude: we calculate the prayer times as if we were at a lower altitude
        *       where the sun does reach -18 or -15, these are usually 48 and 45 N for latitude.
        *       we then apply those times
        *
        *   3. Nearest Day: we find the last day the Sun did actually hit -18 such as early May, and
        *       we just freeze the Fajr and Isha times at that value until the Sun starts hitting
        *       those angles again.
        *
        *

        * */

    public static String formatMinutesToTime(double minutes) {
        long totalMinutes = (Math.round(minutes) + 1440) % 1440;
        long hours = totalMinutes / 60;
        long mins = totalMinutes % 60;
        return String.format("%02d:%02d", hours, mins);
    }
}
