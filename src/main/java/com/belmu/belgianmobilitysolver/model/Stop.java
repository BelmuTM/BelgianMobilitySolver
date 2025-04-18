package com.belmu.belgianmobilitysolver.model;

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
}
