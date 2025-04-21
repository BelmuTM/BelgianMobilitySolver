package com.belmu.belgianmobilitysolver.model;

import java.util.Map;

public class Trip {

    private final String tripId;
    private final String routeId;

    private Route route;

    public Trip(String tripId, String routeId) {
        this.tripId  = tripId;
        this.routeId = routeId;
    }

    public static ReferenceResolver<Trip> resolver(Map<String, Route> routesMap, Map<String, Trip> tripsMap) {
        return trip -> {
            trip.setRoute(Route.getRouteFromId(routesMap, trip.getRouteId()));
            tripsMap.put(trip.getTripId(), trip);
        };
    }

    public String getTripId() {
        return tripId;
    }

    public String getRouteId() {
        return routeId;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
