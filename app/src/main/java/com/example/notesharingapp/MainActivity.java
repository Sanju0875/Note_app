package com.example.notesharingapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the NavHostFragment and NavController
        NavHostFragment navHostFragment = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

            // Set up the BottomNavigationView with the NavController
            NavigationUI.setupWithNavController(bottomNavigationView, navController);

            // Add listener to handle navigation item selection
            bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
                int itemId = item.getItemId();

                // Use if-else instead of switch to avoid the constant expression error
                if (itemId == R.id.nav_home) {
                    navController.navigate(R.id.home_fragement);
                } else if (itemId == R.id.nav_course) {
                    navController.navigate(R.id.course_fragement);
                } else if (itemId == R.id.nav_add) {
                    navController.navigate(R.id.add_fragement);
                } else if (itemId == R.id.nav_you) {
                    navController.navigate(R.id.you_fragement);
                }

                return true;
            });
        } else {
            // Handle the case where navHostFragment is null (e.g., log an error)
            Toast.makeText(this, "Error: Navigation Host Fragment not found!", Toast.LENGTH_LONG).show();
        }
}
}