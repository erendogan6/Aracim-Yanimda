package com.example.aracimyanimda.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aracimyanimda.databinding.RezervationRowBinding;
import com.example.aracimyanimda.model.Rezervation;

import java.util.ArrayList;

public class RezervationAdaptor extends RecyclerView.Adapter<RezervationAdaptor.ReservationViewHolder> {
    private final ArrayList<Rezervation> reservations; // Rezervasyon verilerini tutacak dizi

    // Kurucu method
    public RezervationAdaptor(ArrayList<Rezervation> reservations) {
        this.reservations = reservations; // Verileri al
    }

    // Yeni bir görünüm oluşturulduğunda
    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext()); // Görünüm için layout inflater al
        RezervationRowBinding binding = RezervationRowBinding.inflate(layoutInflater, parent, false); // Bağlama nesnesini oluştur
        return new ReservationViewHolder(binding); // Yeni bir ReservationViewHolder nesnesi oluşturarak döndür
    }

    // Veriler görünüme bağlandığında
    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Rezervation reservation = reservations.get(position); // Belirli bir pozisyondaki rezervasyonu al
        holder.bind(reservation); // ViewHolder'ı belirtilen veriyle bağla
    }

    // Veri kümesinin boyutunu döndür
    @Override
    public int getItemCount() {
        return reservations.size(); // Rezervasyonların sayısını döndür
    }

    // Görünüm tutucu sınıfı
    public class ReservationViewHolder extends RecyclerView.ViewHolder {
        private final RezervationRowBinding binding; // Bağlama nesnesi

        // Kurucu method
        ReservationViewHolder(RezervationRowBinding binding) {
            super(binding.getRoot()); // Üst sınıfın kurucu methodunu çağır
            this.binding = binding; // Bağlama nesnesini ata
        }

        // Verileri görünüme bağla
        void bind(Rezervation reservation) {
            binding.tvStartDate.setText(reservation.getBaslangicTarihi()); // Başlangıç tarihini görünüme ata
            binding.tvEndDate.setText(reservation.getBitisTarihi()); // Bitiş tarihini görünüme ata
            String fiyat = String.valueOf(reservation.getMiktar()); // Fiyatı stringe çevir
            binding.tvCost.setText(fiyat + "₺"); // Fiyatı görünüme ata
            binding.tvLicensePlate.setText(reservation.getPlaka()); // Plakayı görünüme ata
            binding.tvName.setText(reservation.getAracIsmi()); // Araç ismini görünüme ata
            binding.tvReservationStatus.setText(reservation.getRezervasyonDurum()); // Rezervasyon durumunu görünüme ata

            String aracIsmi = reservation.getAracIsmi(); // Araç ismini al, marka ve modeli varsayılan olarak içerir
            if (aracIsmi != null && !aracIsmi.isEmpty()) { // Eğer araç ismi boş değilse
                String[] parts = aracIsmi.split(" ", 2); // Araç ismini boşluktan böl
                if (parts.length > 0) { // Eğer bölme işlemi başarılıysa
                    String marka = parts[0].toLowerCase(); // Markayı al ve küçük harfe çevir
                    loadDrawableToImageView("cropped_" + marka + "_logo", binding.imageViewCarLogo); // Marka logosunu görünüme yükle
                    loadDrawableToImageView("cropped_" + marka + "_car", binding.imageViewCarImage); // Marka arabasını görünüme yükle
                }
            }
        }

        // Drawable'ı ImageView'e yükle
        private void loadDrawableToImageView(String drawableName, ImageView imageView) {
            Context context = imageView.getContext(); // ImageView'dan bağlamayı al
            int drawableResourceId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName()); // Drawable kaynağını al
            if (drawableResourceId != 0) { // Eğer drawable kaynağı varsa
                imageView.setImageDrawable(ContextCompat.getDrawable(context, drawableResourceId)); // Drawable'ı ImageView'e yükle
            }
        }
    }
}

