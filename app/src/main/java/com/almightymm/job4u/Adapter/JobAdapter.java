package com.almightymm.job4u.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.almightymm.job4u.R;
import com.almightymm.job4u.model.Candidate;
import com.almightymm.job4u.model.Job;
import com.almightymm.job4u.model.SavedJob;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;
    int navigation;
    private Context context;
    private ArrayList<Job> jobArrayList;


    public JobAdapter(Context context, ArrayList<Job> jobArrayList, SharedPreferences preferences, SharedPreferences.Editor preferenceEditor, int navigation) {
        this.preferences = preferences;
        this.preferenceEditor = preferenceEditor;
        this.context = context;
        this.jobArrayList = jobArrayList;
        this.navigation = navigation;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.job_list_item, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        final Job job = jobArrayList.get(position);

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
                Navigation.findNavController(v).navigate(navigation);
            }
        });
        holder.jobApplyNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferences.getBoolean("areaOfInterestSelected", false) && preferences.getBoolean("personalDetailsAdded", false) && preferences.getBoolean("keySkillAdded", false) && preferences.getBoolean("educationAdded", false) && preferences.getBoolean("projectWorkAdded", false)) {
//                Toast.makeText(context, "Position " + positionPlusOne, Toast.LENGTH_SHORT).show();
                    final String userId = preferences.getString("userId", "");
                    String userName = preferences.getString("firstName", "") + " " + preferences.getString("lastName", "");

                    Candidate candidate = new Candidate(userId, userName, "Applied");
                    DatabaseReference applyNowDbRef = FirebaseDatabase.getInstance().getReference().child("HR").child("RESPONSES").child(job.getId());
                    applyNowDbRef.child(userId).setValue(candidate).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            SavedJob savedJob = new SavedJob(job.getId());
                            FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("APPLIED_JOB").child(job.getId()).setValue(savedJob).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                            Toast.makeText(context, "Resume sent", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
                else{
                    Toast.makeText(context, "Please complete your profile details first !", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        Button jobApplyNowButton;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitleTextView = itemView.findViewById(R.id.job_title);
            jobCompanyTextView = itemView.findViewById(R.id.txt_company_name);
            jobApplyNowButton = itemView.findViewById(R.id.job_apply_now);
            jobLocationTextView = itemView.findViewById(R.id.txt_location);
            salaryTextView = itemView.findViewById(R.id.txt_salary);
            descriptionTextView = itemView.findViewById(R.id.txt_description);

            jobCardView = itemView.findViewById(R.id.job_card);
        }
    }
}
