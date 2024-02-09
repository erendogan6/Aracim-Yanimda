package com.erendogan6.aracimyanimda.ui.fragment;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.erendogan6.aracimyanimda.R;
import com.erendogan6.aracimyanimda.databinding.FragmentRezervationDateBinding;
import com.erendogan6.aracimyanimda.model.Vehicle;
import com.erendogan6.aracimyanimda.viewmodel.SharedViewModel;

public class RezervationDateFragment extends DialogFragment {
    private FragmentRezervationDateBinding binding;
    private static final String DAILY_RENTAL = "Günlük";
    private static final String UNLIMITED_RENTAL = "Sınırsız";
    private Calendar startDateCalendar, endDateCalendar;

    // Görünüm oluşturulduğunda
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRezervationDateBinding.inflate(inflater, container, false); // Bağlama nesnesini oluştur
        setupListeners(); // Olay dinleyicilerini kur
        return binding.getRoot(); // Kök görünümü döndür
    }

    // Olay dinleyicilerini kur
    private void setupListeners() {
        binding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> updateUIBasedOnRentalType(checkedId)); // Radyo düğmelerine tıklanınca UI'yı güncelle
        binding.radioGunluk.setChecked(true); // Varsayılan olarak günlük kiralama seçili olsun
        binding.editTextPickupDate.setOnClickListener(v -> showDatePickerDialog(true)); // Alış tarihini seçmek için tarih seçiciyi göster
        binding.editTextPickupTime.setOnClickListener(v -> showDatePickerDialog(false)); // Alış zamanını seçmek için tarih seçiciyi göster
        binding.btnKirala.setOnClickListener(v -> showReservationDetailsDialog()); // Kiralama detaylarını göstermek için fragmentı aç
    }

    // Kiralama türüne göre UI'yı güncelle
    private void updateUIBasedOnRentalType(int checkedId) {
        boolean isDaily = checkedId == R.id.radio_gunluk; // Günlük kiralama seçildiyse
        binding.pickUplayout.setVisibility(isDaily ? View.VISIBLE : View.GONE); // Günlük kiralama seçiliyse alış tarihini göster, değilse gizle
        if (!isDaily) {
            binding.editTextPickupDate.setText(""); // Günlük kiralama değilse alış tarihini temizle
            binding.editTextPickupTime.setText(""); // Günlük kiralama değilse alış zamanını temizle
        }
    }

    // Tarih seçiciyi göster
    private void showDatePickerDialog(boolean isStartDate) {
        Calendar calendar = Calendar.getInstance(); // Takvim nesnesi oluştur
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> updateDateInView(isStartDate, year, month, dayOfMonth); // Tarih seçildiğinde işlenecek olay dinleyicisi

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)); // Tarih seçiciyi oluştur
        if (!isStartDate && startDateCalendar != null) {
            datePickerDialog.getDatePicker().setMinDate(startDateCalendar.getTimeInMillis()); // Bitiş tarihini başlangıç tarihinden sonraya ayarla
        } else {
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000L); // Bugünün tarihinden önceki tarihleri devre dışı bırak
        }
        datePickerDialog.show(); // Tarih seçiciyi göster
    }

    // Tarihi güncelle
    private void updateDateInView(boolean isStartDate, int year, int month, int dayOfMonth) {
        Calendar selectedDate = Calendar.getInstance(); // Seçilen tarihi temsil eden takvim nesnesi oluştur
        selectedDate.set(year, month, dayOfMonth); // Seçilen tarihi ayarla
        String formattedDate = dayOfMonth + "/" + (month + 1) + "/" + year; // Biçimlendirilmiş tarih oluştur

        if (isStartDate) {
            startDateCalendar = selectedDate; // Başlangıç tarihini güncelle
            binding.editTextPickupDate.setText(formattedDate); // Alış tarihini güncelle
        } else {
            endDateCalendar = selectedDate; // Bitiş tarihini güncelle
            binding.editTextPickupTime.setText(formattedDate); // Alış zamanını güncelle
        }
    }

    // Rezervasyon detaylarını göster
    private void showReservationDetailsDialog() {
        if (!validateDates()) return; // Tarihleri doğrula, geçerli değilse işlem yapma
        Bundle bundle = getArguments() != null ? getArguments() : new Bundle(); // Verileri tutacak bir dizi oluştur veya mevcut dizi varsa al
        prepareBundleForNextFragment(bundle); // Bir sonraki fragment için veri hazırla
        PaymentsListFragment paymentsListFragment = new PaymentsListFragment(); // Ödeme listesi fragmentını oluştur
        paymentsListFragment.setArguments(bundle); // Verileri ödeme listesi fragmentına aktar
        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class); // Paylaşılan viewModel'i al
        sharedViewModel.setPaymentSelect(true); // Ödeme seçeneğini belirt
        paymentsListFragment.show(getParentFragmentManager(), "PaymentsListFragment"); // Ödeme listesi fragmentını göster
    }

    // Toast mesajı göster
    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show(); // Uygulama içinde Toast mesajı göster
    }

    // Tarihleri doğrula
    private boolean validateDates() {
        if (binding.radioGunluk.isChecked() && (startDateCalendar == null || endDateCalendar == null)) { // Eğer günlük kiralama seçiliyse ve başlangıç veya bitiş tarihi girilmemişse
            showToast("Lütfen başlangıç ve bitiş tarihlerini girin."); // Kullanıcıya uygun bir mesaj göster
            return false; // Doğrulama başarısız oldu
        } else if (!binding.radioGunluk.isChecked() && startDateCalendar == null) { // Günlük kiralama seçili değilse ve başlangıç tarihi girilmemişse
            showToast("Lütfen başlangıç tarihini girin."); // Kullanıcıya uygun bir mesaj göster
            return false; // Doğrulama başarısız oldu
        }
        return true; // Tüm doğrulamalar başarılı, tarihler geçerli
    }

    // Bir sonraki fragment için veri hazırla
    private void prepareBundleForNextFragment(Bundle bundle) {
        String rentalType = binding.radioGunluk.isChecked() ? DAILY_RENTAL : UNLIMITED_RENTAL; // Kiralama tipini belirle
        Vehicle vehicle = bundle.getParcelable("car");
        vehicle.setKiralamaTipi(rentalType); // Kiralama tipini bundle'a ekle
        if (startDateCalendar != null) {
            vehicle.setKiralamaBaslangic(startDateCalendar.getTimeInMillis());// Başlangıç tarihini bundle'a ekle
        }
        if (endDateCalendar != null && "Günlük".equals(rentalType)) {
            vehicle.setKiralamaBitis(endDateCalendar.getTimeInMillis()); // Bitiş tarihini günlük kiralama ise bundle'a ekle
        } else if ("Sınırsız".equals(rentalType)) {
            vehicle.setKiralamaBitis(0L); // Bitiş tarihi sınırsız kiralama ise sıfır ekle
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
}
