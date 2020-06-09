package com.simplemethod.dhx.cassandraDAO;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.simplemethod.dhx.CassandraDataModel.ClientModel;
import com.simplemethod.dhx.CassandraDataModel.EmployeeModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.UUID;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.*;

@Component
public class EmployeeDAO {
    private CqlSession session;

    public void setSession(CqlSession session) {
        this.session = session;
    }

    /**
     * Pobiera wszystkich pracowników.
     * @return Tablica z obiektami pracowników.
     */
    public ArrayList<EmployeeModel> findAll() {
        ArrayList<EmployeeModel> arrayList = new ArrayList<>();
        Select selectUser = selectFrom("kierowcy").all();
        ResultSet rs = session.execute(selectUser.build());
        rs.forEach(
                row -> arrayList.add(new EmployeeModel(row.getUuid("kierowcy_id"), row.getString("imie"), row.getString("nazwisko"),row.getInt("telefon")))
        );
        return arrayList;
    }

    /**
     * Pobiera pracowników po numerze telefonu.
     * @param phoneNumber Numer telefonu pracownika.
     * @return Lista obiektów z pracownikami.
     */
    public ArrayList<EmployeeModel> findAllByPhoneNumber(Integer phoneNumber) {
        ArrayList<EmployeeModel> arrayList = new ArrayList<>();
        Select selectUser = selectFrom("kierowcy").all().whereColumn("telefon").isEqualTo(literal(phoneNumber)).allowFiltering();
        ResultSet rs = session.execute(selectUser.build());
        rs.forEach(
                row -> arrayList.add(new EmployeeModel(row.getUuid("kierowcy_id"), row.getString("imie"), row.getString("nazwisko"),row.getInt("telefon")))
        );
        return arrayList;
    }

    /**
     *  Pobiera pracowników według numeru telefonu i imienia.
     * @param phoneNumber Numer telefonu.
     * @param name Imie.
     * @return Obiekt z danymi pracownika.
     */
    public EmployeeModel findByPhoneNumberAndName(Integer phoneNumber, String name) {
        EmployeeModel clientModel = null;
        ArrayList<EmployeeModel> arrayList;
        arrayList = findAllByPhoneNumber(phoneNumber);
        for (EmployeeModel ClientModel : arrayList) {
            if (ClientModel.getImie().equals(name)) {
                clientModel = ClientModel;
            }
        }
        return clientModel;
    }

    /**
     * Aktualizuje nazwisko pracownika.
     * @param nazwisko Nowe nazwisko pracownika.
     * @param uuid Identyfikator pracownika.
     * @param telefon Telefon pracownika.
     */
    public void setSurnameByUUIDAndPhoneNumber(String nazwisko, UUID uuid, Integer telefon) {
        session.execute("UPDATE kierowcy SET nazwisko='" + nazwisko + "'WHERE kierowcy_id=" + uuid + "AND telefon=" + telefon);
    }

    /**
     * Usuwa pracownika.
     * @param uuid Identyfiaktor pracownika.
     * @param telefon Numer telefonu pracownika.
     */
    public void removeByUUIDAndPhoneNumber(UUID uuid, Integer telefon) {
        session.execute(deleteFrom("kierowcy").whereColumn("kierowcy_id").isEqualTo(literal(uuid)).whereColumn("telefon").isEqualTo(literal(telefon)).build());
    }

    /**
     *  Pobiera pracownika po identyfikatorze.
     * @param uuid Identyfiator pracownika.
     * @return Obiekt pracownika.
     */
    public EmployeeModel findByUUID(UUID uuid) {
        ArrayList<EmployeeModel> arrayList = new ArrayList<>();
        Select selectUser = selectFrom("kierowcy").all().whereColumn("kierowcy_id").isEqualTo(literal(uuid)).allowFiltering();
        ResultSet rs = session.execute(selectUser.build());
        rs.forEach(
                row -> arrayList.add(new EmployeeModel(row.getUuid("kierowcy_id"), row.getString("imie"), row.getString("nazwisko"), row.getInt("telefon")))
        );
        return arrayList.get(0);
    }

    /**
     * Dodaje nowego pracownika do bazy danych.
     * @param imie Imie pracownika.
     * @param nazwisko Nazwisko pracownika.
     * @param telefon Telefon pracownika.
     */
    public void saveEmployee(String imie, String nazwisko, Integer telefon) {
        session.execute(insertInto("kierowcy")
                .value("kierowcy_id", now())
                .value("imie", literal(imie))
                .value("nazwisko", literal(nazwisko))
                .value("telefon", literal(telefon)).build());
    }
}
