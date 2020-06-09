package com.simplemethod.dhx.InfluxDBDataModel;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.math.BigInteger;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Measurement(name="paczki", database = "technologie",timeUnit = TimeUnit.MILLISECONDS)
public class ParcelsModel {
    @Column(name = "time")
    Instant parcel_id;
    @Column(name = "klient_id")
    Instant client_id;
    @Column(name = "kierowcy_id")
    Instant employee_id;
    @Column(name = "miasto")
    String miasto;
    @Column(name = "ulica")
    String ulica;
    @Column(name = "numer_domu")
    Integer numer_domu;
    @Column(name = "ilosc_paczek")
    Integer ilosc_paczek;
    @Column(name = "pobranie")
    Double pobranie;
    @Column(name = "identyfikator_rejonu_paczki")
    Integer identyfikator_rejonu_paczki;

    public Instant getParcel_id() {
        return parcel_id;
    }

    public void setParcel_id(Instant parcel_id) {
        this.parcel_id = parcel_id;
    }

    public Instant getClient_id() {
        return client_id;
    }

    public void setClient_id(Instant client_id) {
        this.client_id = client_id;
    }

    public Instant getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(Instant employee_id) {
        this.employee_id = employee_id;
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

    public Integer getIlosc_paczek() {
        return ilosc_paczek;
    }

    public void setIlosc_paczek(Integer ilosc_paczek) {
        this.ilosc_paczek = ilosc_paczek;
    }

    public Double getPobranie() {
        return pobranie;
    }

    public void setPobranie(Double pobranie) {
        this.pobranie = pobranie;
    }

    public Integer getIdentyfikator_rejonu_paczki() {
        return identyfikator_rejonu_paczki;
    }

    public void setIdentyfikator_rejonu_paczki(Integer identyfikator_rejonu_paczki) {
        this.identyfikator_rejonu_paczki = identyfikator_rejonu_paczki;
    }

    @Override
    public String toString() {
        return "\r\n Identyfkator paczki:" +  String.format("%019d", BigInteger.valueOf(parcel_id.toEpochMilli())) +
                "\r\n Identyfikator klienta:" +  String.format("%019d", BigInteger.valueOf(client_id.toEpochMilli())) +
                "\r\n Identyfikator kierowcy:" +  String.format("%019d", BigInteger.valueOf(employee_id.toEpochMilli())) +
                "\r\n Miasto:" + miasto  +
                "\r\n Ulica:" + ulica +
                "\r\n Numer domu:" + numer_domu +
                "\r\n Ilosc paczek:" + ilosc_paczek +
                "\r\n Kwota pobrania:" + pobranie +
                "\r\n Identyfikator rejonu paczki:" + identyfikator_rejonu_paczki
                ;
    }
}
