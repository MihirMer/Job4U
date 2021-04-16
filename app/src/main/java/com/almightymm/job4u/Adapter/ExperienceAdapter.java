package com.almightymm.job4u.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.almightymm.job4u.R;
import com.almightymm.job4u.model.EducationDetails;
import com.almightymm.job4u.model.WorkExperience;
import com.google.firebase.installations.internal.FidListener;

import java.util.ArrayList;

public class ExperienceAdapter extends RecyclerView.Adapter<ExperienceAdapter.ExperienceViewHolder> {
    private final Context context;
    private ArrayList<WorkExperience> workExperiences;

    public ExperienceAdapter(Context context, ArrayList<WorkExperience> workExperiences) {
        this.context = context;
        this.workExperiences = workExperiences;
    }

    @NonNull
    @Override
    public ExperienceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.experience_layout, parent,false);
        return new ExperienceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExperienceViewHolder holder, int position) {
        WorkExperience workExperience = workExperiences.get(position);
        holder.Experience.setText("I have " + workExperience.getYearsOfExperience() + " year experience as a " + workExperience.getDesignation() + " in " +workExperience.getCompanyName() + " which is located in " + workExperience.getCity());
        holder.duration.setText("Duration : " + workExperience.getFromYear() + " to " + workExperience.getToYear());
        if (position == getItemCount()-1){
            holder.sep.setVisibility(View.GONE);
        }
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_workExperienceFragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return workExperiences.size();
    }


    public static class ExperienceViewHolder extends RecyclerView.ViewHolder {

        TextView Experience, duration;
        ImageView edit;
        View sep;
        public ExperienceViewHolder(@NonNull View view) {
            super(view);
            Experience = view.findViewById(R.id.txt_my_experience);
            duration = view.findViewById(R.id.txt_my_duration);
            sep = view.findViewById(R.id.sep);
            edit = view.findViewById(R.id.icon2);
        }
    }
}
