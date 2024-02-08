package com.example.aracimyanimda.viewmodel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Boolean> paymentAdded = new MutableLiveData<>(); // Ödeme eklendiğinde tetiklenecek yaşam döngüsü verisi
    private final MutableLiveData<Boolean> paymentSelect = new MutableLiveData<>(); // Ödeme seçildiğinde tetiklenecek yaşam döngüsü verisi

    // Ödeme eklenme durumunu ayarla
    public void setPaymentAdded(boolean isCompleted) {
        paymentAdded.setValue(isCompleted); // Ödeme eklenme durumunu güncelle
    }

    // Ödeme eklenme durumunu al
    public LiveData<Boolean> getPaymentAdded() {
        return paymentAdded; // Ödeme eklenme durumunu döndür
    }

    // Ödeme seçme durumunu ayarla
    public void setPaymentSelect(boolean isCompleted) {
        paymentSelect.setValue(isCompleted); // Ödeme seçme durumunu güncelle
    }

    // Ödeme seçme durumunu al
    public LiveData<Boolean> getPaymentSelect() {
        return paymentSelect; // Ödeme seçme durumunu döndür
    }
}
