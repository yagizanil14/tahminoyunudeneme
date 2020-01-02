package com.example.tahminoyunudeneme2;

public class Kullanicilar {
    private String Uid,KullaniciAdi,skor;


    public Kullanicilar() {
    }

    public Kullanicilar(String uid, String kullaniciAdi, String skor) {
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

    public String getKullaniciAdi() {
        return KullaniciAdi;
    }

    public void setKullaniciAdi(String kullaniciAdi) {
        KullaniciAdi = kullaniciAdi;
    }

    public String getSkor() {
        return skor;
    }

    public void setSkor(String skor) {
        this.skor = skor;
    }
}
