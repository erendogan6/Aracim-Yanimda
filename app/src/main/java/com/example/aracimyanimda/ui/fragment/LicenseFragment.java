package com.example.aracimyanimda.ui.fragment;

import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.aracimyanimda.api.RetrofitClientInstance;
import com.example.aracimyanimda.api.UserApiService;
import com.example.aracimyanimda.databinding.FragmentLicenseBinding;
import com.example.aracimyanimda.model.License;
import com.example.aracimyanimda.util.UserManager;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LicenseFragment extends DialogFragment {
    // Log mesajları için etiket.
    private final String TAG = "LicenseFragment";
    // Kullanıcının seçtiği tarih bilgilerini tutmak için Calendar nesnesi.
    private final Calendar myCalendar = Calendar.getInstance();
    // View Binding ile layout bağlantısı için değişken.
    private FragmentLicenseBinding binding;
    // Kullanıcı ID'sini saklamak için değişken.
    private int userId;

    // Fragment'in görünümünün oluşturulduğu yer.
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // View Binding ile fragment layout'unun inflate edilmesi.
        binding = FragmentLicenseBinding.inflate(inflater, container, false);
        // Başlangıçta görünümü gizle.
        binding.getRoot().setVisibility(View.INVISIBLE);
        return binding.getRoot();
    }

    // Görünüm oluşturulduktan sonra çağrılan metod.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Kullanıcı ID'sini al.
        getUserId();
        // Kullanıcı arayüzünü ayarla.
        setupUI();
        // Lisans bilgilerini getir.
        fetchLicenseInfo(userId);
    }

    // Kullanıcı arayüz elemanlarının ayarlandığı yer.
    private void setupUI() {
        // Tarih seçici diyalogun gösterilmesi.
        binding.editTextExpirationDate.setOnClickListener(v -> showDatePickerDialog());
        // Lisans bilgilerini kaydet butonuna tıklama olayı.
        binding.buttonUpdateLicense.setOnClickListener(v -> saveLicenseInfo());
    }

    // Kullanıcı ID'sini al.
    private void getUserId() {
        userId = UserManager.getInstance().getUser().getUserId();
    }

    // Tarih seçici diyalogu göster.
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), this::onDateSet, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000L);
        datePickerDialog.show();
    }

    // Tarih seçildiğinde çağrılan metod.
    private void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myCalendar.set(year, month, dayOfMonth);
        // Seçilen tarihi güncelle.
        updateLabel();
    }

    // Seçilen tarihi editText'e yazdır.
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; // Tarih formatı.
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        binding.editTextExpirationDate.setText(sdf.format(myCalendar.getTime()));
    }

    // Hata mesajı loglama.
    private void logError(Exception e) {
        Log.e(TAG, "Error: ", e);
        showToast("Bir hata oluştu, lütfen daha sonra tekrar deneyin " + e.getLocalizedMessage());
    }

    // Kullanıcıya toast mesajı gösterme.
    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    // API aracılığı ile lisans bilgilerini getirme.
    private void fetchLicenseInfo(int userId) {
        UserApiService apiService = RetrofitClientInstance.getRetrofitInstance().create(UserApiService.class);
        apiService.getLicense(userId).enqueue(new Callback<License>() {
            @Override
            public void onResponse(Call<License> call, Response<License> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Gelen lisans bilgileri ile UI güncelle.
                    updateUIWithLicenseInfo(response.body());
                } else {
                    Log.e(TAG, "Ehliyet Hatası: " + response.raw());
                    // Hata durumunda görünümü göster.
                    binding.getRoot().setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<License> call, Throwable t) {
                // Hata loglama.
                logError(new Exception(t));
            }
        });
    }

    // Gelen lisans bilgileri ile UI güncelleme.
    private void updateUIWithLicenseInfo(License license) {
        // Güncelle butonunu gizle.
        binding.buttonUpdateLicense.setVisibility(View.GONE);
        // Gelen bilgileri ilgili alanlara yerleştir.
        binding.editTextExpirationDate.setText(license.getSonGecerlilikTarihi());
        binding.editTextExpirationDate.setEnabled(false);
        binding.checkboxClassB.setChecked(true);
        binding.checkboxClassB.setEnabled(false);
        binding.editTextLicenseNumber.setText(license.getEhliyetNo());
        binding.editTextLicenseNumber.setEnabled(false);
        // UI'ı görünür yap.
        binding.getRoot().setVisibility(View.VISIBLE);
    }

    // Lisans bilgilerini kaydetme.
    private void saveLicenseInfo() {
        // Kullanıcı girişlerini al.
        String licenseNumber = binding.editTextLicenseNumber.getText().toString();
        String expirationDate = binding.editTextExpirationDate.getText().toString();
        boolean hasClassB = binding.checkboxClassB.isChecked();

        // Girişlerin doğruluğunu kontrol et.
        if (!validateInputs(licenseNumber, expirationDate, hasClassB)) {
            showToast("Lütfen tüm alanları doğru bir şekilde doldurun");
            return;
        }

        // Lisans bilgileri ile yeni bir istek oluştur.
        License license = new License("B", licenseNumber, expirationDate, userId);
        UserApiService apiService = RetrofitClientInstance.getRetrofitInstance().create(UserApiService.class);
        Call<Void> call = apiService.createLicense(license);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    showToast("Ehliyet bilgileri başarıyla güncellendi.");
                    // Bilgiler güncellendikten sonra lisans bilgilerini tekrar getir.
                    fetchLicenseInfo(userId);
                } else {
                    showToast("Ehliyet bilgileri güncellenirken bir sorun oluştu.");
                    Log.e(TAG, "Ehliyet güncelleme hatası: " + response.raw());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Hata loglama.
                logError(new Exception(t));
            }
        });
    }

    // Girişlerin doğruluğunu kontrol eden metod.
    private boolean validateInputs(String licenseNumber, String expirationDate, boolean hasClassB) {
        // Giriş alanlarının boş olmaması ve sınıf B ehliyetin seçilmiş olması gerekmektedir.
        return !licenseNumber.isEmpty() && !expirationDate.isEmpty() && hasClassB;
    }

    // Fragment'in View'ı yok edildiğinde çağrılan metod.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // View Binding'i null yaparak bellek sızıntısını önle.
        binding = null;
    }

    // Dialog'un başlatıldığı metod.
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            // Dialog penceresinin özelliklerini ayarla.
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.gravity = Gravity.BOTTOM; // Pencerenin altta gösterilmesi.
            params.width = ViewGroup.LayoutParams.MATCH_PARENT; // Genişliğin ekran genişliğine uyarlanması.
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT; // Yüksekliğin içerik boyutuna göre ayarlanması.
            getDialog().getWindow().setAttributes(params);
        }
    }
}

