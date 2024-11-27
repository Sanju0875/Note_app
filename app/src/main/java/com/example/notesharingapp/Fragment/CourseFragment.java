package com.example.notesharingapp.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesharingapp.Adapter.CourseAdapter;
import com.example.notesharingapp.Model.Course;
import com.example.notesharingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CourseFragment extends Fragment {

    private static final String TAG = "CourseFragment";

    private AutoCompleteTextView semesterDropdown, subjectDropdown;
    private RecyclerView coursesRecyclerView;

    private DatabaseReference databaseReference;
    private CourseAdapter courseAdapter;
    private ArrayList<Course> courseList;

    @Override
    public View onCreateView(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_fragement, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        semesterDropdown = view.findViewById(R.id.semesterTextView);
        subjectDropdown = view.findViewById(R.id.subjectNameTextView);
        coursesRecyclerView = view.findViewById(R.id.coursesRecyclerView);

        // Initialize RecyclerView
        courseList = new ArrayList<>();
        courseAdapter = new CourseAdapter(requireContext(), courseList);
        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        coursesRecyclerView.setAdapter(courseAdapter);

        // Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("files");

        // Set up Faculty Dropdown
        setupFacultyDropdown();
    }

    private void setupFacultyDropdown() {
        databaseReference.child("faculties").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> faculties = new ArrayList<>();
                for (DataSnapshot facultySnapshot : snapshot.getChildren()) {
                    String facultyName = facultySnapshot.getKey(); // Assuming the faculty names are keys
                    if (facultyName != null) {
                        faculties.add(facultyName);
                    }
                }
//                ArrayAdapter<String> facultyAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, faculties);
//                facultyDropdown.setAdapter(facultyAdapter);
//                facultyDropdown.setOnItemClickListener((parent, view, position, id) -> {
//                    String selectedFaculty = faculties.get(position);
//                    semesterDropdown.setVisibility(View.VISIBLE);
//                    populateSemesterDropdown(selectedFaculty);
//                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to fetch faculties: " + error.getMessage());
                Toast.makeText(requireContext(), "Failed to load faculties.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateSemesterDropdown(String faculty) {
        databaseReference.child("faculties").child(faculty).child("semesters")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<String> semesters = new ArrayList<>();
                        for (DataSnapshot semesterSnapshot : snapshot.getChildren()) {
                            String semesterName = semesterSnapshot.getKey();
                            if (semesterName != null) {
                                semesters.add(semesterName);
                            }
                        }
                        ArrayAdapter<String> semesterAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, semesters);
                        semesterDropdown.setAdapter(semesterAdapter);
                        semesterDropdown.setOnItemClickListener((parent, view, position, id) -> {
                            String selectedSemester = semesters.get(position);
                            subjectDropdown.setVisibility(View.VISIBLE);
                            populateSubjectDropdown(faculty, selectedSemester);
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Failed to fetch semesters: " + error.getMessage());
                        Toast.makeText(requireContext(), "Failed to load semesters.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populateSubjectDropdown(String faculty, String semester) {
        databaseReference.child("faculties").child(faculty).child("semesters").child(semester).child("subjects")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<String> subjects = new ArrayList<>();
                        for (DataSnapshot subjectSnapshot : snapshot.getChildren()) {
                            String subjectName = subjectSnapshot.getKey();
                            if (subjectName != null) {
                                subjects.add(subjectName);
                            }
                        }
                        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, subjects);
                        subjectDropdown.setAdapter(subjectAdapter);
                        subjectDropdown.setOnItemClickListener((parent, view, position, id) -> {
                            String selectedSubject = subjects.get(position);
                            coursesRecyclerView.setVisibility(View.VISIBLE);
                            fetchNotesFromFirebase(selectedSubject);
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Failed to fetch subjects: " + error.getMessage());
                        Toast.makeText(requireContext(), "Failed to load subjects.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchNotesFromFirebase(String subject) {
        courseList.clear();

        databaseReference.child("files").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot fileSnapshot : snapshot.getChildren()) {
                    String fileSubject = fileSnapshot.child("subject").getValue(String.class);
                    if (fileSubject != null && fileSubject.equalsIgnoreCase(subject)) {
                        String title = fileSnapshot.child("title").getValue(String.class);
                        String fileSemester = fileSnapshot.child("semester").getValue(String.class);
                        String fileFaculty = fileSnapshot.child("faculty").getValue(String.class);

                        if (title != null && fileSemester != null && fileFaculty != null) {
                            courseList.add(new Course(
                                    R.drawable.book,
                                    title,
                                    subject,
                                    fileSemester,
                                    fileFaculty
                            ));
                        }
                    }
                }

                if (courseList.isEmpty()) {
                    Toast.makeText(requireContext(), "No notes available for " + subject, Toast.LENGTH_SHORT).show();
                }

                courseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to fetch notes: " + error.getMessage());
                Toast.makeText(requireContext(), "Failed to fetch notes.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
