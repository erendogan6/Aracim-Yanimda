package com.example.aracimyanimda.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.aracimyanimda.databinding.FragmentProfilBinding;
import com.example.aracimyanimda.model.User;
import com.example.aracimyanimda.ui.activity.LoginActivity;
import com.example.aracimyanimda.util.SharedPreferencesUtils;
import com.example.aracimyanimda.util.UserManager;

import java.io.IOException;
import java.security.GeneralSecurityException;
public class ProfileFragment extends Fragment {
    private FragmentProfilBinding binding;

    // Fragment oluşturulduğunda görünümü oluştur
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfilBinding.inflate(inflater, container, false); // Bağlama nesnesini oluştur
        return binding.getRoot(); // Kök görünümü döndür
    }

    // Görünüm oluşturulduğunda
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUI(); // Kullanıcı arayüzünü kur
        getUserInfo(); // Kullanıcı bilgilerini al
    }

    // Kullanıcı arayüzünü kur
    private void setupUI() {
        binding.constraintCikis.setOnClickListener(v -> cikisYap()); // Çıkış düğmesine tıklandığında çıkış yap
        binding.constraintPayment.setOnClickListener(v -> odemeYontemi()); // Ödeme yöntemi düğmesine tıklandığında ödeme yöntemlerini göster
        binding.constraintEhliyet.setOnClickListener(v -> ehliyetEkle()); // Ehliyet ekle düğmesine tıklandığında ehliyet ekranını göster
    }

    // Kullanıcı bilgilerini al
    private void getUserInfo() {
        User user = UserManager.getInstance().getUser(); // Kullanıcı bilgilerini al
        binding.kullaniciAd.setText(String.format("%s %s", user.getAd(), user.getSoyad())); // Kullanıcı adını ve soyadını ayarla
        binding.kullaniciMail.setText(user.getePosta()); // Kullanıcı e-postasını ayarla
        binding.kullaniciTelefon.setText(user.getTelefon()); // Kullanıcı telefon numarasını ayarla
    }

    // Ehliyet ekranını göster
    private void ehliyetEkle() {
        LicenseFragment licenseFragment = new LicenseFragment(); // Ehliyet fragmentini oluştur
        licenseFragment.show(getParentFragmentManager(), "LicenseFragment"); // Ehliyet fragmentini göster
    }

    // Ödeme yöntemlerini göster
    private void odemeYontemi() {
        PaymentsListFragment paymentsListFragment = new PaymentsListFragment(); // Ödeme listesi fragmentini oluştur
        paymentsListFragment.show(getParentFragmentManager(), "PaymentsListFragment"); // Ödeme listesi fragmentini göster
    }

    // Görünüm yok edildiğinde
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Bağlama nesnesini null yap
    }

    // Çıkış yap
    private void cikisYap() {
        try {
            SharedPreferences sharedPreferences = SharedPreferencesUtils.getEncryptedSharedPreferences(getActivity()); // Şifreli paylaşılan tercihleri al
            sharedPreferences.edit().clear().apply(); // Tüm tercihleri temizle
            Intent intent = new Intent(getActivity(), LoginActivity.class); // Giriş ekranına yönlendirme intenti oluştur
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Aktivite yığınını temizle ve yeni görev oluştur
            startActivity(intent); // Giriş ekranını başlat
        } catch (GeneralSecurityException | IOException ex) {
            throw new RuntimeException(ex); // Genel güvenlik hatası veya giriş/çıkış hatası durumunda istisna fırlat
        }
    }
}
