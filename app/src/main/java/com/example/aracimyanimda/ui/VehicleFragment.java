package com.example.aracimyanimda.ui;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import com.example.aracimyanimda.databinding.FragmentVecihleBinding;
import com.example.aracimyanimda.model.Vehicle;

public class VehicleFragment extends DialogFragment {
    private FragmentVecihleBinding binding;

    // Görünüm oluşturulduğunda
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentVecihleBinding.inflate(inflater, container, false); // Bağlama nesnesini oluştur
        Bundle bundle = getArguments(); // Geçerli veri paketini al
        if (bundle != null) {
            setVehicleDataFromBundle(bundle); // Verileri paketten al ve görünüme yükle
            binding.btnKirala.setOnClickListener(v -> showReservationDateDialog()); // Kiralama düğmesine tıklanınca rezervasyon tarihini göster
        }
        return binding.getRoot(); // Kök görünümü döndür
    }

    // Veri paketinden araç verilerini görünüme yükle
    private void setVehicleDataFromBundle(Bundle bundle) {
        // Araç verilerini görünüme yükle
        Vehicle vehicle = (Vehicle) bundle.getParcelable("car");
        binding.vehicleMarka.setText(vehicle.getMarka());
        binding.vehicleModel.setText(vehicle.getModel());
        binding.vehicleYil.setText(vehicle.getYil());
        binding.vehicleYakitTipi.setText(vehicle.getYakitTipi());
        binding.vehicleYakitSeviyesi.setText(vehicle.getYakitDurumu());
        binding.vehicleGunlukFiyat.setText("Günlük: " + getIntFromString(vehicle.getFiyatGunluk()) + "₺");
        binding.vehicleDakikaFiyat.setText("Saatlik: " + getIntFromString(vehicle.getFiyatDakika()) + "₺");
        binding.vehicleAdres.setText(vehicle.getAdres());

        // Araç logosunu ve resmini görünüme yükle
        loadDrawableToImageView("cropped_" + vehicle.getMarka().toLowerCase() + "_logo", binding.vehicleLogo);
        loadDrawableToImageView("cropped_" + vehicle.getMarka().toLowerCase() + "_car", binding.vehicleResim);
    }

    // Drawable kaynağını ImageView'e yükle
    private void loadDrawableToImageView(String drawableName, ImageView imageView) {
        int drawableResourceId = getResources().getIdentifier(drawableName, "drawable", getActivity().getPackageName()); // Drawable kimliğini al
        if (drawableResourceId != 0) {
            imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), drawableResourceId)); // Drawable'ı ImageView'e yükle
        }
    }

    // Metin değerini int'e dönüştür
    private int getIntFromString(String value) {
        try {
            return (int) Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // Fragment başladığında
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes(); // Pencere özelliklerini al
            params.gravity = Gravity.BOTTOM; // Pencereyi alt tarafta konumlandır
            params.width = ViewGroup.LayoutParams.MATCH_PARENT; // Pencere genişliğini ekrana göre ayarla
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT; // Pencere yüksekliğini içeriğe göre ayarla
            getDialog().getWindow().setAttributes(params); // Pencere özelliklerini ayarla
        }
    }

    // Rezervasyon tarihini göster
    private void showReservationDateDialog() {
        Bundle bundle = getArguments(); // Geçerli veri paketini al
        if (bundle != null) {
            RezervationDateFragment rezervationDateDialogFragment = new RezervationDateFragment(); // Rezervasyon tarih fragmentını oluştur
            rezervationDateDialogFragment.setArguments(bundle); // Verileri rezervasyon tarih fragmentına aktar
            rezervationDateDialogFragment.show(getParentFragmentManager(), "RezervationDateFragment"); // Rezervasyon tarih fragmentını göster
        }
    }
}