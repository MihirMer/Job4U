package com.almightymm.job4u.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.almightymm.job4u.R;
import com.almightymm.job4u.model.EducationDetails;

import java.util.ArrayList;

public class EducationAdapter extends RecyclerView.Adapter<EducationAdapter.EducationViewHolder> {
    private final Context context;
    private ArrayList<EducationDetails> educationDetails;

    public EducationAdapter(Context context, ArrayList<EducationDetails> educationDetails) {
        this.context = context;
        this.educationDetails = educationDetails;
    }

    @NonNull
    @Override
    public EducationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.education_details_layout, parent, false);
        return new EducationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EducationViewHolder holder, int position) {
        EducationDetails details = educationDetails.get(position);

        holder.college.setText(details.getCollegeName());
        holder.streem.setText(details.getStream());
        holder.degree.setText(details.getDegreeName() + " in ");
        holder.gyear.setText("Graduation Year" + " : " + details.getYear());
        holder.cgpa.setText("CGPA" + " : " + details.getGpa());
        holder.achievements.setText("Achievements" + " : " + details.getOtherAchievements());
    }

    @Override
    public int getItemCount() {
        return educationDetails.size();
    }


    public static class EducationViewHolder extends RecyclerView.ViewHolder {

        TextView info1, college, streem, degree, cgpa, gyear_txt, gyear, achievements;

        public EducationViewHolder(@NonNull View view) {
            super(view);
            college = view.findViewById(R.id.txt_my_college);
            streem = view.findViewById(R.id.txt_my_streem);
            degree = view.findViewById(R.id.txt_my_degree);
            gyear = view.findViewById(R.id.txt_my_gyear);
            cgpa = view.findViewById(R.id.txt_my_cgpa);
            achievements = view.findViewById(R.id.txt_my_achievements);
        }
    }


}
