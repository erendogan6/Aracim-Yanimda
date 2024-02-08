package com.example.aracimyanimda.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.aracimyanimda.R;
import com.example.aracimyanimda.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Üst sınıfın onCreate() yöntemini çağır
        com.example.aracimyanimda.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(this.getLayoutInflater()); // Bağlama nesnesini oluştur ve ActivityMainBinding sınıfından inflate et
        setContentView(binding.getRoot()); // Kök görünümü ayarla

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main); // Geçiş yöneticisini bul ve NavController nesnesini oluştur
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view); // Alt gezinme görünümünü bul
        NavigationUI.setupWithNavController(bottomNavigationView, navController); // Alt gezinme görünümünü NavController ile ayarla

        bottomNavigationView.setBackgroundColor(ContextCompat.getColor(this, R.color.navback)); // Alt gezinme görünümünün arka plan rengini ayarla
    }
}
