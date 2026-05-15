package com.sayduruddin.prayer_times.astronomy;

public record SolarPositionData(double julianDay, double julianCentury, double obliquity, double meanLongitude, double omega, double meanAnomaly,
                                double equationOfCentre, double trueLongitude, double apparentLongitude, double declination, double equationOfTime) {}
