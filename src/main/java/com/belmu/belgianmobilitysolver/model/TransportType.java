package com.belmu.belgianmobilitysolver.model;

public enum TransportType {

    TRAIN,
    METRO,
    BUS,
    TRAM;

    public static TransportType fromString(String value) {
        try {
            return TransportType.valueOf(value);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Invalid TransportType: " + value);
        }
        return null;
    }
}
