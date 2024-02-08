package com.example.aracimyanimda.api.response;

public class RentResponse {
    private Integer km;
    private Double miktar;

    public Integer getKm() {
        return km;
    }

    public void setKm(Integer km) {
        this.km = km;
    }

    public Double getMiktar() {
        return miktar;
    }

    public void setMiktar(Double miktar) {
        this.miktar = miktar;
    }

    public RentResponse(Integer km, Double miktar) {
        this.km = km;
        this.miktar = miktar;
    }
}
