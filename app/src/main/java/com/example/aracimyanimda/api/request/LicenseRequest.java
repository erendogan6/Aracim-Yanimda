package com.example.aracimyanimda.api.request;
public class LicenseRequest {
    private String ehliyetTipi;
    private String ehliyetNo;
    private String sonGecerlilikTarihi;
    private int userId;
    public LicenseRequest(String ehliyetTipi, String ehliyetNo, String sonGecerlilikTarihi, int userId) {
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
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
