package com.simplemethod.dhx.cassandraDAO;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.simplemethod.dhx.CassandraDataModel.ParcelsModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.UUID;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.*;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

@Component
public class ParcelsDAO {
    private CqlSession session;


    public void setSession(CqlSession session) {
        this.session = session;
    }

    /**
     *  Pobieranie wszystkich paczek.
     * @return Lista z obiektami paczek.
     */
    public ArrayList<ParcelsModel> findAll() {
        ArrayList<ParcelsModel> arrayList = new ArrayList<>();
        Select selectUser = selectFrom("paczki").all();
        ResultSet rs = session.execute(selectUser.build());
        rs.forEach(
                row -> arrayList.add(new ParcelsModel(row.getUuid("paczka_id"), row.getUuid("klient_id"), row.getUuid("kierowcy_id"), row.getString("miasto"), row.getString("ulica"), row.getInt("numer_domu"), row.getInt("ilosc_paczek"), row.getFloat("pobranie"), row.getInt("identyfikator_rejonu_paczki")))
        );
        return arrayList;
    }

    /**
     * Pobieranie paczki według identyfikatora packzi.
     * @param uuid Identyfikator paczki.
     * @return Obiekt paczki.
     */
    public ParcelsModel findByUUID(UUID uuid) {
        ArrayList<ParcelsModel> arrayList = new ArrayList<>();
        Select selectUser = selectFrom("paczki").all().whereColumn("paczka_id").isEqualTo(literal(uuid)).allowFiltering();
        ResultSet rs = session.execute(selectUser.build());
        rs.forEach(
                row -> arrayList.add(new ParcelsModel(row.getUuid("paczka_id"), row.getUuid("klient_id"), row.getUuid("kierowcy_id"), row.getString("miasto"), row.getString("ulica"), row.getInt("numer_domu"), row.getInt("ilosc_paczek"), row.getFloat("pobranie"), row.getInt("identyfikator_rejonu_paczki")))
        );
        return arrayList.get(0);
    }

    /**
     * Pobieranie packzi
     * @param employeeUUID
     * @return
     */
    public ArrayList<ParcelsModel> findByEmployee(UUID employeeUUID) {
        ArrayList<ParcelsModel> arrayListCity = new ArrayList<>();
        ArrayList<ParcelsModel> arrayList;
        arrayList = findAll();
        for (ParcelsModel parcelsModel : arrayList) {
            if (parcelsModel.getEmployee_id().equals(employeeUUID)) {
                arrayListCity.add(parcelsModel);
            }
        }
        return arrayListCity;
    }

    public ArrayList<ParcelsModel> findByClient(UUID clientUUID) {
        ArrayList<ParcelsModel> arrayListCity = new ArrayList<>();
        ArrayList<ParcelsModel> arrayList;
        arrayList = findAll();
        for (ParcelsModel parcelsModel : arrayList) {
            if (parcelsModel.getClient_id().equals(clientUUID)) {
                arrayListCity.add(parcelsModel);
            }
        }
        return arrayListCity;
    }

    /**
     * Pobieranie paczki według miasta.
     * @param city Wyszukiwanie paczek według miasta.
     * @return Lista obiektów paczek.
     */
    public ArrayList<ParcelsModel> findByCity(String city) {
        ArrayList<ParcelsModel> arrayListCity = new ArrayList<>();
        ArrayList<ParcelsModel> arrayList;
        arrayList = findAll();
        for (ParcelsModel parcelsModel : arrayList) {
            if (parcelsModel.getMiasto().equals(city)) {
                arrayListCity.add(parcelsModel);
            }
        }
        return arrayListCity;
    }

    /**
     *  Aktualizacja miasta docelowego paczki.
     * @param city Nowe miasto
     * @param uuid
     * @param trackingID
     */
    public void setCityByUUIDAndTrackingID(String city, UUID uuid, Integer trackingID) {
        session.execute("UPDATE paczki SET miasto='" + city + "'WHERE paczka_id=" + uuid + "AND identyfikator_rejonu_paczki=" + trackingID);
    }

    /**
     * Usuwanie paczki.
     * @param uuid Identyfikator paczki.
     * @param trackingID Identyfikator paczki w rejonie.
     */
    public void removeByUUIDAndTrackingID(UUID uuid, Integer trackingID) {
        session.execute(deleteFrom("paczki").whereColumn("paczka_id").isEqualTo(literal(uuid)).whereColumn("identyfikator_rejonu_paczki").isEqualTo(literal(trackingID)).build());
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
    public void saveParcels(UUID client_id, UUID employee_id, String miasto, String ulica, Integer numer_domu, Integer ilosc_paczek, float pobranie, Integer identyfikator_rejonu_paczki) {
        session.execute(insertInto("paczki")
                .value("paczka_id", now())
                .value("klient_id", literal(client_id))
                .value("kierowcy_id", literal(employee_id))
                .value("miasto", literal(miasto))
                .value("ulica", literal(ulica))
                .value("numer_domu", literal(numer_domu))
                .value("ilosc_paczek", literal(ilosc_paczek))
                .value("pobranie", literal(pobranie))
                .value("identyfikator_rejonu_paczki", literal(identyfikator_rejonu_paczki)).build());
    }
}
