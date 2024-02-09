package com.erendogan6.aracimyanimda.util;

public class InputValidator {
    // SQL enjeksiyonu için girişi temizle
    public static String SQL_InjectionInput(String input) {
        // Potansiyel tehlikeli karakterlerin bir listesi
        String[] dangerousCharacters = {"'", "\"", ";", "--", "/*", "*/", "@@"};

        // Her bir potansiyel tehlikeli karakteri input'tan kaldır
        for (String dangerousChar : dangerousCharacters) {
            input = input.replace(dangerousChar, ""); // Tehlikeli karakteri temizle
        }
        return input.trim(); // Temizlenmiş girişi trim() metodu ile döndür
    }

    // Girişin uzunluğunu belirtilen aralıkta kontrol et
    public static boolean validateLength(String input, int minLength, int maxLength) {
        // Giriş null değilse ve uzunluğu belirtilen aralıkta ise true döndür, aksi halde false döndür
        return input != null && input.length() >= minLength && input.length() <= maxLength;
    }
}
