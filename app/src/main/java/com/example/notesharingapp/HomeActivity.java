package com.example.notesharingapp;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Ensure this is correctly set up
//        setContentView(R.layout.activity_main);
//
//        ArrayList<SlideModel> imageList = new ArrayList<>(); // Create image list
//
//        // Adding images to the list
//        imageList.add(new SlideModel(R.drawable.study1, ScaleTypes.FIT));
//        imageList.add(new SlideModel(R.drawable.study2, ScaleTypes.FIT));
//        imageList.add(new SlideModel(R.drawable.study3, ScaleTypes.FIT));

//        ImageSlider imageSlider = binding.imageSlider;
//        imageSlider.setImageList(imageList);
//        imageSlider.setImageList(imageList, ScaleTypes.FIT);


        ImageSlider imageSlider = findViewById(R.id.image_slider);
//        imageSlider.setImageList(imageList);
    }
}
