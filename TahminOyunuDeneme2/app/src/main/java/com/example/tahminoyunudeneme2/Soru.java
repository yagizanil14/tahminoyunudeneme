package com.example.tahminoyunudeneme2;

public class Soru {
    private String SoruMetni;
    private Integer Cevap;

    public Soru() {
    }

    public Soru(String soruMetni, Integer cevap) {
        SoruMetni = soruMetni;
        Cevap = cevap;
    }

    public String getSoruMetni() {
        return SoruMetni;
    }

    public void setSoruMetni(String soruMetni) {
        SoruMetni = soruMetni;
    }

    public Integer getCevap() {
        return Cevap;
    }

    public void setCevap(Integer cevap) {
        Cevap = cevap;
    }
}

