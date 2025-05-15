package com.belmu.belgianmobilitysolver.cost;

import com.belmu.belgianmobilitysolver.pathfinder.Node;

public interface CostFunction {

    /**
     * Called to compute the cost for each candidate node.
     *
     * @param from          current node
     * @param to            candidate node
     * @param arrivalTime   estimated time of arrival to candidate node
     * @param isWalking     whether the next edge will be walked or not
     * @return              either
     *                      - a positive value
     *                      - Long.MAX_VALUE for forbidden candidate nodes
     */
    long computeEdgeCost(Node from, Node to, long arrivalTime, boolean isWalking);
}
