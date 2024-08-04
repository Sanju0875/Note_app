package com.example.notesharingapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.notesharingapp.Fragment.Home_fragement;
import com.example.notesharingapp.Fragment.add_fragement;
import com.example.notesharingapp.Fragment.course_fragement;
import com.example.notesharingapp.Fragment.you_fragement;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragement(new Home_fragement());

        binding.nav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragement(new Home_fragement());
                    break;

                case R.id.book:
                    replaceFragement(new course_fragement());
                    break;

                case R.id.add:
                    replaceFragement(new add_fragement());
                    break;

                case R.id.profile:
                    replaceFragement(new you_fragement());
                    break;

                default:
                    break;
            }
            return true;

        });
    }

   

    private void replaceFragement(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_fragement,fragment);
        fragmentTransaction.commit();

    }
    
}