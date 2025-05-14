package com.belmu.belgianmobilitysolver.pathfinder;

import com.belmu.belgianmobilitysolver.model.Stop;

import java.util.*;

public class SpatialHashGrid {

    // Those values are approximate, they vary depending on the curvature of the Earth's surface
    private static final double KM_PER_DEGREE_LATITUDE  = 110.0; // One degree latitude is around 110 kilometers
    private static final double KM_PER_DEGREE_LONGITUDE = 111.1; // One degree longitude is around 111.1 kilometers

    private static final double CELL_SIZE_KM = 1.0;

    private final Map<Long, List<Stop>> grid = new HashMap<>();

    public SpatialHashGrid() {
    }

    private int getCellIndexX(double longitude) {
        return (int) Math.floor(longitude * KM_PER_DEGREE_LONGITUDE / CELL_SIZE_KM);
    }

    private int getCellIndexY(double latitude) {
        return (int) Math.floor(latitude * KM_PER_DEGREE_LATITUDE / CELL_SIZE_KM);
    }

    /**
     * @return the encoded hash key of the cell (X coordinate in upper 32 bits, Y coordinate in lower 32 bits)
     */
    private long hashKey(int cellIndexX, int cellIndexY) {
        return ((long) cellIndexX << 32) | (cellIndexY & 0xffffffffL);
    }

    /**
     * Builds a spatial hash grid by discretizing the geographical coordinates
     * into two-dimensional cells with a fixed size. Each cell can hold multiple transport stops.
     *
     * @param stops the list of stops to store in the grid's cells
     */
    public void build(Collection<Stop> stops) {
        for (Stop stop : stops) {
            int  x   = getCellIndexX(stop.getLongitude());
            int  y   = getCellIndexY(stop.getLatitude());
            long key = hashKey(x, y);

            grid.computeIfAbsent(key, k -> new ArrayList<>()).add(stop);
        }
    }

    /**
     * Iterates grid cells that are neighbours to the specified stop's cell
     * to gather their stops if they are within the acceptable walking distance.
     *
     * @param center the stop at the center of the search neighbourhood
     * @param maxWalkDistance the maximum acceptable distance for nearby stops
     * @return a list of the nearby stops
     */
    public List<Stop> getNearbyStops(Stop center, double maxWalkDistance) {
        List<Stop> nearbyStops = new ArrayList<>();

        int neighbourhoodSize = (int) Math.ceil(maxWalkDistance / CELL_SIZE_KM);

        int centerX = getCellIndexX(center.getLongitude());
        int centerY = getCellIndexY(center.getLatitude());

        for (int dx = -neighbourhoodSize; dx <= neighbourhoodSize; dx++) {
            for (int dy = -neighbourhoodSize; dy <= neighbourhoodSize; dy++) {

                long key = hashKey(centerX + dx, centerY + dy);
                List<Stop> neighbourCell = grid.get(key);

                if (neighbourCell == null) continue;

                // Iterating all the stops in the neighbouring cell
                for (Stop nearby : neighbourCell) {
                    double distance = DistanceFunction.computeDistance(center, nearby);

                    // If the distance from the center stop to the neighbour stop is acceptable and if they're different
                    if (distance <= maxWalkDistance && !center.getStopId().equals(nearby.getStopId())) {
                        nearbyStops.add(nearby);
                    }
                }
            }
        }
        return nearbyStops;
    }
}
