package com.bgs.parking.utils;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public enum FormateurDeDuree {
    INSTANCE;
    public String formatDuration(Duration duration) {
        return String.format(" %sh%sm ",
                duration.toHours(),
                duration.toMinutes() - TimeUnit.HOURS.toMinutes(duration.toHours())
               );
    }
}
