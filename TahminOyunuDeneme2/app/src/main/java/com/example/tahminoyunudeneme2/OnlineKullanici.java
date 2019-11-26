package com.example.tahminoyunudeneme2;

public class OnlineKullanici {

    private String uid, Onlinemi;

    public OnlineKullanici() {
    }

    public OnlineKullanici(String uid, String onlinemi) {
        this.uid = uid;
        Onlinemi = onlinemi;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOnlinemi() {
        return Onlinemi;
    }

    public void setOnlinemi(String onlinemi) {
        Onlinemi = onlinemi;
    }
}
