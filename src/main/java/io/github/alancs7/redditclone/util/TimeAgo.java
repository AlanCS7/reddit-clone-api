package io.github.alancs7.redditclone.util;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class TimeAgo {

    public static String using(long epochMillis) {
        Instant instant = Instant.ofEpochMilli(epochMillis);
        OffsetDateTime pastTime = OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
        Duration duration = Duration.between(pastTime, OffsetDateTime.now(ZoneOffset.UTC));

        long seconds = duration.getSeconds();
        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();

        if (seconds < 60) {
            return "just now";
        } else if (seconds < 120) {
            return "a minute ago";
        } else if (minutes < 60) {
            return minutes + " minutes ago";
        } else if (hours < 24) {
            return hours + " hours ago";
        } else if (days < 30) {
            return days + " days ago";
        } else if (days < 365) {
            long months = days / 30;
            return months + " months ago";
        } else {
            long years = days / 365;
            long remainingMonths = (days % 365) / 30;
            if (remainingMonths > 0) {
                return years + " years and " + remainingMonths + " months ago";
            } else {
                return years + " years ago";
            }
        }
    }
}
