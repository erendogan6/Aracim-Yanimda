package com.erendogan6.aracimyanimda.api;

import com.erendogan6.aracimyanimda.model.License;
import com.erendogan6.aracimyanimda.model.Login;
import com.erendogan6.aracimyanimda.model.Payment;
import com.erendogan6.aracimyanimda.model.Register;
import com.erendogan6.aracimyanimda.model.RegisterControl;
import com.erendogan6.aracimyanimda.model.Rent;
import com.erendogan6.aracimyanimda.model.Rezervation;
import com.erendogan6.aracimyanimda.model.User;
import com.erendogan6.aracimyanimda.model.Vehicle;

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
    Call<User> login(@Body Login login);

    // E-posta mevcudiyetini kontrol eder
    @POST("User/email")
    Call<Boolean> checkEmail(@Body RegisterControl request);

    // Yeni kullanıcı kaydı oluşturur
    @POST("User/register")
    Call<Void> register(@Body Register request);

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
    Call<Void> createLicense(@Body License license);

    // Kullanıcının ehliyet bilgisini getirir
    @GET("User/license")
    Call<License> getLicense(@Query("user_id") int user_id);

    // Kullanıcının rezervasyonlarını getirir
    @GET("User/users/rezervasyon")
    Call<List<Rezervation>> getRezervations(@Query("Id") int Id);
}
