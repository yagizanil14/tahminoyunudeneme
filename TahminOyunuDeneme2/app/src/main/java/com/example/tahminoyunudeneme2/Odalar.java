package com.example.tahminoyunudeneme2;

import java.util.ArrayList;
import java.util.List;

public class Odalar {
    private String odauid;
    private Boolean musaitmi;
    private List<String> kullaniciuid, odadakisorular ;



    public Odalar() {
    }



    public Odalar(String odauid, Boolean musaitmi, List<String> kullaniciuid,List<String> odadakisorular) {
        this.odauid = odauid;
        this.musaitmi = musaitmi;
        this.kullaniciuid = kullaniciuid;
        this.odadakisorular = odadakisorular;
    }

    public List<String> getOdadakisorular() {
        return odadakisorular;
    }

    public void setOdadakisorular(List<String> odadakisorular) {
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
