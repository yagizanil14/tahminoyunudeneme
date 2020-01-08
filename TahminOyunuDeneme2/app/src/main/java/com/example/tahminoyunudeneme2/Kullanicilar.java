package com.example.tahminoyunudeneme2;

import java.util.List;

public class Kullanicilar {
    private String Uid;
    private List<Integer> skor;
    private List<String> KullaniciAdi;

    public Kullanicilar() {
    }

    public Kullanicilar(String uid, List<String> kullaniciAdi, List<Integer>  skor) {
        Uid = uid;
        KullaniciAdi = kullaniciAdi;
        this.skor = skor;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public List<String> getKullaniciAdi() {
        return KullaniciAdi;
    }

    public void setKullaniciAdi(List<String> kullaniciAdi) {
        KullaniciAdi = kullaniciAdi;
    }

    public List<Integer>  getSkor() {
        return skor;
    }

    public void setSkor(List<Integer>  skor) {
        this.skor = skor;
    }
}
