package com.simplemethod.dhx.InfluxDBDAO;

import com.simplemethod.dhx.InfluxDBDataModel.ClientModel;
import com.simplemethod.dhx.InfluxDBDataModel.EmployeeModel;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.impl.InfluxDBMapper;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.influxdb.querybuilder.BuiltQuery.QueryBuilder.*;

@Controller
public class InfluxEmployeeDAO {
    private InfluxDB influxDB;

    public void setInfluxDB(InfluxDB influxDB) {
        this.influxDB = influxDB;
    }

    /**
     * Pobiera wszystkich pracowników.
     * @return Tablica z obiektami pracowników.
     */
    public List<EmployeeModel> findAll() {
        InfluxDBMapper influxDBMapper = new InfluxDBMapper(influxDB);
        Query query = select().from("technologie", "kierowcy").orderBy(asc());
        return influxDBMapper.query(query, EmployeeModel.class);
    }

    /**
     * Pobiera pracowników po numerze telefonu.
     * @param phoneNumber Numer telefonu pracownika.
     * @return Lista obiektów z pracownikami.
     */
    public List<EmployeeModel> findAllByPhoneNumber(Integer phoneNumber) {
        InfluxDBMapper influxDBMapper = new InfluxDBMapper(influxDB);
        Query query = select().from("technologie", "kierowcy").where(eq("telefon", phoneNumber)).orderBy(asc());
        return influxDBMapper.query(query, EmployeeModel.class);
    }

    /**
     *  Pobiera pracowników według numeru telefonu i nazwiska.
     * @param telefon Numer telefonu.
     * @param surname Nazwisko.
     * @return Obiekt z danymi pracownika.
     */
    public List<EmployeeModel> findByPhoneNumberAndName(Integer telefon, String surname) {
        InfluxDBMapper influxDBMapper = new InfluxDBMapper(influxDB);
        Query query = select().from("technologie", "kierowcy").where(eq("telefon", telefon)).where(eq("nazwisko", surname)).orderBy(asc());
        return influxDBMapper.query(query, EmployeeModel.class);
    }

    /**
     * Pobiera pracowników po identyfikatorze.
     * @param instant Identyfikator pracownika..
     * @return Lista obiektów z pracownikami.
     */
    public List<EmployeeModel> findByUUID(Long instant) {
        InfluxDBMapper influxDBMapper = new InfluxDBMapper(influxDB);
        Query query = select().from("technologie", "kierowcy").where(eq("time", instant)).orderBy(asc());
        return influxDBMapper.query(query, EmployeeModel.class);
    }

    /**
     * Aktualizuje nazwisko pracownika.
     * @param nazwisko Nowe nazwisko pracownika.
     */
    public void setSurnameByUUID(String nazwisko, Long instant) {
        //InfluxDB does not support UPDATE statements.
        InfluxDBMapper influxDBMapper = new InfluxDBMapper(influxDB);
        Query query = select().from("technologie", "kierowcy").where(eq("time", instant)).orderBy(asc());
        List<ClientModel> clientModels = influxDBMapper.query(query, ClientModel.class);
        if (!clientModels.isEmpty()) {
            clientModels.get(0).setNazwisko(nazwisko);
            influxDB.query(new Query("DELETE FROM kierowcy WHERE time=" + instant));
            Point point = Point.measurement("kierowcy").time(clientModels.get(0).getClient_id().toEpochMilli(), TimeUnit.MILLISECONDS)
                    .addField("imie", clientModels.get(0).getName())
                    .addField("nazwisko", nazwisko)
                    .addField("miasto", clientModels.get(0).getMiasto())
                    .addField("ulica", clientModels.get(0).getUlica())
                    .addField("numer_domu", clientModels.get(0).getNumer_domu())
                    .addField("telefon", clientModels.get(0).getTelefon()).build();
            influxDB.write(point);
        }
    }

    /**
     * Usuwa pracownika.
     * @param instant Identyfiaktor pracownika.
     */
    public void removeByUUID(Long instant) {
        influxDB.query(new Query("DELETE FROM klienci WHERE time=" + instant));
    }

    /**
     * Dodaje nowego pracownika do bazy danych.
     * @param imie Imie pracownika.
     * @param nazwisko Nazwisko pracownika.
     * @param telefon Telefon pracownika.
     */
    public void saveEmployee(String imie, String nazwisko, Integer telefon) {
        Point point = Point.measurement("kierowcy").time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("imie", imie)
                .addField("nazwisko", nazwisko)
                .addField("telefon", telefon)
                .build();
        influxDB.write(point);
    }
}
