package com.erendogan6.aracimyanimda.model;

public class Register {
    private String ad;
    private String soyad;
    private String ePosta;
    private String sifre;
    private String telefon;
    private boolean sozlesmeKabul;
    private String dogumTarihi;

    public Register(String ePosta, String Sifre, String Ad, String Soyad, String Telefon, boolean SozlesmeKabul) {
        this.ad = Ad;
        this.soyad = Soyad;
        this.ePosta = ePosta;
        this.sifre = Sifre;
        this.telefon = Telefon;
        this.sozlesmeKabul = SozlesmeKabul;
    }
}
