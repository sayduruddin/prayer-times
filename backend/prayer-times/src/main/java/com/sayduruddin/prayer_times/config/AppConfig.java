package com.sayduruddin.prayer_times.config;

import net.iakovlev.timeshape.TimeZoneEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public TimeZoneEngine timeZoneEngine() {
        return TimeZoneEngine.initialize();
    }
}
