package com.sayduruddin.prayer_times.service;

import lombok.Getter;

public class PrayerTimesResponse {
    @Getter
    private String fajr;
    @Getter
    private String sunrise;
    @Getter
    private String dhuhr;
    @Getter
    private String asr;
    @Getter
    private String maghrib;
    @Getter
    private String isha;

    public PrayerTimesResponse(String fajr, String sunrise, String dhuhr, String asr, String maghrib, String isha) {
        this.fajr = fajr;
        this.sunrise = sunrise;
        this.dhuhr = dhuhr;
        this.asr = asr;
        this.maghrib = maghrib;
        this.isha = isha;
    }

}
