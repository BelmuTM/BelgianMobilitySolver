package com.belmu.belgianmobilitysolver.utility;

public class TimeParser {

    /**
     * Checks if the given time string matches the HH:mm:ss format
     * with optional leading zeros using regex pattern matching.
     * <p>
     * Hours: allows [0-23]
     * Minutes/Seconds: allows [0-59]
     *
     * @param formattedTime the specified formatted time string
     */
    public static boolean isValidTimeFormat(String formattedTime) {
        String regex = "^(0?[0-9]|1[0-9]|2[0-3]):([0-5]?[0-9]):([0-5]?[0-9])$";
        return formattedTime.matches(regex);
    }

    /**
     * @param formattedTime expects a time string formatted as HH:mm:ss
     */
    public static long convertFormattedTimeToMilliseconds(String formattedTime) {
        String[] timeValues = formattedTime.split(":");

        long hoursToMilliseconds   = Long.parseLong(timeValues[0]) * 3_600_000;
        long minutesToMilliseconds = Long.parseLong(timeValues[1]) * 60_000;
        long secondsToMilliseconds = Long.parseLong(timeValues[2]) * 1_000;

        return hoursToMilliseconds + minutesToMilliseconds + secondsToMilliseconds;
    }

    public static String convertMillisecondsToFormattedTime(long milliseconds) {
        long totalSeconds = milliseconds / 1000;
        long hours        = totalSeconds / 3600;
        long minutes      = (totalSeconds % 3600) / 60;
        long seconds      = totalSeconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}
