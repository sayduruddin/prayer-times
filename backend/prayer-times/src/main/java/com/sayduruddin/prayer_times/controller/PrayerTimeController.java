package com.sayduruddin.prayer_times.controller;

import com.sayduruddin.prayer_times.service.PrayerTimesResponse;
import com.sayduruddin.prayer_times.service.PrayerTimesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;


@RestController
@RequestMapping("/prayer-times")
public class PrayerTimeController {

    private final PrayerTimesService prayerTimesService;

    @Autowired
    public PrayerTimeController(PrayerTimesService prayerTimesService) {
        this.prayerTimesService = prayerTimesService;
    }

    @GetMapping("/today")
    public PrayerTimesResponse getPrayerTimesToday(@RequestParam double latitude,
                                                   @RequestParam double longitude,
                                                   @RequestParam int shadowRatio
                                                   ) {
        return prayerTimesService.getCalculatedTimes(LocalDate.now(), latitude, longitude, shadowRatio);
    }
}
