package com.example.aracimyanimda.model;

public class User {
    private int userId; // Kullanıcı ID'si
    private String ad; // Kullanıcı adı
    private String soyad; // Kullanıcı soyadı
    private String telefon; // Kullanıcı telefon numarası
    private String ePosta; // Kullanıcı e-posta adresi
    private String durum; // Kullanıcı durumu

    // Kullanıcı sınıfının yapıcı yöntemi
    public User(int userId, String ad, String soyad, String telefon, String ePosta, String durum) {
        this.userId = userId; // Kullanıcı ID'sini ayarla
        this.ad = ad; // Kullanıcı adını ayarla
        this.soyad = soyad; // Kullanıcı soyadını ayarla
        this.telefon = telefon; // Kullanıcı telefon numarasını ayarla
        this.ePosta = ePosta; // Kullanıcı e-posta adresini ayarla
        this.durum = durum; // Kullanıcı durumunu ayarla
    }

    // Kullanıcı ID'sini getiren yöntem
    public int getUserId() {
        return userId; // Kullanıcı ID'sini döndür
    }

    // Kullanıcı ID'sini ayarlayan yöntem
    public void setUserId(int userId) {
        this.userId = userId; // Kullanıcı ID'sini ayarla
    }

    // Kullanıcı adını getiren yöntem
    public String getAd() {
        return ad; // Kullanıcı adını döndür
    }

    // Kullanıcı soyadını getiren yöntem
    public String getSoyad() {
        return soyad; // Kullanıcı soyadını döndür
    }

    // Kullanıcı telefon numarasını getiren yöntem
    public String getTelefon() {
        return telefon; // Kullanıcı telefon numarasını döndür
    }

    // Kullanıcı e-posta adresini getiren yöntem
    public String getePosta() {
        return ePosta; // Kullanıcı e-posta adresini döndür
    }
}
