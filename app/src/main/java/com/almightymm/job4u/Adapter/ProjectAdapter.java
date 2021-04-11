package com.almightymm.job4u.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.almightymm.job4u.R;
import com.almightymm.job4u.model.ProjectWork;

import java.util.ArrayList;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
    private final Context context;
    private ArrayList<ProjectWork> projectWorks;

    public ProjectAdapter(Context context, ArrayList<ProjectWork> projectWorks) {
        this.context = context;
        this.projectWorks = projectWorks;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.project_layout, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        ProjectWork projectWork = projectWorks.get(position);
        holder.project_name.setText(projectWork.getProjectName());
        holder.project_description.setText(projectWork.getDescription());
        holder.start_year.setText("Duration " + " : " + projectWork.getStartYear() + " " + "to" + " " + projectWork.getEndYear());

    }

    @Override
    public int getItemCount() {
        return projectWorks.size();
    }


    public static class ProjectViewHolder extends RecyclerView.ViewHolder {

        TextView info2, project_name, project_description, start_year, end_year;

        public ProjectViewHolder(@NonNull View view) {
            super(view);
            project_name = view.findViewById(R.id.txt_my_project_name);
            project_description = view.findViewById(R.id.txt_my_project_description);
            start_year = view.findViewById(R.id.txt_my_project_start_year);
            end_year = view.findViewById(R.id.txt_my_project_end_year);
        }
    }


}
