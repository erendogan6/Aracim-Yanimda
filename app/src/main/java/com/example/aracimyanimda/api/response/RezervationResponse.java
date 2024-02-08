package com.example.aracimyanimda.api.response;

public class RezervationResponse {
    private String baslangicTarihi;
    private String bitisTarihi;
    private String rezervasyonDurum;
    private float miktar;
    private String plaka;
    private String aracIsmi;

    public RezervationResponse(String baslangicTarihi, String bitisTarihi, String rezervasyonDurum, float miktar, String plaka, String aracIsmi) {
        this.baslangicTarihi = baslangicTarihi;
        this.bitisTarihi = bitisTarihi;
        this.rezervasyonDurum = rezervasyonDurum;
        this.miktar = miktar;
        this.plaka = plaka;
        this.aracIsmi = aracIsmi;
    }

    public String getBaslangicTarihi() {
        return baslangicTarihi;
    }

    public void setBaslangicTarihi(String baslangicTarihi) {
        this.baslangicTarihi = baslangicTarihi;
    }

    public String getBitisTarihi() {
        return bitisTarihi;
    }

    public void setBitisTarihi(String bitisTarihi) {
        this.bitisTarihi = bitisTarihi;
    }

    public String getRezervasyonDurum() {
        return rezervasyonDurum;
    }

    public void setRezervasyonDurum(String rezervasyonDurum) {
        this.rezervasyonDurum = rezervasyonDurum;
    }

    public float getMiktar() {
        return miktar;
    }

    public void setMiktar(int miktar) {
        this.miktar = miktar;
    }

    public String getPlaka() {
        return plaka;
    }

    public void setPlaka(String plaka) {
        this.plaka = plaka;
    }

    public String getAracIsmi() {
        return aracIsmi;
    }

    public void setAracIsmi(String aracIsmi) {
        this.aracIsmi = aracIsmi;
    }
}
