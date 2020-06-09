package com.simplemethod.dhx;

import com.simplemethod.dhx.InfluxDBDataModel.ClientModel;
import okhttp3.internal.http2.Http2Connection;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBException;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;
import org.influxdb.impl.InfluxDBMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class InfluxDBConfig {

    private InfluxDB influxDB;

    public InfluxDB getInfluxDB() {
        return influxDB;
    }

    public void init() {
        influxDB = InfluxDBFactory.connect("http://127.0.0.1:8086/");
        Pong response = influxDB.ping();
        if (response.getVersion().equalsIgnoreCase("unknown")) {
            System.out.println("Error pinging server.");
        }
        influxDB.setDatabase("technologie");


       // InfluxDBMapper influxDBMapper = new InfluxDBMapper(influxDB);
       // List<ClientModel> clientModels = influxDBMapper.query(ClientModel.class);
       // for (ClientModel clientModel : clientModels) {
       //     System.out.println(clientModel.getClient_id());
        //}

    }
}
