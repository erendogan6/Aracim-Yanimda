package com.example.aracimyanimda.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aracimyanimda.MainActivity;
import com.example.aracimyanimda.R;
import com.example.aracimyanimda.api.RetrofitClientInstance;
import com.example.aracimyanimda.api.UserApiService;
import com.example.aracimyanimda.databinding.ActivityLoginBinding;
import com.example.aracimyanimda.model.Login;
import com.example.aracimyanimda.model.User;
import com.example.aracimyanimda.util.InputValidator;
import com.example.aracimyanimda.util.SharedPreferencesUtils;
import com.example.aracimyanimda.util.UserManager;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    // Logcat'te loglama yaparken kullanılacak etiket.
    private final String TAG = "LoginActivity";
    // Activity için view binding nesnesi, layout ile kod arasında güvenli bir şekilde etkileşim sağlar.
    private ActivityLoginBinding binding;
    // Uygulama genelinde kullanıcı tercihlerini saklamak için SharedPreferences nesnesi.
    private SharedPreferences sharedPreferences;

    // Verilen email adresinin geçerli bir email formatında olup olmadığını kontrol eder.
    private static boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Activity başlatıldığında çağrılan metod.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        getSharedPreferences();
        checkCredentialsAndLogin();
    }

    // Şifrelenmiş SharedPreferences nesnesini almak için kullanılan metod.
    private void getSharedPreferences() {
        try {
            sharedPreferences = SharedPreferencesUtils.getEncryptedSharedPreferences(getApplicationContext());
        } catch (GeneralSecurityException | IOException e) {
            logError(e);
        }
    }

    // Logcat'e hata mesajı yazdırır ve kullanıcıya bir toast mesajı gösterir.
    private void logError(Exception e) {
        Log.e(TAG, "Error: ", e);
        showToast("Bir hata oluştu, lütfen daha sonra tekrar deneyin. " + e.getLocalizedMessage());
    }

    // Kullanıcıya toast mesajı göstermek için kullanılan metod.
    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_LONG).show());
    }

    // Kaydedilmiş kullanıcı bilgilerini kontrol eder ve otomatik giriş yapar, yoksa giriş ekranını yükler.
    private void checkCredentialsAndLogin() {
        String email = sharedPreferences.getString("user_mail", "");
        String password = sharedPreferences.getString("user_password", "");

        if (!email.isEmpty() && !password.isEmpty()) {
            girisIstek(email, password);
        } else {
            loadLoginScreen();
        }
    }

    // Giriş ekranının layout'unu yükler.
    private void loadLoginScreen() {
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    // Üye ol butonuna tıklandığında çağrılan metod, Kayıt olma ekranına yönlendirir.
    public void uyeOl(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    // Kullanıcı girişi için API isteğini gönderir ve yanıtı işler.
    private void girisIstek(String email, String password) {
        UserApiService apiService = RetrofitClientInstance.getRetrofitInstance().create(UserApiService.class);
        Call<User> call = apiService.login(new Login(email, password));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Giriş başarılı olduğunda kullanıcı bilgileriyle işlemleri yapar.
                    User user = response.body();
                    UserManager.getInstance().setUser(user);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_mail", user.getePosta());
                    editor.putString("user_password", password);
                    editor.apply();
                    showToast("Giriş Başarılı");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Giriş başarısızsa kullanıcıyı bilgilendirir.
                    loadLoginScreen();
                    try {
                        clearSharedPreferences();
                        showToast("Giriş başarısız: Yanlış e-posta veya şifre.");
                    } catch (RuntimeException e) {
                        logError(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Ağ hatası gibi durumlarda kullanıcıyı bilgilendirir.
                loadLoginScreen();
                logError(new Exception(t));
            }
        });
    }

    // SharedPreferences'daki verileri temizler.
    private void clearSharedPreferences() {
        try {
            sharedPreferences.edit().clear().apply();
        } catch (RuntimeException e) {
            logError(e);
        }
    }

    // Giriş yap butonuna tıklandığında çağrılan metod, kullanıcı girişi için gerekli kontrolleri yapar.
    public void girisYap(View view) {
        String email = binding.editTextEmail.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString().trim();

        // SQL injection önlemek için girdi kontrolü yapılır.
        email = InputValidator.SQL_InjectionInput(email);
        password = InputValidator.SQL_InjectionInput(password);

        // E-posta ve şifre uzunluğu kontrolü yapılır.
        if (!InputValidator.validateLength(email, 1, 250)) {
            binding.editTextEmail.setError("Geçerli bir e-posta adresi girin");
            return;
        }
        if (!InputValidator.validateLength(password, 6, 250)) {
            binding.editTextPassword.setError("Şifre en az 6 karakter olmalıdır");
            return;
        }
        // E-posta formatı kontrol edilir.
        if (!isValidEmail(email)) {
            binding.editTextEmail.setError("Geçerli bir e-posta adresi girin");
            return;
        }
        // Tüm kontroller başarılıysa giriş isteği gönderilir.
        girisIstek(email, password);
    }
}
