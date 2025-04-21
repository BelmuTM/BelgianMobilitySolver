package com.belmu.belgianmobilitysolver.cost;

import com.belmu.belgianmobilitysolver.model.Stop;

public class DistanceFunction {

    // https://en.wikipedia.org/wiki/Haversine_formula
    public double computeCost(Stop from, Stop to) {
        final double radius = 6372.8; // Earth's radius in kilometers

        double latitudeFrom = Math.toRadians(from.getLatitude());
        double latitudeTo   = Math.toRadians(to  .getLatitude());

        double longitudeDistance = Math.toRadians(to.getLongitude() - from.getLongitude());
        double latitudeDistance  = latitudeTo - latitudeFrom;

        double haversineTheta = Math.pow(Math.sin(latitudeDistance * 0.5), 2)
                + Math.pow(Math.sin(longitudeDistance * 0.5), 2) * Math.cos(latitudeFrom) * Math.cos(latitudeTo);

        return 2.0 * radius * Math.asin(Math.sqrt(haversineTheta));
    }
}
