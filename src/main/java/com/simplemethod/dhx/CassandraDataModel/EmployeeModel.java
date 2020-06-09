package com.simplemethod.dhx.CassandraDataModel;

import javax.annotation.Resource;
import java.util.UUID;

@Resource
public class EmployeeModel {
    UUID employee_id;
    String imie;
    String nazwisko;
    Integer telefon;

    public UUID getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(UUID employee_id) {
        this.employee_id = employee_id;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public Integer getTelefon() {
        return telefon;
    }

    public void setTelefon(Integer telefon) {
        this.telefon = telefon;
    }

    public EmployeeModel(UUID employee_id, String imie, String nazwisko, Integer telefon) {
        this.employee_id = employee_id;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.telefon = telefon;
    }

    @Override
    public String toString() {
        return "employee_id=" + employee_id +
                "\r\n Imie:" + imie  +
                ",\r\n Nazwisko:" + nazwisko +
                "\r\n Telefon:" + telefon;
    }
}
