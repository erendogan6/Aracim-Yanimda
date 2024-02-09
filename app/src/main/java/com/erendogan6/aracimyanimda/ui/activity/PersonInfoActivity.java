package com.erendogan6.aracimyanimda.ui.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.net.ParseException;

import com.erendogan6.aracimyanimda.api.RetrofitClientInstance;
import com.erendogan6.aracimyanimda.api.UserApiService;
import com.erendogan6.aracimyanimda.databinding.ActivityPersonInfoBinding;
import com.erendogan6.aracimyanimda.model.Register;
import com.erendogan6.aracimyanimda.util.InputValidator;

import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Kişisel bilgilerin girildiği ve kayıt işleminin tamamlandığı aktivite sınıfı.
public class PersonInfoActivity extends AppCompatActivity {
    // View Binding kullanılarak layout ile kod arasında güvenli bir bağlantı kurulur.
    private ActivityPersonInfoBinding binding;

    // Kullanıcıdan alınan kişisel bilgiler için değişkenler.
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    // Kullanıcının sözleşmeyi kabul edip etmediğini tutan boolean değişken.
    private boolean sozlesme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Layout'un inflate edilmesi ve aktivitenin view olarak ayarlanması.
        binding = ActivityPersonInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Intent ile gönderilen verilerin alınması.
        initializeIntentData();
        // Doğum tarihi seçici için dialog penceresinin ayarlanması.
        setupBirthDatePicker();
        // Kayıt tamamlama butonuna tıklama olayının atanması.
        binding.buttonLogin.setOnClickListener(v -> completeRegistration());
    }

    // Intent'ten e-posta ve şifre bilgilerini almak için kullanılan metod.
    private void initializeIntentData() {
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
    }

    // Kullanıcıdan doğum tarihini seçmesini sağlayan DatePickerDialog'un ayarlanması.
    private void setupBirthDatePicker() {
        binding.editTextBirthDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            // DatePickerDialog'un oluşturulması ve gösterilmesi.
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this, this::onDateSet, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            // Kullanıcıların 18 yaşından küçük olmamalarını garantileyen maksimum tarih sınırı.
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 568025136000L);
            datePickerDialog.show();
        });
    }

    // Kullanıcının seçtiği doğum tarihini ayarlayan ve 18 yaş kontrolü yapan metod.
    private void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, dayOfMonth);
        if (isAdult(selectedDate)) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            binding.editTextBirthDate.setText(sdf.format(selectedDate.getTime()));
        } else {
            showToast("18 yaşından küçükler kayıt olamaz.");
        }
    }

    // Kullanıcının 18 yaşından büyük olup olmadığını kontrol eden metod.
    private boolean isAdult(Calendar birthDate) {
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age >= 18;
    }

    // Kullanıcının kayıt işlemini gerçekleştiren metod.
    private void performRegistration(String date){
        UserApiService apiService = RetrofitClientInstance.getRetrofitInstance().create(UserApiService.class);
        Register request = new Register(email, password, firstName, lastName, phoneNumber, sozlesme);
        Call<Void> call = apiService.register(request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    showToast("Kayıt başarılı.");
                    // Kayıt başarılıysa LoginActivity'e yönlendirme.
                    Intent intent = new Intent(PersonInfoActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showToast("Bağlantı hatası: " + t.getMessage());
                Log.e("PersonInfoActivity", "Error: ", t);
            }
        });
    }

    // Kayıt işlemini tamamlamadan önce kullanıcı girişlerinin doğruluğunu kontrol eden metod.
    private void completeRegistration() {
        if (!validateInputs()) return;

        // Doğum tarihini SQL formatına dönüştürme.
        SimpleDateFormat sdfInput = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        try {
            Date birthDate = sdfInput.parse(binding.editTextBirthDate.getText().toString());
            String formattedDate = sdfOutput.format(birthDate);
            performRegistration(formattedDate);
        } catch (java.text.ParseException e) {
            showToast("Doğum tarihi formatı yanlış.");
        }
    }

    // Kullanıcı girişlerinin geçerliliğini kontrol eden metod.
    private boolean validateInputs() {
        // Kullanıcıdan alınan bilgileri trim() metodu ile boşluklardan arındırır.
        firstName = binding.editTextName.getText().toString().trim();
        lastName = binding.editTextSurname.getText().toString().trim();
        phoneNumber = binding.editTextPhone.getText().toString().trim();
        String date = binding.editTextBirthDate.getText().toString().trim();

        // SQL Injection'u önlemek için özel karakterleri temizler.
        firstName= InputValidator.SQL_InjectionInput(firstName);
        lastName = InputValidator.SQL_InjectionInput(lastName);
        phoneNumber = InputValidator.SQL_InjectionInput(phoneNumber);

        // Kullanıcının sözleşmeyi kabul edip etmediğini kontrol eder.
        sozlesme = binding.checkBox.isChecked();

        boolean isValid = true; // Tüm girişlerin geçerli olup olmadığını tutar.

        // Ad alanının boş olup olmadığını kontrol eder.
        if (firstName.isEmpty()) {
            binding.editTextName.setError("Ad alanı boş bırakılamaz.");
            isValid = false;
        } else if (!InputValidator.validateLength(firstName, 1, 250)) {
            // Adın uzunluğunu kontrol eder.
            binding.editTextName.setError("Ad çok uzun.");
            isValid = false;
        } else {
            binding.editTextName.setError(null);
        }

        // Soyad alanının boş olup olmadığını kontrol eder.
        if (lastName.isEmpty()) {
            binding.editTextSurname.setError("Soyad alanı boş bırakılamaz.");
            isValid = false;
        } else if (!InputValidator.validateLength(lastName, 1, 250)) {
            // Soyadın uzunluğunu kontrol eder.
            binding.editTextSurname.setError("Soyad çok uzun.");
            isValid = false;
        } else {
            binding.editTextSurname.setError(null);
        }

        // Telefon numarasının boş olup olmadığını kontrol eder.
        if (phoneNumber.isEmpty()) {
            binding.editTextPhone.setError("Telefon numarası boş bırakılamaz.");
            isValid = false;
        } else if (!InputValidator.validateLength(phoneNumber, 1, 250)) {
            // Telefon numarasının uzunluğunu kontrol eder.
            binding.editTextPhone.setError("Telefon numarası çok uzun.");
            isValid = false;
        } else {
            binding.editTextPhone.setError(null);
        }

        // Doğum tarihinin boş olup olmadığını ve geçerli bir tarih olup olmadığını kontrol eder.
        if (date.isEmpty()) {
            binding.editTextBirthDate.setError("Doğum tarihi boş bırakılamaz.");
            isValid = false;
        }  else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Calendar birthDate = Calendar.getInstance();
            try {
                birthDate.setTime(sdf.parse(date));
                Calendar today = Calendar.getInstance();
                int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
                if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
                    age--;
                }
                if (age < 18) {
                    binding.editTextBirthDate.setError("18 yaşından küçükler kayıt olamaz.");
                    isValid = false;
                } else {
                    binding.editTextBirthDate.setError(null);
                }
            } catch (ParseException e) {
                binding.editTextBirthDate.setError("Doğum tarihi geçersiz.");
                isValid = false;
            } catch (java.text.ParseException e) {
                throw new RuntimeException(e);
            }
        }

        // Sözleşmenin kabul edilip edilmediğini kontrol eder.
        if (!sozlesme) {
            showToast("Sözleşmeyi kabul etmelisiniz.");
            isValid = false;
        }

        return isValid; // Tüm kontrollerden geçerse true, aksi halde false döndürür.
    }

    // Kullanıcıya mesaj göstermek için kullanılan yardımcı metod.
    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_LONG).show());
    }
}
