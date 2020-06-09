package com.simplemethod.dhx.CassandraDataModel;

import javax.annotation.Resource;
import java.util.UUID;

@Resource
public class ParcelsModel {
    UUID parcel_id;
    UUID client_id;
    UUID employee_id;
    String miasto;
    String ulica;
    Integer numer_domu;
    Integer ilosc_paczek;
    Float pobranie;
    Integer identyfikator_rejonu_paczki;

    public UUID getParcel_id() {
        return parcel_id;
    }

    public void setParcel_id(UUID parcel_id) {
        this.parcel_id = parcel_id;
    }

    public UUID getClient_id() {
        return client_id;
    }

    public void setClient_id(UUID client_id) {
        this.client_id = client_id;
    }

    public UUID getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(UUID employee_id) {
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

    public Float getPobranie() {
        return pobranie;
    }

    public void setPobranie(Float pobranie) {
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
        return "\r\n Identyfkator paczki:" + parcel_id +
                "\r\n Identyfikator klienta:" + client_id +
                "\r\n Identyfikator kierowcy:" + employee_id +
                "\r\n Miasto:" + miasto  +
                "\r\n Ulica:" + ulica +
                "\r\n Numer domu:" + numer_domu +
                "\r\n Ilosc paczek:" + ilosc_paczek +
                "\r\n Kwota pobrania:" + pobranie +
                "\r\n Identyfikator rejonu paczki:" + identyfikator_rejonu_paczki
                ;
    }

    public ParcelsModel(UUID parcel_id, UUID client_id, UUID employee_id, String miasto, String ulica, Integer numer_domu, Integer ilosc_paczek, Float pobranie, Integer identyfikator_rejonu_paczki) {
        this.parcel_id = parcel_id;
        this.client_id = client_id;
        this.employee_id = employee_id;
        this.miasto = miasto;
        this.ulica = ulica;
        this.numer_domu = numer_domu;
        this.ilosc_paczek = ilosc_paczek;
        this.pobranie = pobranie;
        this.identyfikator_rejonu_paczki = identyfikator_rejonu_paczki;
    }
}
