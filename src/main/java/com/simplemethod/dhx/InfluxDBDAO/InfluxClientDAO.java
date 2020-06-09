package com.simplemethod.dhx.InfluxDBDAO;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.simplemethod.dhx.InfluxDBDataModel.ClientModel;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBMapper;
import org.springframework.stereotype.Controller;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.impl.InfluxDBMapper;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.*;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.now;
import static org.influxdb.querybuilder.BuiltQuery.QueryBuilder.asc;
import static org.influxdb.querybuilder.BuiltQuery.QueryBuilder.desc;
import static org.influxdb.querybuilder.BuiltQuery.QueryBuilder.eq;
import static org.influxdb.querybuilder.BuiltQuery.QueryBuilder.select;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Controller
public class InfluxClientDAO {
    private InfluxDB influxDB;

    public void setInfluxDB(InfluxDB influxDB) {
        this.influxDB = influxDB;
    }

    public List<ClientModel> findAll() {
        InfluxDBMapper influxDBMapper = new InfluxDBMapper(influxDB);
        Query query = select().from("technologie", "klienci").orderBy(asc());
        return influxDBMapper.query(query, ClientModel.class);
    }

    public List<ClientModel> findAllByPhoneNumber(Integer phoneNumber) {
        InfluxDBMapper influxDBMapper = new InfluxDBMapper(influxDB);
        Query query = select().from("technologie", "klienci").where(eq("telefon", phoneNumber)).orderBy(asc());
        return influxDBMapper.query(query, ClientModel.class);
    }

    public List<ClientModel> findByNameAndSurname(String name, String surname) {
        InfluxDBMapper influxDBMapper = new InfluxDBMapper(influxDB);
        Query query = select().from("technologie", "klienci").where(eq("imie", name)).where(eq("nazwisko", surname)).orderBy(asc());
        return influxDBMapper.query(query, ClientModel.class);
    }

    public List<ClientModel> findByUUID(Long instant) {
        InfluxDBMapper influxDBMapper = new InfluxDBMapper(influxDB);
        Query query = select().from("technologie", "klienci").where(eq("time", instant)).orderBy(asc());
        return influxDBMapper.query(query, ClientModel.class);
    }

    public void setNameByUUID(String imie, Long instant) {
        //InfluxDB does not support UPDATE statements.
        InfluxDBMapper influxDBMapper = new InfluxDBMapper(influxDB);
        Query query = select().from("technologie", "klienci").where(eq("time", instant)).orderBy(asc());
        List<ClientModel> clientModels = influxDBMapper.query(query, ClientModel.class);
        if (!clientModels.isEmpty()) {
            clientModels.get(0).setName(imie);
            influxDB.query(new Query("DELETE FROM klienci WHERE time=" + instant));
            Point point = Point.measurement("klienci").time(clientModels.get(0).getClient_id().toEpochMilli(), TimeUnit.MILLISECONDS)
                    .addField("imie", imie)
                    .addField("nazwisko", clientModels.get(0).getNazwisko())
                    .addField("miasto", clientModels.get(0).getMiasto())
                    .addField("ulica", clientModels.get(0).getUlica())
                    .addField("numer_domu", clientModels.get(0).getNumer_domu())
                    .addField("telefon", clientModels.get(0).getTelefon()).build();
            influxDB.write(point);
        }
    }

    public void removeByUUID(Long instant) {
        influxDB.query(new Query("DELETE FROM klienci WHERE time=" + instant));
    }

    public void saveCustomer(String imie, String nazwisko, String miasto, String ulica, Integer numerDomu, Integer telefon) {
        Point point = Point.measurement("klienci").time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("imie", imie).addField("nazwisko", nazwisko)
                .addField("miasto", miasto)
                .addField("ulica", ulica)
                .addField("numer_domu", numerDomu)
                .addField("telefon", telefon).build();
        influxDB.write(point);
    }
}
