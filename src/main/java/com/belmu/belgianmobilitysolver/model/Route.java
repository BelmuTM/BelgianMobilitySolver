package com.belmu.belgianmobilitysolver.model;

import java.util.Map;

public class Route {

    private final String routeId;

    private final String shortName;
    private final String longName;

    private final TransportType transportType;

    public Route(String routeId, String shortName, String longName, String transportType) {
        this.routeId   = routeId;

        this.shortName = shortName;
        this.longName  = longName;

        this.transportType = TransportType.fromString(transportType);
    }

    public static ReferenceResolver<Route> resolver(Map<String, Route> routesMap) {
        return route -> routesMap.put(route.getRouteId(), route);
    }

    public String getRouteId() {
        return routeId;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLongName() {
        return longName;
    }

    public TransportType getTransportType() {
        return transportType;
    }
}
