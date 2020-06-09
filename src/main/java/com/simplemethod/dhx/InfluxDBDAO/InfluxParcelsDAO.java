package com.simplemethod.dhx.InfluxDBDAO;
import com.simplemethod.dhx.InfluxDBDataModel.ParcelsModel;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.impl.InfluxDBMapper;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.influxdb.querybuilder.BuiltQuery.QueryBuilder.*;

@Controller
public class InfluxParcelsDAO {
    private InfluxDB influxDB;

    public void setInfluxDB(InfluxDB influxDB) {
        this.influxDB = influxDB;
    }

    /**
     *  Pobieranie wszystkich paczek.
     * @return Lista z obiektami paczek.
     */
    public List<ParcelsModel> findAll() {
        InfluxDBMapper influxDBMapper = new InfluxDBMapper(influxDB);
        Query query = select().from("technologie", "paczki").orderBy(asc());
        return influxDBMapper.query(query, ParcelsModel.class);
    }

    /**
     * Pobieranie paczki według identyfikatora packzi.
     * @param instant Identyfikator paczki.
     * @return Obiekt paczki.
     */
    public List<ParcelsModel> findByUUID(Long instant) {
        InfluxDBMapper influxDBMapper = new InfluxDBMapper(influxDB);
        Query query = select().from("technologie", "paczki").where(eq("time", instant)).orderBy(asc());
        return influxDBMapper.query(query, ParcelsModel.class);
    }

    /**
     * Pobieranie packzi po identyfikatorze klienta.
     * @param instant Identyfikator klienta.
     * @return Obiekt paczki.
     */
    public List<ParcelsModel> findByClient(Long instant) {
        InfluxDBMapper influxDBMapper = new InfluxDBMapper(influxDB);
        Query query = select().from("technologie", "paczki").where(eq("klient_id", instant)).orderBy(asc());
        return influxDBMapper.query(query, ParcelsModel.class);
    }

    /**
     * Pobieranie packzi po identyfikatorze pracownika.
     * @param instant Identyfikator pracownika.
     * @return Obiekt paczki.
     */
    public List<ParcelsModel> findByEmployee(Long instant) {
        InfluxDBMapper influxDBMapper = new InfluxDBMapper(influxDB);
        Query query = select().from("technologie", "paczki").where(eq("kierowcy_id", instant)).orderBy(asc());
        return influxDBMapper.query(query, ParcelsModel.class);
    }

    /**
     * Pobieranie paczki według miasta.
     * @param city Wyszukiwanie paczek według miasta.
     * @return Lista obiektów paczek.
     */
    public List<ParcelsModel> findByCity(String city) {
        InfluxDBMapper influxDBMapper = new InfluxDBMapper(influxDB);
        Query query = select().from("technologie", "paczki").where(eq("miasto", city)).orderBy(asc());
        return influxDBMapper.query(query, ParcelsModel.class);
    }

    /**
     *  Aktualizacja nymeru trackingu paczki.
     * @param identyfikator_rejonu_paczki Nowy tracking.
     * @param instant  Identyfikator paczki.
     */
    public void setTrackingIDByID(Integer identyfikator_rejonu_paczki, Long instant) {
        //InfluxDB does not support UPDATE statements.
        InfluxDBMapper influxDBMapper = new InfluxDBMapper(influxDB);
        Query query = select().from("technologie", "paczki").where(eq("time", instant)).orderBy(asc());
        List<ParcelsModel> parcelsModels = influxDBMapper.query(query, ParcelsModel.class);
        if (!parcelsModels.isEmpty()) {
            parcelsModels.get(0).setIdentyfikator_rejonu_paczki(identyfikator_rejonu_paczki);
            influxDB.query(new Query("DELETE FROM klienci WHERE time=" + instant));
            Point point = Point.measurement("paczki").time(parcelsModels.get(0).getClient_id().toEpochMilli(), TimeUnit.MILLISECONDS)
                    .addField("klient_id", parcelsModels.get(0).getClient_id().toEpochMilli())
                    .addField("kierowcy_id", parcelsModels.get(0).getEmployee_id().toEpochMilli())
                    .addField("miasto",parcelsModels.get(0).getMiasto())
                    .addField("ulica",parcelsModels.get(0).getUlica())
                    .addField("numer_domu",parcelsModels.get(0).getNumer_domu())
                    .addField("ilosc_paczek",parcelsModels.get(0).getIlosc_paczek())
                    .addField("pobranie",parcelsModels.get(0).getPobranie())
                    .addField("identyfikator_rejonu_paczki",parcelsModels.get(0).getIdentyfikator_rejonu_paczki()).build();
            influxDB.write(point);
        }
    }
    /**
     * Usuwanie paczki.
     * @param instant Identyfikator paczki.
     */
    public void removeByUUID(Long instant) {
        influxDB.query(new Query("DELETE FROM paczki WHERE time=" + instant));
    }

    /**
     * Dodaje nową paczkę do bazy danych.
     * @param client_id Identyfikator klienta.
     * @param employee_id Identyfikator pracownika.
     * @param miasto Miasto docelowe.
     * @param ulica Ulica docelowa.
     * @param numer_domu Numer domu docelowy.
     * @param ilosc_paczek Ilość paczek do dostarczenia na podany adres.
     * @param pobranie Kwota pobrania paczki.
     * @param identyfikator_rejonu_paczki Identyfikator paczki w sortownii.
     */
    public void saveParcels(Long client_id, Long employee_id, String miasto, String ulica, Integer numer_domu, Integer ilosc_paczek, float pobranie, Integer identyfikator_rejonu_paczki) {
        Point point = Point.measurement("paczki").time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("klient_id", client_id)
                .addField("kierowcy_id", employee_id)
                .addField("miasto", miasto)
                .addField("ulica", ulica)
                .addField("numer_domu", numer_domu)
                .addField("ilosc_paczek",ilosc_paczek)
                .addField("pobranie",pobranie)
                .addField("identyfikator_rejonu_paczki",identyfikator_rejonu_paczki)
                .build();
        influxDB.write(point);
    }
}
