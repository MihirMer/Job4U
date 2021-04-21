package com.almightymm.job4u.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.almightymm.job4u.R;
import com.almightymm.job4u.model.Job;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AppliedJobAdapter extends RecyclerView.Adapter<AppliedJobAdapter.JobViewHolder> {
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;
    private Context context;
    private ArrayList<Job> jobArrayList;

    public AppliedJobAdapter(Context context, ArrayList<Job> jobArrayList, SharedPreferences preferences, SharedPreferences.Editor preferenceEditor) {
        this.context = context;
        this.jobArrayList = jobArrayList;
        this.preferences = preferences;
        this.preferenceEditor = preferenceEditor;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.applied_job_list_item, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, final int position) {
        final Job job = jobArrayList.get(position);
       /* if (preferences.getString("role", "").equals("HR")) {
            holder.jobApplyNowButton.setVisibility(View.GONE);
        } else {
            holder.jobApplyNowButton.setVisibility(View.VISIBLE);
        }*/
        holder.jobTitleTextView.setText(job.getName());
        holder.jobCompanyTextView.setText(job.getCompanyName());
        holder.jobLocationTextView.setText(job.getCity());
        holder.salaryTextView.setText(job.getSalary());
        holder.descriptionTextView.setText(job.getDescription());
        final String positionPlusOne = Integer.toString(position + 1);
        holder.jobCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "Position " + positionPlusOne, Toast.LENGTH_SHORT).show();
//                HomeFragmentDirections.ActionHomeFragmentToJobPreviewFragment action = HomeFragmentDirections.actionHomeFragmentToJobPreviewFragment();
//                action.setJobId(job.getId());
//                Navigation.findNavController(v).navigate(action);
                preferenceEditor.putString("companyId", job.getCompanyId());
                preferenceEditor.putString("jobId", job.getId());
                preferenceEditor.apply();
                preferenceEditor.commit();
                if (preferences.getString("role", "").equals("HR")) {
                    Navigation.findNavController(v).navigate(R.id.action_HRHomeFragment_to_jobPreviewFragment);
                }else{
                    Navigation.findNavController(v).navigate(R.id.action_appliedJobFragment_to_jobPreviewFragment);
                }
            }
        });
        final String userId = preferences.getString("userId", "");
       /* holder.jobApplyNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference saved_job_ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("SAVED_JOB").child(job.getId());
                saved_job_ref.removeValue();
                jobArrayList.remove(position);
                getItemCount();
                notifyDataSetChanged();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return jobArrayList.size();
    }

    public void filterList(ArrayList<Job> filteredList) {
        jobArrayList = filteredList;
        getItemCount();
        notifyDataSetChanged();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitleTextView, jobCompanyTextView;
        TextView jobLocationTextView, salaryTextView, descriptionTextView;
        CardView jobCardView;
        //Button jobApplyNowButton;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitleTextView = itemView.findViewById(R.id.job_title);
            jobCompanyTextView = itemView.findViewById(R.id.txt_company_name);
            //jobApplyNowButton = itemView.findViewById(R.id.btn_remove_saved);
            jobLocationTextView = itemView.findViewById(R.id.txt_location);
            salaryTextView = itemView.findViewById(R.id.txt_salary);
            descriptionTextView = itemView.findViewById(R.id.txt_description);

            jobCardView = itemView.findViewById(R.id.job_card);
        }
    }
}
