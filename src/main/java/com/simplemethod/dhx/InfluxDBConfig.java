package com.simplemethod.dhx;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Pong;
import org.springframework.stereotype.Component;


@Component
public class InfluxDBConfig {

    private InfluxDB influxDB;

    public InfluxDB getInfluxDB() {
        return influxDB;
    }

    public void init() {
        influxDB = InfluxDBFactory.connect("http://192.168.1.35:8086/");
        Pong response = influxDB.ping();
        if (response.getVersion().equalsIgnoreCase("unknown")) {
            System.out.println("Error pinging server.");
        }
        influxDB.setDatabase("technologie");


    }
}
