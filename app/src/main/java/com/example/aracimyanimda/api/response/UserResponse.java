package com.example.aracimyanimda.api.response;

public class UserResponse {
    private String userId;
    private String ad;
    private String soyad;
    private String ePosta;
    private String telefon;
    private String durum;
    public String getAd() {
        return ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public String getePosta() {
        return ePosta;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getDurum() {
        return durum;
    }
    public String getUserId() {
        return userId;
    }
}
