package com.example.notesharingapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesharingapp.Model.Course;
import com.example.notesharingapp.R;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private Context context;
    private List<Course> courseList;

    public CourseAdapter(Context context, List<Course> courseList) {
        this.context = context;
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_item, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);

        holder.noteImageView.setImageResource(course.getNoteImage());
        holder.noteTitleTextView.setText(course.getNoteTitle());
        holder.subjectNameTextView.setText("Subject: " + course.getSubjectName());
        holder.semesterTextView.setText("Semester: " + course.getSemester());
        holder.facultyTextView.setText("Faculty: " + course.getFaculty());
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {

        ImageView noteImageView;
        TextView noteTitleTextView, subjectNameTextView, semesterTextView, facultyTextView;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);

            noteImageView = itemView.findViewById(R.id.noteImageView);
            noteTitleTextView = itemView.findViewById(R.id.noteTitleTextView);
            subjectNameTextView = itemView.findViewById(R.id.subjectNameTextView);
            semesterTextView = itemView.findViewById(R.id.semesterTextView);
            facultyTextView = itemView.findViewById(R.id.facultyTextView);
        }
    }
}
