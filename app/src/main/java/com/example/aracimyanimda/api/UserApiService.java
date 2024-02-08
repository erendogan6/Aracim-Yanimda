package com.example.aracimyanimda.api;

import com.example.aracimyanimda.api.request.LicenseRequest;
import com.example.aracimyanimda.api.request.LoginRequest;
import com.example.aracimyanimda.api.request.RegisterFirstRequest;
import com.example.aracimyanimda.api.request.RegisterRequest;
import com.example.aracimyanimda.api.response.Rent;
import com.example.aracimyanimda.model.Payment;
import com.example.aracimyanimda.model.Rezervation;
import com.example.aracimyanimda.model.User;
import com.example.aracimyanimda.model.Vehicle;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
public interface UserApiService {
    // Kullanıcı girişi için istek gönderir
    @POST("User/login")
    Call<User> login(@Body LoginRequest loginRequest);

    // E-posta mevcudiyetini kontrol eder
    @POST("User/email")
    Call<Boolean> checkEmail(@Body RegisterFirstRequest request);

    // Yeni kullanıcı kaydı oluşturur
    @POST("User/register")
    Call<Void> register(@Body RegisterRequest request);

    // Araç listesini getirir
    @GET("Vehicle/vehicles")
    Observable<List<Vehicle>> vehicleGet();

    // Rezervasyon oluşturur
    @POST("Rezervasyon/create")
    Call<Void> createReservation(@Query("paymentMethodId") int paymentMethodId, @Body Rezervation requestData);

    // Ödeme yöntemi oluşturur
    @POST("Payment/paymentmethod/create")
    Call<Void> createPayment(@Body Payment paymentCreateRequest);

    // Kullanıcının ödeme yöntemlerini getirir
    @GET("Payment/paymentmethods")
    Call<List<Payment>> getPayment(@Query("user_id") int userId);

    // Kullanıcının rezervasyonunu kontrol eder
    @GET("User/rezervasyonkontrol")
    Observable<Response<Integer>> checkRezervation(@Query("user_id") int userId);

    // Kullanıcının kiralama bilgisini kontrol eder
    @GET("Payment/bilgi")
    Observable<Response<Rent>> checkRent(@Query("rezervation_id") int rezervation_id);

    // Kullanıcının kiralama işlemini tamamlar
    @POST("Payment/tamamla")
    Observable<Response<Void>> finishRent(@Query("rezervation_id") int rezervation_id);

    // Ehliyet bilgisini kaydeder
    @POST("User/license/create")
    Call<Void> createLicense(@Body LicenseRequest licenseRequest);

    // Kullanıcının ehliyet bilgisini getirir
    @GET("User/license")
    Call<LicenseRequest> getLicense(@Query("user_id") int user_id);

    // Kullanıcının rezervasyonlarını getirir
    @GET("User/users/rezervasyon")
    Call<List<Rezervation>> getRezervations(@Query("Id") int Id);
}
