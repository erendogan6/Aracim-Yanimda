package com.erendogan6.aracimyanimda.model;

public class Payment {
    private int userId;
    private int paymentMethodId;
    private String adSoyad;
    private String kartNumarasi;
    private String cvv;
    private String sonKullanma;

    // API üzerinden Payment çekmek için
    public Payment(int paymentMethodId, String adSoyad, String kartNumarasi, String cvv, String sonKullanma, int userId) {
        this.paymentMethodId = paymentMethodId; // Ödeme yöntemi kimliğini ayarla
        this.adSoyad = adSoyad; // Ad ve soyadı ayarla
        this.kartNumarasi = kartNumarasi; // Kart numarasını ayarla
        this.cvv = cvv; // CVV numarasını ayarla
        this.sonKullanma = sonKullanma; // Son kullanma tarihini ayarla
        this.userId = userId; // Kullanıcı kimliğini ayarla
    }

    // API üzerinden yeni payment eklemek için
    public Payment(int userId, String adSoyad, String kartNumarasi, String sonKullanmaTarihi, String cvv) {
        this.userId = userId;
        this.adSoyad = adSoyad;
        this.kartNumarasi = kartNumarasi;
        this.sonKullanma = sonKullanmaTarihi;
        this.cvv = cvv;
    }

    public int getPaymentMethodId() {
        return paymentMethodId;
    }

    public String getKartNumarasi() {
        return kartNumarasi;
    }

    public String getSonKullanma() {
        return sonKullanma;
    }
}
