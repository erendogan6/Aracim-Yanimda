package com.example.aracimyanimda.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aracimyanimda.api.RetrofitClientInstance;
import com.example.aracimyanimda.api.UserApiService;
import com.example.aracimyanimda.databinding.ActivityRegisterBinding;
import com.example.aracimyanimda.model.RegisterControl;
import com.example.aracimyanimda.util.InputValidator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    // Activity için view binding nesnesi, layout ile kod arasında güvenli bir şekilde etkileşim sağlar.
    private ActivityRegisterBinding binding;

    // Activity başlatıldığında çağrılan metod.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Layout'u view binding ile inflate eder ve setContentView ile ekranı bu view ile ayarlar.
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    // Kullanıcı "Kaydol" butonuna bastığında çağrılan metod.
    public void checkEmailAndRegister(View view) {
        // Kullanıcı girişlerinden e-posta ve şifreyi alır ve boşlukları temizler.
        String email = binding.editTextEmail.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString().trim();

        // SQL injection saldırılarını önlemek için girdileri filtreler.
        email = InputValidator.SQL_InjectionInput(email);
        password = InputValidator.SQL_InjectionInput(password);

        // Girdi geçerliliğini kontrol eden bir bayrak.
        boolean isValid = true;

        // E-posta adresi geçerlilik kontrolleri.
        if (email.isEmpty()) {
            binding.editTextEmail.setError("E-posta adresi boş bırakılamaz.");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editTextEmail.setError("Geçersiz e-posta adresi.");
            isValid = false;
        } else if (!InputValidator.validateLength(email,1,250)) {
            binding.editTextEmail.setError("E-posta adresi çok uzun");
            isValid = false;
        } else {
            binding.editTextEmail.setError(null);
        }

        // Şifre geçerlilik kontrolleri.
        if (password.isEmpty()) {
            binding.editTextPassword.setError("Şifre boş bırakılamaz.");
            isValid = false;
        } else if (password.length() < 6) {
            binding.editTextPassword.setError("Şifre en az 6 karakter olmalıdır.");
            isValid = false;
        } else {
            binding.editTextPassword.setError(null);
        }

        // Geçerli değilse, daha fazla işlem yapmadan döner.
        if (!isValid) {
            return;
        }

        // E-posta adresinin zaten kullanılıp kullanılmadığını kontrol eden API çağrısı.
        UserApiService apiService = RetrofitClientInstance.getRetrofitInstance().create(UserApiService.class);
        Call<Boolean> checkEmailCall = apiService.checkEmail(new RegisterControl(email));
        String finalEmail = email;
        String finalPassword = password;
        checkEmailCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body()) {
                    // Eğer e-posta zaten kullanımdaysa, kullanıcıyı bilgilendirir.
                    Toast.makeText(RegisterActivity.this, "E-posta adresi zaten kullanımda.", Toast.LENGTH_LONG).show();
                } else {
                    // E-posta kullanımda değilse, kayıt işlemine devam eder.
                    registerUser(finalEmail, finalPassword);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                // API çağrısında bir hata oluşursa, hata mesajını yazdırır.
                System.out.println(t.getLocalizedMessage());
            }
        });
    }

    // Kullanıcının kişisel bilgi ekranına yönlendirildiği ve e-posta ile şifresinin aktarıldığı yardımcı metod.
    private void registerUser(String email, String password) {
        Intent intent = new Intent(this, PersonInfoActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        startActivity(intent);
    }
}

