package com.example.aracimyanimda.adaptor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aracimyanimda.databinding.PaymentRowBinding;
import com.example.aracimyanimda.model.Payment;
import com.example.aracimyanimda.viewmodel.PaymentAdapterListener;
import com.example.aracimyanimda.viewmodel.SharedViewModel;

import java.util.List;
public class PaymentAdaptor extends RecyclerView.Adapter<PaymentAdaptor.PaymentViewHolder> {
    private final List<Payment> paymentMethods; // Ödeme yöntemlerini tutan liste
    private final SharedViewModel sharedViewModel; // Paylaşılan viewModel
    private final Bundle bundle; // Veri iletilmek için kullanılan dizi
    private final PaymentAdapterListener listener; // Ödeme yöntemi seçildiğinde gerçekleştirilecek olay dinleyicisi

    // Kurucu metot
    public PaymentAdaptor(List<Payment> paymentMethods, SharedViewModel viewModel, Bundle bundle, PaymentAdapterListener listener) {
        this.paymentMethods = paymentMethods; // Ödeme yöntemlerini ata
        this.sharedViewModel = viewModel; // Paylaşılan viewModel'i ata
        this.bundle = bundle; // Veri dizisini ata
        this.listener = listener; // Olay dinleyicisini ata
    }

    // Yeni bir öğe görünümü oluşturulduğunda çağrılır
    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext()); // Layout inflater oluştur
        PaymentRowBinding itemBinding = PaymentRowBinding.inflate(layoutInflater, parent, false); // Öğe bağlama nesnesini oluştur
        return new PaymentViewHolder(itemBinding); // Öğe görünümünü döndür
    }

    // Öğe görünümü bağlandığında çağrılır
    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        sharedViewModel.getPaymentSelect().observeForever(isSelect -> {
            if (isSelect) { // Ödeme yöntemi seçildiyse
                holder.binding.paymentRow.setOnClickListener(v -> { // Ödeme yöntemi satırına tıklanınca
                    bundle.putString("cardNumber", paymentMethods.get(position).getKartNumarasi()); // Kart numarasını veri dizisine ekle
                    bundle.putInt("cardId", paymentMethods.get(position).getPaymentMethodId()); // Kart kimliğini veri dizisine ekle
                    if (listener != null) {
                        listener.onPaymentItemSelected(bundle); // Ödeme yöntemi seçildiğinde dinleyiciye bildir
                    }
                });
            }
        });

        holder.binding.kartBaslik.setText("Card " + (position + 1)); // Kart başlığını ayarla
        Payment paymentMethod = paymentMethods.get(position); // Pozisyona göre ödeme yöntemini al
        holder.bind(paymentMethod); // Görünümü bağla
    }

    // Toplam öğe sayısını döndürür
    @Override
    public int getItemCount() {
        return paymentMethods.size(); // Ödeme yöntemi sayısını döndür
    }

    // Ödeme yöntemi görünümünü tutan iç sınıf
    static class PaymentViewHolder extends RecyclerView.ViewHolder {
        private final PaymentRowBinding binding; // Ödeme yöntemi satırını bağlayan nesne

        // Kurucu metot
        PaymentViewHolder(PaymentRowBinding binding) {
            super(binding.getRoot()); // Üst sınıfı başlat
            this.binding = binding; // Bağlama nesnesini ata
        }

        // Kart numarasını temizleyip yeniden formatlayan metot
        private String formatCardNumber(String cardNumber) {
            // Kart numarasını temizleyip yeniden formatla
            cardNumber = cardNumber.replaceAll("\\s+", ""); // Tüm boşlukları kaldır
            StringBuilder formatted = new StringBuilder();
            for (int i = 0; i < cardNumber.length(); i++) {
                if (i > 0 && i % 4 == 0) {
                    formatted.append(" "); // Her dört karakterden sonra bir boşluk ekle
                }
                formatted.append(cardNumber.charAt(i)); // Karakteri ekle
            }
            return formatted.toString(); // Formatlanmış kart numarasını döndür
        }

        // Kart numarasını maskeler
        private String maskCardNumber(String cardNumber) {
            if (cardNumber == null || cardNumber.length() < 4) {
                return "**** **** **** ****"; // Kart numarası 4 karakterden azsa yıldızlarla doldur
            }
            // İlk 6 ve son 4 hane hariç diğer haneleri maskeler
            String firstSix = cardNumber.length() > 6 ? cardNumber.substring(0, 6) : cardNumber;
            String lastFour = cardNumber.length() > 4 ? cardNumber.substring(cardNumber.length() - 4) : "";
            // İlk 6 ve son 4 hane arasındaki karakterleri * ile değiştir
            return formatCardNumber(firstSix + "******" + lastFour); // Formatlanmış kart numarasını döndür
        }

        // Ödeme yöntemi verisini görünüme bağlayan metot
        void bind(Payment paymentMethod) {
            String maskedCardNumber = maskCardNumber(paymentMethod.getKartNumarasi()); // Kart numarasını maskeler
            binding.kartNumarasi.setText(maskedCardNumber); // Maskelenmiş kart numarasını ayarla
            binding.cvv.setText("***"); // CVV genellikle gizlenir
            binding.sonKullanma.setText(paymentMethod.getSonKullanma()); // Son kullanma tarihini ayarla
        }
    }
}
