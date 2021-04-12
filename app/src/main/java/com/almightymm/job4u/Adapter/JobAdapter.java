package com.almightymm.job4u.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.almightymm.job4u.R;
import com.almightymm.job4u.model.Job;

import java.util.ArrayList;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {
    private Context context;
    private ArrayList<Job> jobArrayList;

    public JobAdapter(Context context, ArrayList<Job> jobArrayList) {
        this.context = context;
        this.jobArrayList = jobArrayList;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.job_list_item, parent,false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobArrayList.get(position);
//        holder.jobTitleTextView.setText(job.getTitle());
        final String positionPlusOne = Integer.toString(position+1);
        holder.jobCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Position " + positionPlusOne, Toast.LENGTH_SHORT).show();
            }
        });
        holder.jobApplyNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Position " + positionPlusOne, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobArrayList.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder{
        TextView jobTitleTextView;
        CardView jobCardView;
        Button jobApplyNowButton;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitleTextView = itemView.findViewById(R.id.job_title);
            jobApplyNowButton = itemView.findViewById(R.id.job_apply_now);
            jobCardView = itemView.findViewById(R.id.job_card);
        }
    }

    public void filterList(ArrayList<Job> filteredList) {
        jobArrayList = filteredList;
        getItemCount();
        notifyDataSetChanged();
    }
}
