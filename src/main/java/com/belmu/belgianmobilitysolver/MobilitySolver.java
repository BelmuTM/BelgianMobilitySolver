package com.belmu.belgianmobilitysolver;

import com.belmu.belgianmobilitysolver.model.StopTime;
import com.belmu.belgianmobilitysolver.parser.DataParser;

import java.util.List;

public class MobilitySolver {

    public static void main(String[] args) {

        DataParser parser = new DataParser();

        List<StopTime> times = parser.stopTimesMap.get("DELIJN-0");

        for (StopTime stopTime : times) {
            System.out.println(stopTime.getTripId() + " " + stopTime.getStopId());
        }
    }
}
