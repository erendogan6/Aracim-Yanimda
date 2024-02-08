package com.example.aracimyanimda.api.response;
public class VehicleResponse {
    private String vehicleId;
    private String marka;
    private String model;
    private String yil;
    private String plaka;
    private String yakitTipi;
    private String yakitDurumu;
    private String adres;
    private String fiyatGunluk;
    private String fiyatDakika;
    private String latitude;
    private String longitude;
    private String durum;

    public VehicleResponse(String vehicleId, String marka, String model, String yil, String plaka, String yakitTipi, String yakitDurumu, String fiyatGunluk, String fiyatDakika, String latitude, String longitude,String durum) {
        this.vehicleId = vehicleId;
        this.marka = marka;
        this.model = model;
        this.yil = yil;
        this.plaka = plaka;
        this.yakitTipi = yakitTipi;
        this.yakitDurumu = yakitDurumu;
        this.fiyatGunluk = fiyatGunluk;
        this.fiyatDakika = fiyatDakika;
        this.latitude = latitude;
        this.longitude = longitude;
        this.durum = durum;
    }

    public String getDurum() {
        return durum;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getMarka() {
        return marka;
    }

    public String getModel() {
        return model;
    }

    public String getYil() {
        return yil;
    }

    public String getPlaka() {
        return plaka;
    }

    public String getYakitTipi() {
        return yakitTipi;
    }

    public String getYakitDurumu() {
        return yakitDurumu;
    }

    public String getFiyatGunluk() {
        return fiyatGunluk;
    }

    public String getFiyatDakika() {
        return fiyatDakika;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

}
