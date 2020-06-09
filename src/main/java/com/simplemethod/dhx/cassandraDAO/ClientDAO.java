package com.simplemethod.dhx.cassandraDAO;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.simplemethod.dhx.CassandraDataModel.ClientModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.UUID;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.*;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

@Component
public class ClientDAO {

    private CqlSession session;


    public void setSession(CqlSession session) {
        this.session = session;
    }

    /**
     * Pobieranie wszystkich klientów z bazy danych.
     * @return Listę z obietkami.
     */
    public ArrayList<ClientModel> findAll() {
        ArrayList<ClientModel> arrayList = new ArrayList<>();
        Select selectUser = selectFrom("klienci").all();
        ResultSet rs = session.execute(selectUser.build());
        rs.forEach(
                row -> arrayList.add(new ClientModel(row.getUuid("klient_id"), row.getString("imie"), row.getString("nazwisko"),
                        row.getString("miasto"), row.getString("ulica"), row.getInt("numer_domu"), row.getInt("telefon")))
        );
        return arrayList;
    }

    /**
     *  Pobieranie klientów według numeru telefonu.
     * @param phoneNumber numer telefonu klienta.
     * @return Liste z obiektami.
     */
    public ArrayList<ClientModel> findAllByPhoneNumber(Integer phoneNumber) {
        ArrayList<ClientModel> arrayList = new ArrayList<>();
        Select selectUser = selectFrom("klienci").all().whereColumn("telefon").isEqualTo(literal(phoneNumber)).allowFiltering();
        ResultSet rs = session.execute(selectUser.build());
        rs.forEach(
                row -> arrayList.add(new ClientModel(row.getUuid("klient_id"), row.getString("imie"), row.getString("nazwisko"),
                        row.getString("miasto"), row.getString("ulica"), row.getInt("numer_domu"), row.getInt("telefon")))
        );
        return arrayList;
    }

    /**
     * Pobiera klienta według imienia i nazwiska.
     * @param name Imie klienta.
     * @param surname Nazwisko klienta.
     * @return Liste z obiektami.
     */
    public ClientModel findByNameAndSurname(String name, String surname) {
        ClientModel clientModel = null;
        ArrayList<ClientModel> arrayList;
        arrayList = findAll();
        for (ClientModel ClientModel : arrayList) {
            if (ClientModel.getImie().equals(name) && ClientModel.getNazwisko().equals(surname)) {
                clientModel=ClientModel;
            }
        }
        return clientModel;
    }

    /**
     * Pobiera klienta według numeru telefonu i imienia.
     * @param phoneNumber Numer telefonu klienta.
     * @param name Imie klienta.
     * @return Obiekt z danymi klienta.
     */
    public ClientModel findByPhoneNumberAndName(Integer phoneNumber, String name) {
        ClientModel clientModel = null;
        ArrayList<ClientModel> arrayList;
        arrayList = findAllByPhoneNumber(phoneNumber);
        for (ClientModel ClientModel : arrayList) {
            if (ClientModel.getImie().equals(name)) {
                clientModel = ClientModel;
            }
        }
        return clientModel;
    }

    /**
     * Pobiera obiekt klienta według indetyfikatora.
     * @param uuid Identyfikator klienta.
     * @return Obiekt z danymi klienta.
     */
    public ClientModel findByUUID(UUID uuid) {
        ArrayList<ClientModel> arrayList = new ArrayList<>();
        Select selectUser = selectFrom("klienci").all().whereColumn("klient_id").isEqualTo(literal(uuid)).allowFiltering();
        ResultSet rs = session.execute(selectUser.build());
        rs.forEach(
                row -> arrayList.add(new ClientModel(row.getUuid("klient_id"), row.getString("imie"), row.getString("nazwisko"),
                        row.getString("miasto"), row.getString("ulica"), row.getInt("numer_domu"), row.getInt("telefon")))
        );
        return arrayList.get(0);
    }

    /**
     *  Aktualizuje imię klienta.
     * @param imie Nowe imie klienta.
     * @param uuid Indetyfikator klienta.
     * @param telefon Numer telefoni klienta.
     */
    public void setNameByUUIDAndPhoneNumber(String imie, UUID uuid, Integer telefon) {
        session.execute("UPDATE klienci SET imie='" + imie + "'WHERE klient_id=" + uuid + "AND telefon=" + telefon);
    }

    /**
     * Usuwa kliena.
     * @param uuid Identyfikator klienta.
     * @param telefon Numer telefonu klienta.
     */
    public void removeByUUIDAndPhoneNumber(UUID uuid, Integer telefon) {
        session.execute(deleteFrom("klienci").whereColumn("klient_id").isEqualTo(literal(uuid)).whereColumn("telefon").isEqualTo(literal(telefon)).build());
    }

    /**
     * Dodaje nowego klienta do bazy danych.
     * @param imie Imie klienta.
     * @param nazwisko Nazwisko klienta.
     * @param miasto Miasto.
     * @param ulica Ulica.
     * @param numerDomu Numer domu.
     * @param telefon Telefon.
     */
    public void saveCustomer(String imie, String nazwisko, String miasto, String ulica, Integer numerDomu, Integer telefon) {
        session.execute(insertInto("klienci")
                .value("klient_id", now())
                .value("imie", literal(imie))
                .value("nazwisko", literal(nazwisko))
                .value("miasto", literal(miasto))
                .value("ulica", literal(ulica))
                .value("numer_domu", literal(numerDomu))
                .value("telefon", literal(telefon)).build());
    }

}
