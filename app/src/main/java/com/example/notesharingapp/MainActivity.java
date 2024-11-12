package com.example.notesharingapp;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.notesharingapp.Fragment.Home_fragement;
import com.example.notesharingapp.Fragment.add_fragement;
import com.example.notesharingapp.Fragment.course_fragement;
import com.example.notesharingapp.Fragment.you_fragement;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.navigation);

        NavigationBarView.OnItemSelectedListener navListener = item -> {
            int itemId = item.getItemId();
            Fragment selectedFragment = null;

            if (itemId == R.id.nav_home) {
                selectedFragment = new Home_fragement();
            } else if (itemId == R.id.nav_course) {
                selectedFragment = new course_fragement();
            } else if (itemId == R.id.nav_add) {
                selectedFragment = new add_fragement();
            } else if (itemId == R.id.nav_you) {
                selectedFragment = new you_fragement();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, selectedFragment).commit();
            }
            return true;
        };

        bottomNav.setOnItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.home_fragement);

        Fragment selectedFragment = new Home_fragement();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, selectedFragment).commit();

        ImageSlider imageSlider = findViewById(R.id.image_slider);
        ArrayList<SlideModel> imageList = new ArrayList<>();
        imageList.add(new SlideModel(R.drawable.study1, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.study2, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.study3, ScaleTypes.FIT));
        imageSlider.setImageList(imageList, ScaleTypes.FIT);
    }
}
