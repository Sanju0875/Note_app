package com.example.notesharingapp.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.notesharingapp.R;

import java.util.ArrayList;

public class Home_fragement extends Fragment {

    private EditText searchEditText;
    private ImageSlider imageSlider;
    private TextView facultyText;
    private ImageView bcaImage, bhmImage, bitImage;

    public Home_fragement() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_fragement, container, false);

        // Initialize UI components
        searchEditText = view.findViewById(R.id.search);
        imageSlider = view.findViewById(R.id.image_slider);
        facultyText = view.findViewById(R.id.faculty);
        bcaImage = view.findViewById(R.id.bca_image);
        bhmImage = view.findViewById(R.id.bhm);
        bitImage = view.findViewById(R.id.bit);

        // Set up the image slider
        setupImageSlider();

        // Set click listeners for the faculty icons (Optional)
        bcaImage.setOnClickListener(v -> openFaculty("BCA"));
        bhmImage.setOnClickListener(v -> openFaculty("BHM"));
        bitImage.setOnClickListener(v -> openFaculty("BIT"));

        return view;
    }

    // Method to set up the image slider with sample images and titles
    private void setupImageSlider() {
        ArrayList<SlideModel> slideModels = new ArrayList<>();

        // Adding images with titles to the slider
        slideModels.add(new SlideModel(R.drawable.study1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.study2,  ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.study3, ScaleTypes.FIT));

        // Set the image list to the slider
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);
    }

    // Method to handle click events for faculty icons
    private void openFaculty(String facultyName) {
}
}