package com.kosovandrey.calorietracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableCaching
@EnableAsync
@SpringBootApplication
public class CalorieTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalorieTrackerApplication.class, args);
    }

}
