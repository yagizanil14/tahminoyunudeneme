package com.example.tahminoyunudeneme2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Odalar {
    private String odauid;
    private Boolean musaitmi;
    private List<String> kullaniciuid ;
    private List<Cevaplar> cevaplar;
    private List<Sorular> odadakisorular;

    public Odalar() {
    }


    public Odalar(String odauid, Boolean musaitmi, List<String> kullaniciuid, List<Sorular> odadakisorular,List<Cevaplar> cevaplar) {
        this.odauid = odauid;
        this.musaitmi = musaitmi;
        this.kullaniciuid = kullaniciuid;
        this.odadakisorular = odadakisorular;
        this.cevaplar = cevaplar;
    }

    public List<Cevaplar> getCevaplar() {
        return cevaplar;
    }

    public void setCevaplar(List<Cevaplar> cevaplar) {
        this.cevaplar = cevaplar;
    }

    public List<Sorular> getOdadakisorular() {
        return odadakisorular;
    }

    public void setOdadakisorular(List<Sorular> odadakisorular) {
        this.odadakisorular = odadakisorular;
    }

    public List<String> getKullaniciuid() {
        return kullaniciuid;
    }

    public void setKullaniciuid(List<String> kullaniciuid) {
        this.kullaniciuid = kullaniciuid;
    }

    public Boolean getMusaitmi() {
        return musaitmi;
    }

    public void setMusaitmi(Boolean musaitmi) {
        this.musaitmi = musaitmi;
    }

    public String getOdauid() {
        return odauid;
    }

    public void setOdauid(String odauid) {
        this.odauid = odauid;
    }



}
