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
        if (cosH > 1 || cosH < -1) {
            return Double.NaN;
        }

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
        *   mean we need to do one of the  methods to determine the isha and fajr prayer times.
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
        *   4. we use a proportion of the night duration based on the angle
        *       nightDuration = sunset - sunrise
        *       fajrPortion = fajrAngle / 60
        *       fajr = sunrise - (fajrPortion * nightDuration)
        *
        *   will need to expose the above options as a user choice so they can decide, Wifaqul Ulama use 1/7th rule
        *   UK Islamic Mission use Angle based rule
        *   Hikmah Way use 1/7th rule.
        *
        *

        * */

    public static double applyAngleBasedRule(double sunrise, double sunset, double angle, boolean isMorning) {
        // night spans across midnight, goes from sunset to sunrise the next day.
        double nightDuration = sunrise + (1440 - sunset);

        // this converts the prayer angle into a fraction of the night to use as the offset
        // dividing by 60 gives us the proportion of the night to use
        // for Fajr, at an angle of 18 it would be 18 / 60 = 0.3, so fajr is 30% of the night duration before sunrise
        // isha will be 15 / 60 = 0.25, so 25% of the night duration after sunset
        double portion = Math.abs(angle) / 60.0;

        double offset = portion * nightDuration;

        return (isMorning) ? sunrise - offset : sunrise + offset;
    }

    public static double calculateAsrAngle(double latitude, double declination, int shadowRatio) {
        // asrAngle = -arcTan(1 / shadowRatio + tan(|latitude - declination|))
        double absoluteDifference = Math.abs(latitude - declination);
        double differenceRad = Math.toRadians(absoluteDifference);

        double calculation = 1.0 / (shadowRatio + Math.tan(differenceRad));

        return Math.toDegrees((Math.atan(calculation)));
    }

    public static String formatMinutesToTime(double minutes) {
        long totalMinutes = (Math.round(minutes) + 1440) % 1440;
        long hours = totalMinutes / 60;
        long mins = totalMinutes % 60;
        return String.format("%02d:%02d", hours, mins);
    }
}
