package com.example.notesharingapp;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<SlideModel> imageList = new ArrayList<>(); // Create image list

        // Adding images to the list
        imageList.add(new SlideModel(R.drawable.study1, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.study2, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.study3, ScaleTypes.FIT));
        NavHostFragment navHostFragment = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        } else {
            throw new IllegalStateException("NavHostFragment not found");
        }

    }
}

