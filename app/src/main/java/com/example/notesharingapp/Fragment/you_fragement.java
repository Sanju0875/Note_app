package com.example.notesharingapp.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.notesharingapp.LoginActivity;
import com.example.notesharingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class you_fragement extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView profilePicture;
    private TextView userName, userEmail, userContact;
    private Button uploadPictureButton, logoutButton;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;

    private Uri selectedImageUri;

    public you_fragement() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_you_fragement, container, false);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("LoginInfo");
        storageReference = FirebaseStorage.getInstance().getReference("ProfilePictures");

        // Initialize UI elements
        profilePicture = view.findViewById(R.id.profilePicture);
        userName = view.findViewById(R.id.userName);
        userEmail = view.findViewById(R.id.userEmail);
        userContact = view.findViewById(R.id.userContact);
        uploadPictureButton = view.findViewById(R.id.addProfileImageButton);
        logoutButton = view.findViewById(R.id.logoutButton);

        // Fetch and set profile data
        fetchProfileData();

        // Handle profile picture upload
        uploadPictureButton.setOnClickListener(v -> openFileChooser());

        // Handle logout button
        logoutButton.setOnClickListener(v -> {
            firebaseAuth.signOut(); // Sign out the user
            Toast.makeText(getActivity(), "Logged Out", Toast.LENGTH_SHORT).show();

            // Redirect to LoginActivity
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
            startActivity(intent);
        });

        return view;
    }

    private void fetchProfileData() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String contact = snapshot.child("phone").getValue(String.class);
                        String profileUrl = snapshot.child("profilePicture").getValue(String.class);

                        userName.setText(name != null ? name : "No Name");
                        userEmail.setText(email != null ? email : "No Email");
                        userContact.setText(contact != null ? "Contact: " + contact : "No Contact");

                        // Log data for debugging
                        Log.d("FetchData", "Profile URL: " + profileUrl);
                        Log.d("FetchData", "User Name: " + name);
                        Log.d("FetchData", "User Email: " + email);

                        if (profileUrl != null) {
                            Glide.with(getActivity())
                                    .load(profileUrl)
                                    .placeholder(R.drawable.book) // Default placeholder image
                                    .error(R.drawable.user) // Error image
                                    .skipMemoryCache(true) // Avoid caching for testing updates
                                    .into(profilePicture);
                        } else {
                            profilePicture.setImageResource(R.drawable.book); // Default image
                        }
                    } else {
                        Toast.makeText(getActivity(), "No user data found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            uploadProfilePicture();
        }
    }

    private void uploadProfilePicture() {
        if (selectedImageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");

            fileReference.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot ->
                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String userId = firebaseAuth.getCurrentUser().getUid();
                        HashMap<String, Object> updates = new HashMap<>();
                        updates.put("profilePicture", uri.toString());

                        databaseReference.child(userId).updateChildren(updates)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Profile Picture Updated", Toast.LENGTH_SHORT).show();
                                        fetchProfileData(); // Refresh the data to show the updated picture
                                    } else {
                                        Toast.makeText(getActivity(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    })
            ).addOnFailureListener(e -> Toast.makeText(getActivity(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}
