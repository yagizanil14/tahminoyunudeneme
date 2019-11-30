package com.example.tahminoyunudeneme2;

import java.util.List;

public class Cevaplar {
    private String kullaniciuuid, cevaplar;

    public Cevaplar() {
    }

    public Cevaplar(String kullaniciuuid, String cevaplar) {
        this.kullaniciuuid = kullaniciuuid;
        this.cevaplar = cevaplar;
    }

    public String getKullaniciuuid() {
        return kullaniciuuid;
    }

    public void setKullaniciuuid(String kullaniciuuid) {
        this.kullaniciuuid = kullaniciuuid;
    }

    public String getCevaplar() {
        return cevaplar;
    }

    public void setCevaplar(String cevaplar) {
        this.cevaplar = cevaplar;
    }
}
