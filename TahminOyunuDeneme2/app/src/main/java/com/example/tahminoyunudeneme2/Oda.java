package com.example.tahminoyunudeneme2;

import java.util.List;

public class Oda {
    private String odauid;
    private List<String> kullanicilar;
    private Boolean musaitmi;
    private List<Soru> sorular;

    public Oda() {
    }

    public Oda(String odauid) {
        this.odauid = odauid;
    }

    public String getOdauid() {
        return odauid;
    }

    public void setOdauid(String odauid) {
        this.odauid = odauid;
    }

    public Oda(List<String> kullanicilar, Boolean musaitmi, List<Soru> sorular) {
        this.kullanicilar = kullanicilar;
        this.musaitmi = musaitmi;
        this.sorular = sorular;
    }

    public List<String> getKullanicilar() {
        return kullanicilar;
    }

    public void setKullanicilar(List<String> kullanicilar) {
        this.kullanicilar = kullanicilar;
    }

    public Boolean getMusaitmi() {
        return musaitmi;
    }

    public void setMusaitmi(Boolean musaitmi) {
        this.musaitmi = musaitmi;
    }

    public List<Soru> getSorular() {
        return sorular;
    }

    public void setSorular(List<Soru> sorular) {
        this.sorular = sorular;
    }
}