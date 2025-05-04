package com.belmu.belgianmobilitysolver;

import com.belmu.belgianmobilitysolver.parser.DataParser;
import com.belmu.belgianmobilitysolver.pathfinder.Edge;
import com.belmu.belgianmobilitysolver.pathfinder.Pathfinder;

import java.time.LocalTime;
import java.util.List;

public class MobilitySolver {

    public static void main(String[] args) {

        DataParser parser     = new DataParser();
        Pathfinder pathfinder = new Pathfinder(parser);

        List<Edge> path = pathfinder.findPath("DELIJN-509720", "SNCB-S8865003", LocalTime.of(10, 30));

        for (Edge edge : path) {
            System.out.printf(
                    "%s → %s : depart %s, arrive %s%n",
                    edge.getFrom().stopId(),
                    edge.getTo().stopId(),
                    edge.getFrom().time(),
                    edge.getTo().time()
            );
        }
    }
}
