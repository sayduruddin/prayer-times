package com.sayduruddin.prayer_times.service;

import com.sayduruddin.prayer_times.astronomy.JulianDate;
import com.sayduruddin.prayer_times.astronomy.PrayerTimeCalculator;
import com.sayduruddin.prayer_times.astronomy.SolarPosition;
import com.sayduruddin.prayer_times.astronomy.SolarPositionData;
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
        System.out.println("Zone offset: " + offset + " offset in mins: " + offset.getTotalSeconds() / 60 + " offset in secs: " + offset.getTotalSeconds());

        return offset.getTotalSeconds() / 60;
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

    // service methods called by controller
    public PrayerTimesResponse getCalculatedTimes(LocalDate date, double actualLatitude, double actualLongitude, int shadowRatio) {
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        SolarPositionData solarData = calculateSolarPositionData(year, month, day);
//        System.out.println("Values passed in " + actualLatitude + " " + actualLongitude + " " + shadowRatio);
//        System.out.println("Solar data T: " + solarData.julianCentury() + " solar data eqt: " + solarData.equationOfTime());

        double solarNoon = PrayerTimeCalculator.calculateSolarNoon(solarData.equationOfTime(), actualLongitude);
//        System.out.println("Got solar noon as: " + PrayerTimeCalculator.formatMinutesToTime(solarNoon));

        // calculate sunrise and sunset first
        double sunriseHourAngle = PrayerTimeCalculator.calculateHourAngle(-0.833, actualLatitude, solarData.declination());
        double sunrise = solarNoon - (sunriseHourAngle * 4);
        double sunset = solarNoon + (sunriseHourAngle * 4);
//        System.out.println("sunrise hour angle: " + sunriseHourAngle + " sunrise: " + PrayerTimeCalculator.formatMinutesToTime(sunrise) + " sunset: " + PrayerTimeCalculator.formatMinutesToTime(sunset));

        double fajrHourAngle = PrayerTimeCalculator.calculateHourAngle(-18, actualLatitude, solarData.declination());
        double fajrTime;
        if (Double.isNaN(fajrHourAngle)) {
//            System.out.println("fajr hour angle is NaN, applying angle based rule");
            fajrTime = PrayerTimeCalculator.applyAngleBasedRule(sunrise, sunset, 18.0, true);
        } else {
            fajrTime = solarNoon - ( fajrHourAngle * 4); // multiply by 4 to convert degrees of rotation into minutes of time
        }

//        System.out.println("Fajr time in minutes: " + fajrTime);

        // depending on the timezone the location is in, will need to add this to the actual UTC calculated times
        int offsetMinutes = getTimeZoneOffsetInMinutes(actualLatitude, actualLongitude, date);

        System.out.println("Declination: " + solarData.declination());
        System.out.println("Solar noon: " + solarNoon);
        System.out.println("Sunrise H: " + sunriseHourAngle);
        System.out.println("Sunrise UTC minutes: " + sunrise);
        System.out.println("Sunset UTC minutes: " + sunset);
        System.out.println("Night duration: " + (sunrise + (1440 - sunset)));
        System.out.println("Fajr portion: " + (18.0 / 60.0));
        System.out.println("Fajr offset: " + ((18.0 / 60.0) * (sunrise + (1440 - sunset))));
        System.out.println("Fajr raw UTC: " + fajrTime);

        String fajrUTC = PrayerTimeCalculator.formatMinutesToTime(fajrTime);
        String sunriseUTC = PrayerTimeCalculator.formatMinutesToTime(sunrise);
        String maghribUTC = PrayerTimeCalculator.formatMinutesToTime(sunset);

        return new PrayerTimesResponse(fajrUTC, sunriseUTC, "00:00", "00:00", maghribUTC, "00:00");
    }
}
