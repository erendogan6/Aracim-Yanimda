package com.erendogan6.aracimyanimda.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.erendogan6.aracimyanimda.api.RetrofitClientInstance;
import com.erendogan6.aracimyanimda.api.UserApiService;
import com.erendogan6.aracimyanimda.databinding.FragmentPaymentBinding;
import com.erendogan6.aracimyanimda.model.Payment;
import com.erendogan6.aracimyanimda.model.User;
import com.erendogan6.aracimyanimda.util.UserManager;
import com.erendogan6.aracimyanimda.viewmodel.SharedViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class PaymentFragment extends DialogFragment {
    private final String TAG = "PaymentFragment";
    private int userId;
    private FragmentPaymentBinding binding;
    private String userName;
    private String cardNumber;
    private String expirationDate;
    private String cvv;
    private SharedViewModel sharedViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUserId(); // Kullanıcı kimliğini al
        setSharedViewModel(); // Paylaşılan View Model'i ayarla
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPaymentBinding.inflate(inflater, container, false); // Bağlama nesnesini oluştur
        setupUI(); // Kullanıcı arayüzünü kur
        return binding.getRoot(); // Kök görünümü döndür
    }

    // Paylaşılan View Model'i ayarla
    private void setSharedViewModel() {
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    // Kullanıcı arayüzünü kur
    private void setupUI() {
        binding.buttonPaymentAdd.setOnClickListener(v -> attemptPayment()); // Ödeme girişimini başlat
    }

    // Ödeme girişimini başlat
    private void attemptPayment() {
        if (validateInputs()) { // Girişleri doğrula
            performPayment(); // Ödemeyi gerçekleştir
        }
    }

    // Girişleri doğrula
    private boolean validateInputs() {
        cardNumber = binding.editTextCardNumber.getText().toString(); // Kart numarasını al
        expirationDate = binding.editTextExpirationDate.getText().toString(); // Son kullanma tarihini al
        cvv = binding.editTextCVV.getText().toString(); // CVV'yi al

        if (cardNumber.isEmpty() || !cardNumber.matches("\\d{16}")) {
            showToast("Lütfen geçerli bir kart numarası giriniz (16 hane).");
            return false;
        }
        if (expirationDate.isEmpty() || !expirationDate.matches("^(0[1-9]|1[0-2])\\/([0-9]{2})$")) {
            showToast("Son kullanma tarihi AA/YY formatında olmalıdır.");
            return false;
        }
        if (cvv.isEmpty() || !cvv.matches("\\d{3}")) {
            showToast("Lütfen geçerli bir CVV giriniz (3 hane).");
            return false;
        }
        return true;
    }

    // Ödemeyi gerçekleştir
    private void performPayment() {
        UserApiService apiService = RetrofitClientInstance.getRetrofitInstance().create(UserApiService.class);

        Call<Void> call = apiService.createPayment(new Payment(userId, userName, cardNumber, expirationDate, cvv));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    showToast("Ödeme Yöntemi Eklendi!"); // Başarılı yanıt durumunda mesaj göster
                    sharedViewModel.setPaymentAdded(true); // Paylaşılan View Model'e ödeme eklenmiş olarak işaretle
                    dismiss(); // Pencereyi kapat
                } else {
                    showToast("Ödeme Yöntemi Eklenemedi. Hata: " + response.code()); // Başarısız yanıt durumunda hata mesajı göster
                    dismiss(); // Pencereyi kapat
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showToast("Ödeme Yöntemi Eklemesi Sırasında Hata Oluştu."); // Hata durumunda hata mesajı göster
                logError(new Exception(t)); // Hata günlüğüne kaydet
                dismiss(); // Pencereyi kapat
            }
        });
    }

    // Kullanıcı kimliğini al
    private void getUserId() {
        User user = UserManager.getInstance().getUser();
        userId = user.getUserId(); // Kullanıcı kimliğini al
        userName = user.getAd() + " " + user.getSoyad(); // Kullanıcı adını ve soyadını al
    }

    // Hata günlüğüne kaydet
    private void logError(Exception e) {
        Log.e(TAG, "Error: ", e); // Hata günlüğüne kaydet
        showToast("Bir hata oluştu, lütfen daha sonra tekrar deneyin " + e.getLocalizedMessage()); // Hata mesajını göster
    }

    // Toast mesajı göster
    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show(); // Toast mesajı göster
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.gravity = Gravity.BOTTOM; // Pencereyi alt kısmına yerleştir
            params.width = ViewGroup.LayoutParams.MATCH_PARENT; // Genişliği ekrana uyumlu olarak ayarla
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT; // Yüksekliği içeriğe uyumlu olarak ayarla
            getDialog().getWindow().setAttributes(params); // Pencere özelliklerini ayarla
        }
    }
}
