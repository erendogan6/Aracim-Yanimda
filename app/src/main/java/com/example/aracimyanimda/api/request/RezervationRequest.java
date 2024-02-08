package com.example.aracimyanimda.api.request;
public class RezervationRequest {
    private final int vehicleId;
    private final int userId;
    private String baslangicTarihi;
    private String bitisTarihi;
    private String durum;
    private final String tur;
    public RezervationRequest(int vehicleId, int userId, String baslangicTarihi, String bitisTarihi, String durum ) {
        this.vehicleId = vehicleId;
        this.userId = userId;
        this.baslangicTarihi = baslangicTarihi;
        this.bitisTarihi = bitisTarihi;
        this.durum = durum;
        this.tur="";
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

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }
}
