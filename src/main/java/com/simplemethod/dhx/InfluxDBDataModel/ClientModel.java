package com.simplemethod.dhx.InfluxDBDataModel;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Measurement(name="klienci", database = "technologie",timeUnit = TimeUnit.MILLISECONDS)
public class ClientModel {

    @Column(name = "time")
    Instant client_id;
    @Column(name = "imie")
    String name;
    @Column(name = "nazwisko")
    String nazwisko;
    @Column(name = "miasto")
    String miasto;
    @Column(name = "ulica")
    String ulica;
    @Column(name = "numer_domu")
    Integer numer_domu;
    @Column(name = "telefon")
    Integer telefon;

    public Instant getClient_id() {
        return client_id;
    }

    public void setClient_id(Instant client_id) {
        this.client_id = client_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public String getMiasto() {
        return miasto;
    }

    public void setMiasto(String miasto) {
        this.miasto = miasto;
    }

    public String getUlica() {
        return ulica;
    }

    public void setUlica(String ulica) {
        this.ulica = ulica;
    }

    public Integer getNumer_domu() {
        return numer_domu;
    }

    public void setNumer_domu(Integer numer_domu) {
        this.numer_domu = numer_domu;
    }

    public Integer getTelefon() {
        return telefon;
    }

    public void setTelefon(Integer telefon) {
        this.telefon = telefon;
    }

    @Override
    public String toString() {
        return
                "Identyfikator klienta:" + String.format("%019d", BigInteger.valueOf(client_id.toEpochMilli())) +
                        "\r\n Imie:" + name +
                        "\r\n Nazwisko:" + nazwisko +
                        "\r\n Miasto:" + miasto +
                        "\r\n Ulica:" + ulica +
                        "\r\n Numer domu:" + numer_domu +
                        "\r\n Telefon:" + telefon;
    }
}
