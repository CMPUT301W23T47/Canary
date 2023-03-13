package com.cmput301w23t47.canary.view.activity;

import android.os.Bundle;

import com.cmput301w23t47.canary.databinding.ActivityHomeBinding;
import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cmput301w23t47.canary.R;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        initNavbar();
    }

    /**
     * Initializes the navbar (bottom and top)
     */
    private void initNavbar() {
        // Bottom navigation
        NavController navController = Navigation.findNavController(this, R.id.fragment_container_view_home);
        NavigationUI.setupWithNavController(binding.bottomNavigationLayout.bottomNavigation, navController);

        // top navigation
        setSupportActionBar(binding.toolbar);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragment_container_view_home);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}