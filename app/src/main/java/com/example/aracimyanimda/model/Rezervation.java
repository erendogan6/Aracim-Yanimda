package com.example.aracimyanimda.model;

public class Rezervation {
    private int vehicleId;
    private int userId;
    private String baslangicTarihi;
    private String bitisTarihi;
    private String rezervasyonDurum;
    private String durum;
    private float miktar;
    private String plaka;
    private String aracIsmi;
    private String tur;

    public Rezervation(int vehicleId, int userId, String baslangicTarihi, String bitisTarihi, String durum) {
        this.vehicleId = vehicleId;
        this.userId = userId;
        this.baslangicTarihi = baslangicTarihi;
        this.bitisTarihi = bitisTarihi;
        this.durum = durum;
        this.tur = "";
    }

    public int getUserId() {
        return userId;
    }

    public String getBaslangicTarihi() {
        return baslangicTarihi;
    }

    public String getBitisTarihi() {
        return bitisTarihi;
    }

    public String getRezervasyonDurum() {
        return rezervasyonDurum;
    }

    public float getMiktar() {
        return miktar;
    }
    public String getPlaka() {
        return plaka;
    }
    public String getAracIsmi() {
        return aracIsmi;
    }
}
