package com.erendogan6.aracimyanimda.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://aracimyanimda.azurewebsites.net/api/";

    // Retrofit örneğini alma
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) { // Eğer retrofit örneği henüz oluşturulmadıysa
            retrofit = new retrofit2.Retrofit.Builder() // Yeni bir Retrofit örneği oluştur
                    .baseUrl(BASE_URL) // Temel URL'yi ayarla
                    .addConverterFactory(GsonConverterFactory.create()) // JSON verilerini Gson ile dönüştürmek için dönüştürücü ekle
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // RxJava kullanarak çağrıları dönüştürmek için çağrı adaptörü ekle
                    .build(); // Retrofit örneğini oluştur
        }
        return retrofit; // Oluşturulan Retrofit örneğini döndür
    }
}
