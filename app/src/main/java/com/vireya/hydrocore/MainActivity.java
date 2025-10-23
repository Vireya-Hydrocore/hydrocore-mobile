package com.vireya.hydrocore;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.vireya.hydrocore.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        SharedPreferences prefs = getSharedPreferences("configuracoes", Context.MODE_PRIVATE);
        boolean darkModeAtivo = prefs.getBoolean("modoEscuro", false);

        if (darkModeAtivo) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        AppCompatDelegate.setDefaultNightMode(
                prefs.getBoolean("modoEscuro", false)
                        ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO
        );

        setContentView(binding.getRoot());

        // NavController principal
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        // Top navigation (botões do topo)
        ImageView btnVireya = findViewById(R.id.imgHydrocore);
        ImageView btnAgenda = findViewById(R.id.imgAgenda);
        ImageView btnConfig = findViewById(R.id.imgConfig);

        // Botão que abre o Drawer
        btnVireya.setOnClickListener(v -> {
            DrawerLayout drawer = findViewById(R.id.drawerLayout);
            if (drawer != null) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        // Botão que navega para Agenda
        btnAgenda.setOnClickListener(v -> {
            navController.navigate(R.id.navigation_agenda, null,
                    new androidx.navigation.NavOptions.Builder()
                            .setPopUpTo(R.id.navigation_home, true) // limpa até a Home
                            .build()
            );
            desmarcarHome();
        });

        // Botão que navega para Configurações
        btnConfig.setOnClickListener(v -> {
            navController.navigate(R.id.navigation_configuracoes, null,
                    new androidx.navigation.NavOptions.Builder()
                            .setPopUpTo(R.id.navigation_home, true)
                            .build()
            );
            desmarcarHome();
        });




        // Bottom navigation
        BottomNavigationView navView = binding.navView;
        NavigationUI.setupWithNavController(navView, navController);

        // Drawer navigation
        NavigationView navigationView = findViewById(R.id.navigationView);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void desmarcarHome() {
        BottomNavigationView bottomNav = findViewById(R.id.nav_view);
        if (bottomNav != null) {
            bottomNav.getMenu().setGroupCheckable(0, true, false);
            bottomNav.getMenu().findItem(R.id.navigation_home).setChecked(false);
            bottomNav.getMenu().setGroupCheckable(0, true, true);
        }
    }
}
