package com.cmput301w23t47.canary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.cmput301w23t47.canary.databinding.ActivityMainBinding;


/**
 * Main Acitvity
 * @author Meharpreet Singh Nanda
 */
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        init(savedInstanceState);
    }

    /**
     * Initialization for activity
     */
    private void init(Bundle savedInstanceState) {
        initNavbar();
    }

    /**
     * Initializes the navbar (bottom and top)
     */
    private void initNavbar() {
        // Bottom navigation
        NavController navController = Navigation.findNavController(this, R.id.fragment_container_view_main);
        NavigationUI.setupWithNavController(binding.bottomNavigationLayout.bottomNavigation, navController);

        // top navigation
        setSupportActionBar(binding.toolbar);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragment_container_view_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}