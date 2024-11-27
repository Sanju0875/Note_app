package com.example.notesharingapp.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import java.util.HashMap;

public class AddFragment extends Fragment {

    private static final int FILE_SELECT_CODE = 1;

    private ImageView fileIconImageView;
    private TextView fileNameTextView;
    private EditText fileTitleEditText, facultyEditText, subjectEditText;
    private AutoCompleteTextView semesterDropdown;
    private Button uploadButton;
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
        facultyEditText = view.findViewById(R.id.facultyEditText);
        subjectEditText = view.findViewById(R.id.subjectEditText);
        semesterDropdown = view.findViewById(R.id.semesterDropdown);
        uploadButton = view.findViewById(R.id.uploadButton);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("files");

        setupSemesterDropdown();

        fileIconImageView.setOnClickListener(v -> openFileChooser());
        uploadButton.setOnClickListener(v -> uploadFile());

        return view;
    }

    private void setupSemesterDropdown() {
        String[] semesters = {"First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh", "Eighth"};
        ArrayAdapter<String> semesterAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, semesters);
        semesterDropdown.setAdapter(semesterAdapter);
        semesterDropdown.setOnClickListener(v -> semesterDropdown.showDropDown());
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
        String faculty = facultyEditText.getText().toString().trim();
        String semester = semesterDropdown.getText().toString().trim();
        String subject = subjectEditText.getText().toString().trim();

        if (fileUri == null) {
            Toast.makeText(getContext(), "Please select a file", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(faculty) || TextUtils.isEmpty(semester) || TextUtils.isEmpty(subject)) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        uploadButton.setEnabled(false);
        String fileName = System.currentTimeMillis() + "_" + title;

        StorageReference fileRef = storageReference.child(fileName);
        fileRef.putFile(fileUri).addOnSuccessListener(taskSnapshot ->
                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    saveFileDetailsToDatabase(title, faculty, semester, subject, uri.toString());
                    Toast.makeText(getContext(), "File uploaded successfully", Toast.LENGTH_SHORT).show();
                    resetFields();
                })).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            uploadButton.setEnabled(true);
        });
    }

    private void saveFileDetailsToDatabase(String title, String faculty, String semester, String subject, String fileUrl) {
        HashMap<String, Object> fileDetails = new HashMap<>();
        fileDetails.put("title", title);
        fileDetails.put("faculty", faculty);
        fileDetails.put("semester", semester);
        fileDetails.put("subject", subject);
        fileDetails.put("fileUrl", fileUrl);

        databaseReference.push().setValue(fileDetails);
    }

    private void resetFields() {
        fileUri = null;
        fileNameTextView.setText("No file selected");
        fileTitleEditText.setText("");
        facultyEditText.setText("");
        semesterDropdown.setText("");
        subjectEditText.setText("");
        uploadButton.setEnabled(true);
    }
}
