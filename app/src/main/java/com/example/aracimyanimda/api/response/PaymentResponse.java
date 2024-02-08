package com.example.aracimyanimda.api.response;
public class PaymentResponse {
    private int paymentMethodId;
    private String adSoyad;
    private String kartNumarasi;
    private String cvv;
    private String sonKullanma;
    private int userId;

    // Yapıcı method, ödeme yanıtı oluşturur
    public PaymentResponse(int paymentMethodId, String adSoyad, String kartNumarasi, String cvv, String sonKullanma, int userId) {
        this.paymentMethodId = paymentMethodId; // Ödeme yöntemi kimliğini ayarla
        this.adSoyad = adSoyad; // Ad ve soyadı ayarla
        this.kartNumarasi = kartNumarasi; // Kart numarasını ayarla
        this.cvv = cvv; // CVV numarasını ayarla
        this.sonKullanma = sonKullanma; // Son kullanma tarihini ayarla
        this.userId = userId; // Kullanıcı kimliğini ayarla
    }

    // Ödeme yöntemi kimliğini döndür
    public int getPaymentMethodId() {
        return paymentMethodId;
    }

    // Kart numarasını döndür
    public String getKartNumarasi() {
        return kartNumarasi;
    }

    // Son kullanma tarihini döndür
    public String getSonKullanma() {
        return sonKullanma;
    }
}
