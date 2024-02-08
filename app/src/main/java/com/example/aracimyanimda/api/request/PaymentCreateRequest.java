package com.example.aracimyanimda.api.request;

public class PaymentCreateRequest {
    private final String AdSoyad;
    private final String kartNumarasi;
    private final String cvv;
    private final String sonKullanma;
    private int userId;

    public PaymentCreateRequest(int userId, String adSoyad, String kartNumarasi, String sonKullanmaTarihi,String cvv) {
        this.userId = userId;
        this.AdSoyad = adSoyad;
        this.kartNumarasi = kartNumarasi;
        this.sonKullanma = sonKullanmaTarihi;
        this.cvv=cvv;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
