package com.example.tahminoyunudeneme2;

public class Sorular {
    private String sorumetni;
    private Integer sorucevap;

    public Sorular() {
    }

    public Sorular(String sorumetni, Integer sorucevap) {
        this.sorumetni = sorumetni;
        this.sorucevap = sorucevap;
    }

    public String getSorumetni() {
        return sorumetni;
    }

    public void setSorumetni(String sorumetni) {
        this.sorumetni = sorumetni;
    }

    public Integer getSorucevap() {
        return sorucevap;
    }

    public void setSorucevap(Integer sorucevap) {
        this.sorucevap = sorucevap;
    }
}
