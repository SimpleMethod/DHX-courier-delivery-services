package com.simplemethod.dhx.CassandraDataModel;

import javax.annotation.Resource;
import java.util.UUID;

@Resource
public class ClientModel {
    UUID client_id;
    String name;
    String nazwisko;
    String miasto;
    String ulica;
    Integer numer_domu;
    Integer telefon;

    public ClientModel(UUID client_id, String imie, String nazwisko, String miasto, String ulica, Integer numer_domu, Integer telefon) {
        this.client_id = client_id;
        this.name = imie;
        this.nazwisko = nazwisko;
        this.miasto = miasto;
        this.ulica = ulica;
        this.numer_domu = numer_domu;
        this.telefon = telefon;
    }

    public UUID getClient_id() {
        return client_id;
    }

    public void setClient_id(UUID client_id) {
        this.client_id = client_id;
    }

    public String getImie() {
        return name;
    }

    public void setImie(String imie) {
        this.name = imie;
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
                "Identyfikator klienta:" + client_id +
                "\r\n Imie:" + name +
                "\r\n Nazwisko:" + nazwisko +
                "\r\n Miasto:" + miasto +
                "\r\n Ulica:" + ulica +
                "\r\n Numer domu:" + numer_domu +
                "\r\n Telefon:" + telefon;
    }
}
