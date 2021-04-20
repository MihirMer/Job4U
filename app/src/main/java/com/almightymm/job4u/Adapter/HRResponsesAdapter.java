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
import androidx.recyclerview.widget.RecyclerView;

import com.almightymm.job4u.R;
import com.almightymm.job4u.model.Candidate;
import com.almightymm.job4u.model.Job;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HRResponsesAdapter extends RecyclerView.Adapter<HRResponsesAdapter.CandidateViewHolder> {
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;
    private Context context;
    private ArrayList<Candidate> candidateArrayList;

    public HRResponsesAdapter(Context context, ArrayList<Candidate> candidateArrayList, SharedPreferences preferences, SharedPreferences.Editor preferenceEditor) {
        this.preferences = preferences;
        this.preferenceEditor = preferenceEditor;
        this.context = context;
        this.candidateArrayList = candidateArrayList;
    }

    public void filterList(ArrayList<Candidate> filteredList) {
        candidateArrayList = filteredList;
        getItemCount();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CandidateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.candidate_list_item, parent, false);
        return new HRResponsesAdapter.CandidateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CandidateViewHolder holder, int position) {
        final Candidate candidate = candidateArrayList.get(position);
        holder.candidateName.setText(candidate.getName());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(candidate.getId());
        databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return candidateArrayList.size();
    }

    public static class CandidateViewHolder extends RecyclerView.ViewHolder {
        TextView candidateName;
        Button acceptCandidate, rejectCandidate;
        CardView candidateCardView;

        public CandidateViewHolder(@NonNull View itemView) {
            super(itemView);
            candidateName = itemView.findViewById(R.id.txt_candidate_name);
            candidateCardView = itemView.findViewById(R.id.candidate_card);
            acceptCandidate = itemView.findViewById(R.id.btn_candidate_accept);
            rejectCandidate = itemView.findViewById(R.id.btn_candidate_reject);
        }
    }
}
