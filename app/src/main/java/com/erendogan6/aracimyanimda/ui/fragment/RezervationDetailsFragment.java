package com.erendogan6.aracimyanimda.ui.fragment;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.erendogan6.aracimyanimda.api.RetrofitClientInstance;
import com.erendogan6.aracimyanimda.api.UserApiService;
import com.erendogan6.aracimyanimda.databinding.FragmentRezervationDetailsBinding;
import com.erendogan6.aracimyanimda.model.Rezervation;
import com.erendogan6.aracimyanimda.model.User;
import com.erendogan6.aracimyanimda.model.Vehicle;
import com.erendogan6.aracimyanimda.ui.activity.MainActivity;
import com.erendogan6.aracimyanimda.util.UserManager;

import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RezervationDetailsFragment extends DialogFragment {
    private static final String ERROR_MESSAGE_PREFIX = "Bir hata oluştu: ";
    private static final String TAG = "RezervationDetailsFragment";
    private FragmentRezervationDetailsBinding binding;
    private int userId;
    private int kartId;
    private String vehicleId;
    private String startDate;
    private String endDate;

    // Görünüm oluşturulduğunda
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRezervationDetailsBinding.inflate(inflater, container, false); // Bağlama nesnesini oluştur
        return binding.getRoot(); // Kök görünümü döndür
    }

    // Görünüm oluşturulduğunda veya yeniden oluşturulduğunda
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments(); // Argümanları al
        if (bundle != null) {
            setDataFromBundle(bundle); // Argümanlardan verileri al ve görsel öğelere yerleştir
            getUserInfo(); // Kullanıcı bilgilerini al ve görsel öğelere yerleştir
            binding.complete.setOnClickListener(v -> completeRezervation()); // Rezervasyonu tamamla butonuna tıklanınca işlemi başlat
        }
    }

    // Kullanıcı bilgilerini al
    private void getUserInfo() {
        User user = UserManager.getInstance().getUser(); // Kullanıcıyı al
        userId = user.getUserId(); // Kullanıcı kimliğini al
        binding.tvUserName.setText(user.getAd() + " " + user.getSoyad()); // Kullanıcı adını ve soyadını görsel öğeye yerleştir
        binding.tvUserEmail.setText(user.getePosta()); // Kullanıcı e-postasını görsel öğeye yerleştir
        binding.tvUserPhone.setText(user.getTelefon()); // Kullanıcı telefon numarasını görsel öğeye yerleştir
    }

    // Kart numarasını biçimlendir
    private String formatCardNumber(String cardNumber) {
        cardNumber = cardNumber.replaceAll("\\s+", ""); // Boşlukları kaldır
        StringBuilder formatted = new StringBuilder(); // Biçimlendirilmiş kart numarasını tutacak bir dize oluştur
        for (int i = 0; i < cardNumber.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                formatted.append(" "); // Her dört karakterden sonra bir boşluk ekle
            }
            formatted.append(cardNumber.charAt(i)); // Karakterleri dizeye ekle
        }
        return formatted.toString(); // Biçimlendirilmiş kart numarasını döndür
    }

    // Kart numarasını maskela
    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "**** **** **** ****"; // Kart numarası eksikse veya boşsa, maskelenmiş bir numara döndür
        }
        String firstSix = cardNumber.length() > 6 ? cardNumber.substring(0, 6) : cardNumber; // İlk altı karakteri al
        String lastFour = cardNumber.length() > 4 ? cardNumber.substring(cardNumber.length() - 4) : ""; // Son dört karakteri al
        return formatCardNumber(firstSix + "******" + lastFour); // Maskelenmiş kart numarasını döndür
    }

    // Görsel öğeye drawable yükleyin
    private void loadDrawableToImageView(String drawableName, ImageView imageView) {
        int drawableResourceId = getResources().getIdentifier(drawableName, "drawable", getActivity().getPackageName()); // Drawable kaynağını al
        if (drawableResourceId != 0) {
            imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), drawableResourceId)); // Drawable'ı görsel öğeye yerleştir
        }
    }

    // Hata mesajını günlüğe kaydet ve Toast mesajı göster
    private void logError(Exception e) {
        Log.e(TAG, ERROR_MESSAGE_PREFIX, e); // Hata mesajını günlüğe kaydet
        showToast(ERROR_MESSAGE_PREFIX + e.getMessage()); // Hata mesajını Toast mesajı olarak göster
    }

    // Toast mesajı göster (ana iş parçacığında çalışır)
    private void showToast(String message) {
        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show()); // Toast mesajını göster
    }

    // Argümanlardan verileri al ve görsel öğelere yerleştir
    private void setDataFromBundle(Bundle bundle) {
        if (bundle != null) {
            Vehicle vehicle = bundle.getParcelable("car");
            vehicleId = vehicle.getVehicleId(); // Araç kimliğini al, yoksa varsayılan değeri kullan
            String marka = vehicle.getMarka(); // Markayı al, yoksa "N/A" kullan
            String model = vehicle.getModel(); // Modeli al, yoksa "N/A" kullan
            String adres = vehicle.getAdres(); // Adresi al, yoksa "N/A" kullan
            String yakitTipi = vehicle.getYakitTipi(); // Yakıt türünü al, yoksa "N/A" kullan
            long kiralamaBaslangicMS = vehicle.getKiralamaBaslangic(); // Başlangıç tarihini al, yoksa 0 kullan
            long kiralamaBitisMS = vehicle.getKiralamaBitis(); // Bitiş tarihini al, yoksa 0 kullan
            String kartNumarasi = bundle.getString("cardNumber", "0"); // Kart numarasını al, yoksa "0" kullan
            kartId = bundle.getInt("cardId", 0); // Kart kimliğini al, yoksa 0 kullan

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // Tarih formatlayıcı oluştur
            startDate = kiralamaBaslangicMS == 0L ? "" : formatter.format(new Date(kiralamaBaslangicMS)); // Başlangıç tarihini biçimlendir
            endDate = kiralamaBitisMS == 0L ? "" : formatter.format(new Date(kiralamaBitisMS)); // Bitiş tarihini biçimlendir

            binding.vehicleMarka.setText(marka + " " + model); // Araç marka ve modelini görsel öğeye yerleştir
            binding.tvCarYear.setText(model); // Araç yılını görsel öğeye yerleştir
            binding.tvCarAddress.setText(adres); // Araç adresini görsel öğeye yerleştir
            binding.tvCarFuelType.setText(yakitTipi); // Araç yakıt türünü görsel öğeye yerleştir
            binding.tvStartDate.setText(startDate); // Başlangıç tarihini görsel öğeye yerleştir
            binding.tvEndDate.setText(endDate); // Bitiş tarihini görsel öğeye yerleştir
            binding.tvCardNumber.setText(maskCardNumber(kartNumarasi)); // Kart numarasını görsel öğeye yerleştir

            loadDrawableToImageView("cropped_" + marka.toLowerCase() + "_logo", binding.vehicleLogo); // Araç logosunu yükle
            loadDrawableToImageView("cropped_" + marka.toLowerCase() + "_car", binding.vehicleResim); // Araç resmini yükle
        }
    }

    // Rezervasyonu tamamla
    private void completeRezervation() {
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance(); // Retrofit örneğini al
        UserApiService service = retrofit.create(UserApiService.class); // Kullanıcı API hizmetini al
        Rezervation newReservation = new Rezervation( // Yeni bir rezervasyon oluştur
                Integer.parseInt(vehicleId), // Araç kimliğini ata
                userId, // Kullanıcı kimliğini ata
                startDate, // Başlangıç tarihini ata
                endDate, // Bitiş tarihini ata
                "Rezerve"); // Durumu "Rezerve" olarak ata
        Call<Void> call = service.createReservation(kartId, newReservation); // Rezervasyon oluşturma isteği oluştur
        call.enqueue(new Callback<Void>() { // İsteği sıraya al
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) { // Yanıt alındığında
                if (response.isSuccessful()) { // Başarılı yanıt alındıysa
                    showToast("Rezervasyon Başarılı"); // Başarılı mesajını göster
                    navigateToMainActivity(); // Ana ekrana yönlendir
                } else { // Başarısız yanıt alındıysa
                    showToast("Rezervasyon Başarısız"); // Başarısız mesajını göster
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) { // İstek başarısız olduğunda
                logError(new Exception(t)); // Hata mesajını günlüğe kaydet
            }
        });
    }

    // Ana ekrana yönlendir
    private void navigateToMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class); // Ana ekrana yönlendirecek bir intent oluştur
        startActivity(intent); // Ana ekrana yönlendir
        getActivity().finish(); // Şuanki aktiviteyi sonlandır
    }

    // Fragment başladığında
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) { // Eğer dialog mevcutsa
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes(); // Pencere özelliklerini al
            params.gravity = Gravity.BOTTOM; // Pencereyi alt tarafta konumlandır
            params.width = ViewGroup.LayoutParams.MATCH_PARENT; // Pencere genişliğini ekrana göre ayarla
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT; // Pencere yüksekliğini içeriğe göre ayarla
            getDialog().getWindow().setAttributes(params); // Pencere özelliklerini ayarla
        }
    }
}
