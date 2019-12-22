package com.example.tahminoyunudeneme2;


public class Sorular {
    private String sorumetni,sorucevap;

    public Sorular() {
    }

    public Sorular(String sorumetni, String sorucevap) {
        this.sorumetni = sorumetni;
        this.sorucevap = sorucevap;
    }

    public String getSorumetni() {
        return sorumetni;
    }

    public void setSorumetni(String sorumetni) {
        this.sorumetni = sorumetni;
    }

    public String getSorucevap() {
        return sorucevap;
    }

    public void setSorucevap(String sorucevap) {
        this.sorucevap = sorucevap;
    }
}
