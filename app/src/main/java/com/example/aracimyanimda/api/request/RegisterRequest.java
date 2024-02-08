package com.example.aracimyanimda.api.request;

public class RegisterRequest {
    private String ad;
    private String soyad;
    private String ePosta;
    private String sifre;
    private String telefon;
    private boolean sozlesmeKabul;
    private String dogumTarihi;
    public RegisterRequest(String ePosta, String Sifre, String Ad, String Soyad,
                           String Telefon, boolean SozlesmeKabul) {
        this.ad = Ad;
        this.soyad = Soyad;
        this.ePosta = ePosta;
        this.sifre = Sifre;
        this.telefon = Telefon;
        this.sozlesmeKabul = SozlesmeKabul;
    }
    public String getePosta() {
        return ePosta;
    }

    public void setePosta(String ePosta) {
        this.ePosta = ePosta;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getDogumTarihi() {
        return dogumTarihi;
    }

    public void setDogumTarihi(String dogumTarihi) {
        this.dogumTarihi = dogumTarihi;
    }

    public boolean isSozlesmeKabul() {
        return sozlesmeKabul;
    }

    public void setSozlesmeKabul(boolean sozlesmeKabul) {
        this.sozlesmeKabul = sozlesmeKabul;
    }
}
