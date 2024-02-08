package com.example.aracimyanimda.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.aracimyanimda.adaptor.RezervationAdaptor;
import com.example.aracimyanimda.api.RetrofitClientInstance;
import com.example.aracimyanimda.api.UserApiService;
import com.example.aracimyanimda.databinding.FragmentRezervationsBinding;
import com.example.aracimyanimda.model.Rezervation;
import com.example.aracimyanimda.util.UserManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class RezervationsFragment extends Fragment {
    private FragmentRezervationsBinding binding;
    private int userId;

    // Görünüm oluşturulduğunda
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRezervationsBinding.inflate(inflater, container, false); // Bağlama nesnesini oluştur
        getUserId(); // Kullanıcı kimliğini al
        loadData(); // Verileri yükle
        return binding.getRoot(); // Kök görünümü döndür
    }

    // Verileri yükle
    private void loadData() {
        UserApiService apiService = RetrofitClientInstance.getRetrofitInstance().create(UserApiService.class); // API hizmeti oluştur
        Call<List<Rezervation>> call = apiService.getRezervations(userId); // Rezervasyonları getirme isteği oluştur
        call.enqueue(new Callback<List<Rezervation>>() { // İstek başarılıysa veya başarısızsa geri çağırma işlevi ayarla
            @Override
            public void onResponse(Call<List<Rezervation>> call, Response<List<Rezervation>> response) {
                if (response.isSuccessful() && response.body() != null) { // İstek başarılı ve boş değilse
                    ArrayList<Rezervation> reservationList = new ArrayList<>(response.body()); // Yanıttan rezervasyon listesini al
                    binding.rvPastReservations.setLayoutManager(new LinearLayoutManager(getContext())); // RecyclerView düzen yöneticisini ayarla
                    binding.rvPastReservations.setAdapter(new RezervationAdaptor(reservationList)); // RecyclerView adaptörünü ayarla
                }
            }

            @Override
            public void onFailure(Call<List<Rezervation>> call, Throwable t) { // İstek başarısız olduğunda
                System.out.println(t.getLocalizedMessage()); // Hata mesajını yazdır
            }
        });
    }

    // Kullanıcı kimliğini al
    private void getUserId() {
        userId = UserManager.getInstance().getUser().getUserId(); // Kullanıcı kimliğini al ve userId değişkenine ata
    }

    // Fragment görünümü yok edildiğinde
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Bağlama nesnesini yok et
    }
}
