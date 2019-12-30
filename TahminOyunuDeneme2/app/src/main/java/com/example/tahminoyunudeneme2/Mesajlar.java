package com.example.tahminoyunudeneme2;

import java.util.List;

public class Mesajlar {
    private String mesajlar,rakipmesaj;

    public Mesajlar() {
    }

    public Mesajlar(String mesajlar, String rakipmesaj) {
        this.mesajlar = mesajlar;
        this.rakipmesaj = rakipmesaj;
    }

    public String getMesajlar() {
        return mesajlar;
    }

    public void setMesajlar(String mesajlar) {
        this.mesajlar = mesajlar;
    }

    public String getRakipmesaj() {
        return rakipmesaj;
    }

    public void setRakipmesaj(String rakipmesaj) {
        this.rakipmesaj = rakipmesaj;
    }
}
