package com.example.aracimyanimda.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Vehicle implements Parcelable {
    private String vehicleId;
    private String marka;
    private String model;
    private String yil;
    private String plaka;
    private String yakitTipi;
    private String yakitDurumu;
    private String adres;
    private String fiyatGunluk;
    private String fiyatDakika;
    private String latitude;
    private String longitude;
    private String durum;
    private String kiralamaTipi;
    private Long kiralamaBaslangic;
    private Long kiralamaBitis;
    public Vehicle(String vehicleId, String marka, String model, String yil, String plaka, String yakitTipi, String yakitDurumu, String fiyatGunluk, String fiyatDakika, String latitude, String longitude,String durum) {
        this.vehicleId = vehicleId;
        this.marka = marka;
        this.model = model;
        this.yil = yil;
        this.plaka = plaka;
        this.yakitTipi = yakitTipi;
        this.yakitDurumu = yakitDurumu;
        this.fiyatGunluk = fiyatGunluk;
        this.fiyatDakika = fiyatDakika;
        this.latitude = latitude;
        this.longitude = longitude;
        this.durum = durum;
    }

    public String getDurum() {
        return durum;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getKiralamaTipi() {
        return kiralamaTipi;
    }

    public void setKiralamaTipi(String kiralamaTipi) {
        this.kiralamaTipi = kiralamaTipi;
    }

    public Long getKiralamaBaslangic() {
        return kiralamaBaslangic;
    }

    public void setKiralamaBaslangic(Long kiralamaBaslangic) {
        this.kiralamaBaslangic = kiralamaBaslangic;
    }

    public Long getKiralamaBitis() {
        return kiralamaBitis;
    }

    public void setKiralamaBitis(Long kiralamaBitis) {
        this.kiralamaBitis = kiralamaBitis;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getMarka() {
        return marka;
    }

    public String getModel() {
        return model;
    }

    public String getYil() {
        return yil;
    }

    public String getPlaka() {
        return plaka;
    }

    public String getYakitTipi() {
        return yakitTipi;
    }

    public String getYakitDurumu() {
        return yakitDurumu;
    }

    public String getFiyatGunluk() {
        return fiyatGunluk;
    }

    public String getFiyatDakika() {
        return fiyatDakika;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    // Parcelable.Creator sınıfı tanımlanır, bu sınıf Vehicle nesnelerini parcel'dan çıkarmak için kullanılır.
    public static final Parcelable.Creator<Vehicle> CREATOR = new Parcelable.Creator<Vehicle>() {
        public Vehicle createFromParcel(Parcel in) {
            return new Vehicle(in);
        }
        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }
    };

    // Parcelable arayüzü için gerekli olan describeContents() metodunu override eder.
    @Override
    public int describeContents() {
        return 0;
    }

    // writeParcelable metodunu override eder, nesnenin içeriğini bir Parcel nesnesine yazar.
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(vehicleId);
        dest.writeString(marka);
        dest.writeString(model);
        dest.writeString(yil);
        dest.writeString(plaka);
        dest.writeString(yakitTipi);
        dest.writeString(yakitDurumu);
        dest.writeString(adres);
        dest.writeString(fiyatGunluk);
        dest.writeString(fiyatDakika);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(durum);
        dest.writeString(kiralamaTipi);
        dest.writeLong(kiralamaBaslangic);
        dest.writeLong(kiralamaBitis);
    }

    // Parcel nesnesinden bir Vehicle nesnesi oluşturmak için kullanılan özel constructor.
    private Vehicle(Parcel in) {
        vehicleId = in.readString();
        marka = in.readString();
        model = in.readString();
        yil = in.readString();
        plaka = in.readString();
        yakitTipi = in.readString();
        yakitDurumu = in.readString();
        adres = in.readString();
        fiyatGunluk = in.readString();
        fiyatDakika = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        durum = in.readString();
        kiralamaTipi = in.readString();
        kiralamaBaslangic = in.readLong();
        kiralamaBitis = in.readLong();
    }
}
