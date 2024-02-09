package com.erendogan6.aracimyanimda.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SharedPreferencesUtils {

    // Şifrelenmiş SharedPreferences nesnesini oluşturur
    public static SharedPreferences getEncryptedSharedPreferences(Context context) throws GeneralSecurityException, IOException {
        // Anahtar oluşturucudan veya varsa anahtarı alır
        String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

        // Şifrelenmiş SharedPreferences nesnesini oluşturur ve döndürür
        return EncryptedSharedPreferences.create(
                "aracimYanimda", // Dosya adı
                masterKeyAlias, // Anahtar
                context, // Bağlam
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, // Anahtar şifreleme şeması
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM // Değer şifreleme şeması
        );
    }
}
