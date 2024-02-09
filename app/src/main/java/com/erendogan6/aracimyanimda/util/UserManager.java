package com.erendogan6.aracimyanimda.util;

import com.erendogan6.aracimyanimda.model.User;
// Singleton tasarım deseni kullanılarak kullanıcı yönetimini sağlayan sınıf
public class UserManager {
    // Tekil örnek (singleton) için sınıf içinde bir örnek tanımlanır
    private static UserManager instance;
    // Kullanıcı nesnesini saklamak için bir değişken tanımlanır
    private User user;

    // Yapıcı metod özel olarak belirtilerek dışarıdan örnek oluşturulması engellenir
    private UserManager() {}

    // Singleton nesnesini döndüren statik metot
    public static UserManager getInstance() {
        // Eğer instance null ise (ilk defa çağrıldıysa) yeni bir instance oluşturulur
        if (instance == null) {
            instance = new UserManager();
        }
        // Oluşturulan veya mevcut olan instance döndürülür
        return instance;
    }

    // Kullanıcıyı ayarlayan metot
    public void setUser(User user) {
        this.user = user; // Parametre olarak gelen kullanıcıyı sınıf içindeki kullanıcı değişkenine atar
    }

    // Mevcut kullanıcıyı döndüren metot
    public User getUser() {
        return user; // Sınıf içindeki kullanıcı değişkenini döndürür
    }
}
