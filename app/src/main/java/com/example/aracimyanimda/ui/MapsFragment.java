package com.example.aracimyanimda.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.aracimyanimda.R;
import com.example.aracimyanimda.api.RetrofitClientInstance;
import com.example.aracimyanimda.api.UserApiService;
import com.example.aracimyanimda.api.response.RentResponse;
import com.example.aracimyanimda.databinding.FragmentMapsBinding;
import com.example.aracimyanimda.model.Vehicle;
import com.example.aracimyanimda.util.UserManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Response;

// Harita fragmenti sınıfı
public class MapsFragment extends Fragment {
    // Sabitler
    private final String TAG = "MapsFragment"; // Log etiketi
    private final float MAP_ZOOM = 10.0f; // Harita yakınlaştırma düzeyi
    private final long REFRESH_INTERVAL_MS = 20000L; // Harita ve durumun yenilenme aralığı (milisaniye cinsinden)
    private final String ERROR_ADDRESS_NOT_FOUND = "Adres bulunamadı"; // Adres bulunamadı hatası mesajı
    private final String ERROR_GETTING_ADDRESS = "Adres alınırken hata oluştu"; // Adres alınırken hata oluştu hatası mesajı
    private final String PERMISSION_DENIED_MESSAGE = "Uygulamayı Kullanabilmek İçin Konum İzni Gereklidir"; // Konum izni reddedildi mesajı
    private final String ERROR_MESSAGE_PREFIX = "Bir hata oluştu: "; // Genel hata mesajı öneki
    private final Map<Marker, Vehicle> vehicleMarkerMap = new HashMap<>(); // Araç simgesi ve yanıt eşlemesi için bir harita
    private final Handler handler = new Handler(); // Arka planda çalışan iş parçacığı için bir işleyici
    // Diğer değişkenler
    private GoogleMap mMap; // Google Harita nesnesi
    private ActivityResultLauncher<String> requestPermissionLauncher; // Konum izni isteği başlatıcısı
    private UserApiService apiService; // Kullanıcı API hizmeti
    private FusedLocationProviderClient fusedLocationClient; // Birleştirilmiş Konum Sağlayıcı İstemcisi
    private FragmentMapsBinding binding; // Harita fragmenti bağlama nesnesi
    private int userId; // Kullanıcı kimliği
    private Integer rezervation_id; // Rezervasyon kimliği
    // Araç detayları diyalogunu açmak için kullanılan çalıştırılabilir kod
    private final Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            refreshMapAndStatus(); // Haritayı ve durumu yenile
            handler.postDelayed(this, REFRESH_INTERVAL_MS); // Yenileme aralığı kadar geciktir ve tekrar çalıştır
        }
    };
    // Harita hazır olduğunda çalıştırılacak geri çağırma işlevi
    private final OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            initializeMap(); // Haritayı başlat
        }
    };

    // Fragment sonlandırıldığında gerçekleşecek işlemler
    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnableCode); // İş parçacığını durdur
    }

    // Araç simgeleri üzerine tıklama olayını işleyen metot
    private void registerMarkerClickHandler() {
        mMap.setOnMarkerClickListener(marker -> {
            Vehicle selectedVehicle = vehicleMarkerMap.get(marker);
            if (selectedVehicle != null) {
                LatLng vehicleLocation = marker.getPosition();
                selectedVehicle.setAdres(getAddressFromLocation(vehicleLocation.latitude, vehicleLocation.longitude));
                openVehicleDetailsDialog(selectedVehicle); // Araç detayları diyalogunu aç
            }
            return true;
        });
    }

    // Araç detayları diyalogunu açan metot
    private void openVehicleDetailsDialog(Vehicle vehicle) {
        VehicleFragment dialogFragment = new VehicleFragment();
        Bundle bundle = createVehicleDetailsBundle(vehicle);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getParentFragmentManager(), "VehicleFragment");
    }

    // Araç detayları için paket oluşturan metot
    private Bundle createVehicleDetailsBundle(Vehicle selectedVehicle) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("car",selectedVehicle);
        return bundle;
    }

    // Güncel konumu getir
    @SuppressLint("MissingPermission")
    private void fetchCurrentLocation() {
        if (hasLocationPermission()) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this::moveCameraToLocation);
        } else {
            requestLocationPermission();
        }
    }

    // Konum izni var mı kontrol et
    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // Haritada konumu güncelle
    private void moveCameraToLocation(Location location) {
        if (location != null) {
            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, MAP_ZOOM));
        }
    }

    // Konum izni iste
    private void requestLocationPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            showLocationPermissionRationaleDialog();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    // Konum izni diyalogunu göster
    private void showLocationPermissionRationaleDialog() {
        new AlertDialog.Builder(getContext()).setTitle("Konum İzni Gerekli").setMessage("Bu uygulamanın düzgün çalışabilmesi için konum iznine ihtiyacı var. Lütfen konum izni verin.").setPositiveButton("İzin Ver", (dialog, which) -> requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)).setNegativeButton("İptal", (dialog, which) -> dialog.dismiss()).create().show();
    }

    // Haritayı başlat
    private void initializeMap() {
        updateLocationUI(); // Konum arayüzünü güncelle
        registerMarkerClickHandler(); // Araç simgeleri üzerine tıklama olayını işle
        fetchCurrentLocation(); // Güncel konumu getir
        getIdFromViewModel(); // ViewModel'den kullanıcı kimliğini al
    }

    // View modelden kullanıcı kimliğini al
    private void getIdFromViewModel() {
        userId = UserManager.getInstance().getUser().getUserId();
        handler.post(runnableCode); // Arka planda çalışan iş parçacığını başlat
    }

    // Konum arayüzünü güncelle
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.setTrafficEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, MAP_ZOOM));
                }
            });
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    // Konum bilgisinden adresi al
    private String getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getAddressLine(0);
            } else {
                return ERROR_ADDRESS_NOT_FOUND; // Adres bulunamadı hatası
            }
        } catch (IOException e) {
            logError(e); // Hata günlüğüne kaydet
            return ERROR_GETTING_ADDRESS; // Adres alınırken hata oluştu hatası
        }
    }

    // Harita simgelerini yeniden boyutlandır
    @Nullable
    private BitmapDescriptor resizeMapIcons() {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.caricon);
        if (imageBitmap == null) {
            Log.e(TAG, "Kaynak çözümlenemedi: caricon"); // Kaynak çözümlenemedi hatası
            return null;
        }
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, 80, 60, false);
        return BitmapDescriptorFactory.fromBitmap(resizedBitmap);
    }

    // Araçları yükle
    private void loadVehicles() {
        // Arayüzden ayrı iş parçacığında araçları yükle
        apiService.vehicleGet().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Vehicle>>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                // Abonelik başladığında yapılacak işlemler
            }

            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Vehicle> vehicleResponses) {
                // Veriler alındığında yapılacak işlemler
                displayVehiclesOnMap(vehicleResponses); // Harita üzerinde araçları göster
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                // Hata oluştuğunda yapılacak işlemler
                logError(new Exception(e)); // Hata günlüğüne kaydet
            }

            @Override
            public void onComplete() {
                // Gözlemleme tamamlandığında yapılacak işlemler
            }
        });
    }

    // Haritada araçları göster
    private void displayVehiclesOnMap(List<Vehicle> vehicles) {
        for (Vehicle vehicle : vehicles) {
            // "Bosta" durumundaki araçları işle
            if ("Bosta".equals(vehicle.getDurum())) {
                LatLng vehicleLocation = new LatLng(Double.parseDouble(vehicle.getLatitude()), Double.parseDouble(vehicle.getLongitude()));
                MarkerOptions markerOptions = createMarkerForVehicle(vehicle, vehicleLocation); // Araç için işaretçi seçenekleri oluştur
                Marker marker = mMap.addMarker(markerOptions); // Haritaya işareti ekle
                vehicleMarkerMap.put(marker, vehicle); // Araç işareti ve yanıt eşleşmesini haritaya ekle
            }
        }
    }

    // Araç için işaretçi seçenekleri oluştur
    private MarkerOptions createMarkerForVehicle(Vehicle vehicle, LatLng location) {
        return new MarkerOptions().position(location).title(vehicle.getMarka() + " " + vehicle.getModel()).icon(resizeMapIcons()); // Araç simgesini yeniden boyutlandır ve ayarla
    }

    // onCreate yöntemi
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = RetrofitClientInstance.getRetrofitInstance().create(UserApiService.class); // API hizmetini oluştur
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity()); // Birleştirilmiş Konum Sağlayıcı İstemcisini al
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            // İzin isteği sonucunu işle
            if (isGranted) {
                updateLocationUI(); // Konum arayüzünü güncelle
            } else {
                showToast(PERMISSION_DENIED_MESSAGE); // İzin reddedildi mesajını göster
            }
        });
    }

    // onCreateView yöntemi
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMapsBinding.inflate(inflater, container, false); // Bağlama nesnesini oluştur
        return binding.getRoot(); // Kök görünümü döndür
    }

    // showToast metodu
    private void showToast(String message) {
        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show()); // Toast mesajı göster
    }

    // onViewCreated yöntemi
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map); // Harita fragmentını al
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback); // Haritayı asenkron olarak al
        }
        binding.buttonRentFinish.setOnClickListener(v -> finishRent()); // Kiralamayı bitir
    }

    // Kiralamayı bitir
    private void finishRent() {
        apiService.finishRent(rezervation_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Response<Void>>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                // Abonelik başladığında yapılacak işlemler
            }

            @Override
            public void onNext(Response<Void> voidResponse) {
                // Veri alındığında yapılacak işlemler
                if (voidResponse.isSuccessful()) {
                    showToast("Kiralama Bitirildi"); // Kiralama bitirildi mesajını göster
                    refreshMapAndStatus(); // Haritayı ve durumu yenile
                } else {
                    Log.e(TAG, "Error code: " + voidResponse.code());
                    showToast("Hata oluştu: " + voidResponse.code()); // Hata kodunu göster
                }
            }

            @Override
            public void onError(Throwable e) {
                // Hata olduğunda yapılacak işlemler
                logError(new Exception(e)); // Hata günlüğüne kaydet
            }

            @Override
            public void onComplete() {
                // Gözlemleme tamamlandığında yapılacak işlemler
            }
        });
    }

    private void refreshMapAndStatus() {
        clearMap(); // Haritayı temizle
        loadVehicles(); // Araçları yükle
        checkRezervation(); // Rezervasyonu kontrol et
    }

    // Haritayı temizle
    private void clearMap() {
        if (mMap != null) {
            mMap.clear(); // Haritayı temizle
        }
    }

    // Kiralama arayüzünü güncelle
    private void updateRentalUI(boolean hasActiveRental, RentResponse rentResponse) {
        int visibility = hasActiveRental && rentResponse != null ? View.VISIBLE : View.GONE; // Görünürlüğü belirle
        binding.rentalStatusContainer.setVisibility(visibility); // Kiralama durumu konteynırının görünürlüğünü ayarla
        binding.container2.setVisibility(visibility); // Konteynır 2'nin görünürlüğünü ayarla
        binding.tvRentalKM.setVisibility(visibility); // KM metninin görünürlüğünü ayarla
        binding.tvRentalPrice.setVisibility(visibility); // Fiyat metninin görünürlüğünü ayarla
        binding.tvRentalStatus.setVisibility(visibility); // Durum metninin görünürlüğünü ayarla
        binding.buttonRentFinish.setVisibility(visibility); // Kiralamayı bitir düğmesinin görünürlüğünü ayarla
        if (hasActiveRental && rentResponse != null) {
            binding.tvRentalKM.setText("KM: " + rentResponse.getKm()); // KM bilgisini güncelle
            binding.tvRentalPrice.setText("Ücret: " + rentResponse.getMiktar()); // Ücret bilgisini güncelle
        }
    }

    // Hata günlüğüne kaydet
    private void logError(Exception e) {
        Log.e(TAG, ERROR_MESSAGE_PREFIX, e); // Hata günlüğüne kaydet
        showToast("Bir hata oluştu, lütfen daha sonra tekrar deneyin " + e.getLocalizedMessage()); // Hata mesajını göster
    }

    // Rezervasyonu kontrol et
    private void checkRezervation() {
        apiService.checkRezervation(userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Response<Integer>>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                // Abonelik başladığında yapılacak işlemler
            }

            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull Response<Integer> integerResponse) {
                // Veri alındığında yapılacak işlemler
                if (integerResponse.isSuccessful()) {
                    rezervation_id = integerResponse.body(); // Rezervasyon kimliğini al
                    fetchRentDetails(rezervation_id); // Kiralama detaylarını getir
                } else {
                    handleNoActiveRental(); // Aktif kiralama yoksa işlemi tamamla
                }
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                // Hata olduğunda yapılacak işlemler
                logError(new Exception(e)); // Hata günlüğüne kaydet
            }

            @Override
            public void onComplete() {
                // Gözlemleme tamamlandığında yapılacak işlemler
            }
        });
    }

    // Kiralama detaylarını getir
    private void fetchRentDetails(Integer reservationId) {
        apiService.checkRent(reservationId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Response<RentResponse>>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                // Abonelik başladığında yapılacak işlemler
            }

            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull Response<RentResponse> response) {
                // Veri alındığında yapılacak işlemler
                if (response.isSuccessful()) {
                    updateRentalUI(true, response.body()); // Kiralama arayüzünü güncelle
                }
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                // Hata olduğunda yapılacak işlemler
                logError(new Exception(e)); // Hata günlüğüne kaydet
                handleNoActiveRental(); // Aktif kiralama yoksa işlemi tamamla
            }

            @Override
            public void onComplete() {
                // Gözlemleme tamamlandığında yapılacak işlemler
            }
        });
    }

    // Aktif kiralama yoksa işlemi tamamla
    private void handleNoActiveRental() {
        updateRentalUI(false, null); // Kiralama arayüzünü güncelle
        loadVehicles(); // Araçları yükle
    }
}