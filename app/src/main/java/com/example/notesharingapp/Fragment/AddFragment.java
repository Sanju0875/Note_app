package com.example.notesharingapp.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.notesharingapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddFragment extends Fragment {

    private static final int FILE_SELECT_CODE = 1;

    private ImageView fileIconImageView;
    private TextView fileNameTextView;
    private EditText fileTitleEditText, fileTypeEditText;
    private Button uploadButton;
    private ProgressBar progressBar;

    private Uri fileUri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_fragement, container, false);

        fileIconImageView = view.findViewById(R.id.fileIconImageView);
        fileNameTextView = view.findViewById(R.id.fileNameTextView);
        fileTitleEditText = view.findViewById(R.id.fileTitleEditText);
        fileTypeEditText = view.findViewById(R.id.fileTypeEditText);
        uploadButton = view.findViewById(R.id.uploadButton);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("files");

        fileIconImageView.setOnClickListener(v -> openFileChooser());
        uploadButton.setOnClickListener(v -> uploadFile());

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        String[] mimeTypes = {"application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(Intent.createChooser(intent, "Select a PDF or DOCX file"), FILE_SELECT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_SELECT_CODE && resultCode == Activity.RESULT_OK && data != null) {
            fileUri = data.getData();
            if (fileUri != null) {
                String fileName = fileUri.getLastPathSegment();
                fileNameTextView.setText(fileName != null ? fileName : "No file selected");
            }
        }
    }

    private void uploadFile() {
        String title = fileTitleEditText.getText().toString().trim();
        String type = fileTypeEditText.getText().toString().trim();

        if (fileUri == null) {
            Toast.makeText(getContext(), "Please select a file", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(type)) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        uploadButton.setEnabled(false);
        String fileName = System.currentTimeMillis() + "_" + title + "." + type;

        StorageReference fileRef = storageReference.child(fileName);
        fileRef.putFile(fileUri).addOnSuccessListener(taskSnapshot ->
                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    saveFileDetailsToDatabase(title, type, uri.toString());
                    Toast.makeText(getContext(), "File uploaded successfully", Toast.LENGTH_SHORT).show();
                    resetFields();
                }).addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to get download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    uploadButton.setEnabled(true);
                })
        ).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "File upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            uploadButton.setEnabled(true);
        });
    }

    private void saveFileDetailsToDatabase(String title, String type, String downloadUrl) {
        String fileId = databaseReference.push().getKey();
        HashMap<String, String> fileDetails = new HashMap<>();
        fileDetails.put("title", title);
        fileDetails.put("type", type);
        fileDetails.put("url", downloadUrl);

        if (fileId != null) {
            databaseReference.child(fileId).setValue(fileDetails)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "File details saved to database", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Failed to save file details", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    private void resetFields() {
        fileNameTextView.setText("No file selected");
        fileTitleEditText.setText("");
        fileTypeEditText.setText("");
        uploadButton.setEnabled(true);
    }
}
