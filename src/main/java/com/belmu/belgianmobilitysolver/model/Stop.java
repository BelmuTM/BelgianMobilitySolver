package com.belmu.belgianmobilitysolver.model;

import java.util.Map;

public class Stop {

    private final String stopId;

    private final String name;

    private final double latitude;
    private final double longitude;

    public Stop(String stopId, String name, String latitude, String longitude) {
        this.stopId    = stopId;
        this.name      = name;
        this.latitude  = Double.parseDouble(latitude);
        this.longitude = Double.parseDouble(longitude);
    }

    public static ReferenceResolver<Stop> resolver(Map<String, Stop> stopsMap) {
        return stop -> stopsMap.put(stop.getStopId(), stop);
    }

    public String getStopId() {
        return stopId;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public static Stop getStopByName(Map<String, Stop> stopsMap, String name) {

        for (Stop stop : stopsMap.values()) {
            if (stop.getName().equalsIgnoreCase(name)) {
                return stop;
            }
        }
        return null;
    }
}
