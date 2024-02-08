package com.example.aracimyanimda.model;

public class License {
    private String ehliyetTipi;
    private String ehliyetNo;
    private String sonGecerlilikTarihi;
    private int userId;

    public License(String ehliyetTipi, String ehliyetNo, String sonGecerlilikTarihi, int userId) {
        this.ehliyetTipi = ehliyetTipi;
        this.ehliyetNo = ehliyetNo;
        this.sonGecerlilikTarihi = sonGecerlilikTarihi;
        this.userId = userId;
    }

    public String getEhliyetNo() {
        return ehliyetNo;
    }
    public String getSonGecerlilikTarihi() {
        return sonGecerlilikTarihi;
    }
}
