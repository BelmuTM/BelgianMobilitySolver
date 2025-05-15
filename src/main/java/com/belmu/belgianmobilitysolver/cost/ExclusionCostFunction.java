package com.belmu.belgianmobilitysolver.cost;

import com.belmu.belgianmobilitysolver.model.TransportType;
import com.belmu.belgianmobilitysolver.pathfinder.Node;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ExclusionCostFunction implements CostFunction {

    private final Set<TransportType> excludedTransportTypes;

    /**
     * Cost function for excluded transport types.
     * @param excludedTransportTypes set of transport types forbidden by the user
     */
    public ExclusionCostFunction(final Collection<TransportType> excludedTransportTypes) {
        this.excludedTransportTypes = new HashSet<>(excludedTransportTypes);
    }

    @Override
    public long computeEdgeCost(Node from, Node to, long arrivalTime, boolean isWalking) {
        TransportType transportType = isWalking ? TransportType.WALK : to.route.getTransportType();

        if (excludedTransportTypes.contains(transportType)) {
            return Long.MAX_VALUE;
        }

        // When no exclusion applies, use the time delta as a cost increment
        return arrivalTime - from.time;
    }
}
