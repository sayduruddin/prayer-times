package com.sayduruddin.prayer_times.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/prayer-times")
public class PrayerTimeController {

    @GetMapping("/today")
    public String getPrayerTimesToday() {
        return "Implement Prayer Times Service";
    }
}
