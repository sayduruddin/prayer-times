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

        // need to add a guard for if cosH is greater than 1 or less than -1 but not sure what this means for UK
        /*
        * In Keighley (53.9°N) in late June:

            The sun sets around 9:30pm
            It only dips to about 13° below the horizon at its lowest point
            If your Isha angle is -15°, the sun never reaches it — it starts rising again before getting that deep
            cosH comes out as -1.something — outside the valid range

            In Mecca (21.4°N) this never happens — the sun always dips well below the horizon even in summer.

        * */

        return Math.toDegrees(Math.acos(cosH));
    }

    public static String formatMinutesToTime(double minutes) {
        long totalMinutes = (Math.round(minutes) + 1440) % 1440;
        long hours = totalMinutes / 60;
        long mins = totalMinutes % 60;
        return String.format("%02d:%02d", hours, mins);
    }
}
