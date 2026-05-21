package com.sayduruddin.prayer_times.service;

import com.sayduruddin.prayer_times.astronomy.*;
import net.iakovlev.timeshape.TimeZoneEngine;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
public class PrayerTimesService {

    private final TimeZoneEngine timeZoneEngine;

    public PrayerTimesService(TimeZoneEngine timeZoneEngine) {
        this.timeZoneEngine = timeZoneEngine;
    }

    private int getTimeZoneOffsetInMinutes(double latitude, double longitude, LocalDate date) {
        Optional<ZoneId> zoneId = timeZoneEngine.query(latitude, longitude);

        // falls back to standard UTC time if timezone not found
        if (zoneId.isEmpty()) {
            return 0;
        }

        ZoneOffset offset = date.atStartOfDay(zoneId.get()).getOffset();

        return offset.getTotalSeconds() / 60;
    }

    private double applyHighLatitudeRule(double sunrise, double sunset, double angle, boolean isMorning, HighLatitudeRule rule) {
        return switch (rule) {
            case ANGLE_BASED -> PrayerTimeCalculator.applyAngleBasedRule(sunrise, sunset, angle, isMorning);
            case SEVENTH_OF_NIGHT -> PrayerTimeCalculator.applySeventhOfNight(sunrise, sunset, isMorning);
        };
    }

    public SolarPositionData calculateSolarPositionData(int year, int month, int day) {
        double JD = JulianDate.calculateJulianDay(year, month, day);
        double julianCentury = SolarPosition.calculateJulianCentury(JD);

        // use julianCentury (T) to work out the core variables
        double obliquity = SolarPosition.calculateObliquity(julianCentury);
        double meanLongitude = SolarPosition.calculateMeanLongitude(julianCentury);
        double omega = SolarPosition.calculateOmega(julianCentury);
        double meanAnomaly = SolarPosition.calculateMeanAnomaly(julianCentury);

        // use the core values to calculate intermediate variables
        double equationOfCentre = SolarPosition.calculateEquationOfCentre(meanAnomaly);
        double trueLongitude = SolarPosition.calculateTrueLongitude(meanLongitude, equationOfCentre);
        double apparentLongitude = SolarPosition.calculateApparentLongitude(trueLongitude, omega);
        double declination = SolarPosition.calculateDeclination(obliquity, apparentLongitude);
        double equationOfTime = SolarPosition.calculateEquationOfTime(julianCentury, obliquity, meanAnomaly, meanLongitude);

        return new SolarPositionData(JD, julianCentury, obliquity, meanLongitude, omega, meanAnomaly, equationOfCentre, trueLongitude, apparentLongitude, declination, equationOfTime);
    };

    public PrayerTimesResponse getCalculatedTimes(LocalDate date, double actualLatitude, double actualLongitude, int shadowRatio, HighLatitudeRule highLatitudeRule, double fajrAngle, double ishaAngle) {
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        SolarPositionData solarData = calculateSolarPositionData(year, month, day);

        double solarNoon = PrayerTimeCalculator.calculateSolarNoon(solarData.equationOfTime(), actualLongitude);

        double sunriseHourAngle = PrayerTimeCalculator.calculateHourAngle(-0.833, actualLatitude, solarData.declination());
        double sunrise = solarNoon - (sunriseHourAngle * 4);
        double sunset = solarNoon + (sunriseHourAngle * 4);

        double fajrHourAngle = PrayerTimeCalculator.calculateHourAngle(-fajrAngle, actualLatitude, solarData.declination());
        boolean fajrHighLatitude = Double.isNaN(fajrHourAngle) || fajrHourAngle > 150;
        double fajrTime = fajrHighLatitude ? applyHighLatitudeRule(sunrise, sunset, fajrAngle, true, highLatitudeRule) : solarNoon - ( fajrHourAngle * 4);

        double asrAngle = PrayerTimeCalculator.calculateAsrAngle(actualLatitude, solarData.declination(), shadowRatio);
        double asrHourAngle = PrayerTimeCalculator.calculateHourAngle(asrAngle, actualLatitude, solarData.declination());
        double asrTime = solarNoon + ( asrHourAngle * 4);

        double ishaHourAngle = PrayerTimeCalculator.calculateHourAngle(-ishaAngle, actualLatitude, solarData.declination());
        boolean ishaHighLatitude = Double.isNaN(ishaHourAngle) || ishaHourAngle > 150;
        double ishaTime = ishaHighLatitude ? applyHighLatitudeRule(sunrise, sunset, ishaAngle, false, highLatitudeRule) : solarNoon + ( ishaHourAngle * 4);;

        System.out.println("Isha H: " + ishaHourAngle);
        System.out.println("Isha raw UTC minutes: " + ishaTime);
        System.out.println("Is high latitude triggered: " + Double.isNaN(ishaHourAngle));

        // depending on the timezone the location is in, will need to add this to the actual UTC calculated times
        int timeZoneOffsetInMinutes = getTimeZoneOffsetInMinutes(actualLatitude, actualLongitude, date);

        // TODO: research other high latitude rules and implement them

        double prayerTimeOffset = 2.0;
        String fajrClockTime = PrayerTimeCalculator.formatMinutesToTime(fajrTime + timeZoneOffsetInMinutes);
        String sunriseClockTime = PrayerTimeCalculator.formatMinutesToTime(sunrise + timeZoneOffsetInMinutes);
        String dhuhrClockTime = PrayerTimeCalculator.formatMinutesToTime(solarNoon + timeZoneOffsetInMinutes + prayerTimeOffset);
        String maghribClockTime = PrayerTimeCalculator.formatMinutesToTime(sunset + timeZoneOffsetInMinutes + prayerTimeOffset);
        String asrClockTime = PrayerTimeCalculator.formatMinutesToTime(asrTime + timeZoneOffsetInMinutes + prayerTimeOffset);
        String ishaClockTime = PrayerTimeCalculator.formatMinutesToTime(ishaTime + timeZoneOffsetInMinutes + prayerTimeOffset);

        return new PrayerTimesResponse(fajrClockTime, sunriseClockTime, dhuhrClockTime, asrClockTime, maghribClockTime, ishaClockTime);
    }
}
