package com.example.aracimyanimda.ui;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.aracimyanimda.adaptor.PaymentAdaptor;
import com.example.aracimyanimda.api.RetrofitClientInstance;
import com.example.aracimyanimda.api.UserApiService;
import com.example.aracimyanimda.databinding.FragmentPaymentsListBinding;
import com.example.aracimyanimda.model.Payment;
import com.example.aracimyanimda.util.UserManager;
import com.example.aracimyanimda.viewmodel.PaymentAdapterListener;
import com.example.aracimyanimda.viewmodel.SharedViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class PaymentsListFragment extends DialogFragment implements PaymentAdapterListener {
    private FragmentPaymentsListBinding binding;
    private int userId;
    private final String TAG = "PaymentsListFragment";
    private SharedViewModel sharedViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPaymentsListBinding.inflate(inflater, container, false); // Bağlama nesnesini oluştur
        setupUI(); // Kullanıcı arayüzünü kur
        observeViewModel(); // View Model'i izle
        getUserId(); // Kullanıcı kimliğini al
        fetchPayments(); // Ödemeleri getir
        return binding.getRoot(); // Kök görünümü döndür
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

    // Kullanıcı kimliğini al
    private void getUserId() {
        userId = UserManager.getInstance().getUser().getUserId(); // Kullanıcı kimliğini al
    }

    // Kullanıcı arayüzünü kur
    private void setupUI() {
        binding.paymentRecyclerView.setVisibility(View.VISIBLE); // Ödeme listesini görünür yap
        binding.paymentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Lineer düzen yöneticisini ayarla
        binding.buttonPaymentAdd.setOnClickListener(v -> addPayment()); // Ödeme ekle düğmesine tıklandığında ödeme ekranını aç
    }

    // Ödeme görünürlüğünü güncelle
    private void updatePaymentVisibility(Boolean isSelect) {
        binding.textOdeme.setVisibility(isSelect ? View.VISIBLE : View.GONE); // Ödeme metninin görünürlüğünü güncelle
    }

    // View Model'i izle
    private void observeViewModel() {
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class); // Paylaşılan View Model'i oluştur
        sharedViewModel.getPaymentSelect().observe(getViewLifecycleOwner(), this::updatePaymentVisibility); // Ödeme seçimini izle ve görünürlüğü güncelle
        sharedViewModel.getPaymentAdded().observe(getViewLifecycleOwner(), isAdded -> {
            if (isAdded) fetchPayments(); // Ödeme eklendiğinde ödemeleri getir
        });
    }

    // Ödemeleri getir
    private void fetchPayments() {
        UserApiService service = RetrofitClientInstance.getRetrofitInstance().create(UserApiService.class); // Kullanıcı API servisini oluştur
        service.getPayment(userId).enqueue(new Callback<List<Payment>>() {
            @Override
            public void onResponse(Call<List<Payment>> call, Response<List<Payment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    binding.paymentRecyclerView.setAdapter(new PaymentAdaptor(response.body(), sharedViewModel, getArguments(), PaymentsListFragment.this)); // Ödeme adaptörünü oluştur ve ayarla
                } else {
                    showToast("Kart Bilgileri Alınırken Hata Oluştu"); // Kart bilgileri alınamadığında hata mesajı göster
                }
            }

            @Override
            public void onFailure(Call<List<Payment>> call, Throwable t) {
                logError(new Exception(t)); // Hata durumunda hata günlüğüne kaydet
            }
        });
    }

    // Ödeme ekranını aç
    private void addPayment() {
        new PaymentFragment().show(getParentFragmentManager(), "PaymentFragment"); // Ödeme ekranını göster
    }

    // Ödeme öğesi seçildiğinde
    @Override
    public void onPaymentItemSelected(Bundle bundle) {
        RezervationDetailsFragment fragment = new RezervationDetailsFragment(); // Rezervasyon detayları fragmentini oluştur
        fragment.setArguments(bundle); // Argümanları ayarla
        fragment.show(getParentFragmentManager(), "RezervationDetailsFragment"); // Rezervasyon detayları fragmentini göster
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
