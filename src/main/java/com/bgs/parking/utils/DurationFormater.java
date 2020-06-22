package com.bgs.parking.utils;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public enum DurationFormater {
    ;
    public static String formatDuration(Duration duration) {
        return String.format(" %sh %sm %ss",
                duration.toHours(),
                duration.toMinutes() - TimeUnit.HOURS.toMinutes(duration.toHours()),
                duration.getSeconds() - TimeUnit.MINUTES.toSeconds(duration.toMinutes()));
    }
}
