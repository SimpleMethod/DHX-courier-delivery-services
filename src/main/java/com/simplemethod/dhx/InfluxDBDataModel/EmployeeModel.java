package com.simplemethod.dhx.InfluxDBDataModel;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Measurement(name="kierowcy", database = "technologie",timeUnit = TimeUnit.MILLISECONDS)
public class EmployeeModel {
    @Column(name = "time")
    Instant employee_id;
    @Column(name = "imie")
    String imie;
    @Column(name = "nazwisko")
    String nazwisko;
    @Column(name = "nazwisko")
    Integer telefon;


    public Instant getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(Instant employee_id) {
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

    @Override
    public String toString() {
        return "Identyfikator pracownika=" + employee_id.toEpochMilli() +
                "\r\n Imie:" + imie  +
                "\r\n Nazwisko:" + nazwisko +
                "\r\n Telefon:" + telefon;
    }
}
