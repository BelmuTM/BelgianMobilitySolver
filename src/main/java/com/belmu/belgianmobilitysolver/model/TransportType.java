package com.belmu.belgianmobilitysolver.model;

public enum TransportType {

    BUS  ,
    METRO,
    TRAIN,
    TRAM ,
    WALK ;

    public static TransportType fromString(String value) {
        try {
            return TransportType.valueOf(value);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Invalid TransportType: " + value);
        }
        return null;
    }
}
