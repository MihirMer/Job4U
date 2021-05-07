package com.almightymm.job4u.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.almightymm.job4u.R;
import com.almightymm.job4u.fragment.ProfileFragment;
import com.almightymm.job4u.latex.PreferenceHelper;
import com.almightymm.job4u.model.Candidate;
import com.almightymm.job4u.model.EducationDetails;
import com.almightymm.job4u.model.Notification;
import com.almightymm.job4u.utils.FilesUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.almightymm.job4u.fragment.ProfileFragment.WRITE_EXTERNAL_STORAGE_PERMISSION;

public class HRResponsesAdapter extends RecyclerView.Adapter<HRResponsesAdapter.CandidateViewHolder> {
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;
    private Context context;
    private ArrayList<Candidate> candidateArrayList;
    Activity activity;
    String userId, jobId;

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private static final String TAG = "HRResponsesAdapter";
    public HRResponsesAdapter(Context context, Activity activity, ArrayList<Candidate> candidateArrayList, SharedPreferences preferences, SharedPreferences.Editor preferenceEditor) {
        this.preferences = preferences;
        this.preferenceEditor = preferenceEditor;
        this.context = context;
        this.candidateArrayList = candidateArrayList;
        this.activity = activity;
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
    public void onBindViewHolder(@NonNull CandidateViewHolder holder, final int position) {
        final Candidate candidate = candidateArrayList.get(position);
        holder.candidateName.setText(candidate.getName());
        holder.candidateCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkStoragePermissions(ProfileFragment.Permissions.SAVE)) {
                    downloadResume(candidate.getId(), candidate.getName());
                }
            }
        });
        userId = preferences.getString("userId", "");
        jobId = preferences.getString("jobId","");
        if (!candidate.getStatus().equals("Applied")){
            holder.status.setText("Candidate "+candidate.getStatus());
            if (candidate.getStatus().equals("Rejected")){
                holder.status.setTextColor(Color.RED);
            }
            holder.status.setVisibility(View.VISIBLE);
            holder.rejectCandidate.setVisibility(View.GONE);
            holder.acceptCandidate.setVisibility(View.GONE);
        }
        holder.rejectCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference candidate_ref = FirebaseDatabase.getInstance().getReference().child("HR").child("RESPONSES").child(jobId).child(candidate.getId());
               candidate_ref.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                   @Override
                   public void onSuccess(DataSnapshot dataSnapshot) {
                        Candidate candidate2 = dataSnapshot.getValue(Candidate.class);
                        if (candidate2 !=null){
                        candidate2.setStatus("Rejected");
                        candidate_ref.setValue(candidate2);
                        }
                   }
               });
                final DatabaseReference candidate_ref_j = FirebaseDatabase.getInstance().getReference().child("Users").child(candidate.getId()).child("APPLIED_JOB").child(jobId);
                candidate_ref.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        Candidate candidate2 = dataSnapshot.getValue(Candidate.class);
                        if (candidate2 !=null){
                            candidate2.setStatus("Rejected");
                            candidate2.setId(jobId);
                            candidate_ref_j.setValue(candidate2);
                        }
                    }
                });

                    String title = "Sorry!  Your application is rejected";
                    String body = "Better luck next time!!!";
                sendNotification(jobId+candidate.getId(),title, body);

//                database entry for notification

               DatabaseReference db_notif = FirebaseDatabase.getInstance().getReference("Users").child(candidate.getId()).child("NOTIFICATIONS");
                String id = db_notif.push().getKey();

                Notification notif_object = new Notification(title, body, candidate.getId(), jobId);

                db_notif.child(id).setValue(notif_object).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                        Toast.makeText(context, "Details Save", Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
        holder.acceptCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference candidate_ref = FirebaseDatabase.getInstance().getReference().child("HR").child("RESPONSES").child(jobId).child(candidate.getId());
                candidate_ref.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        Candidate candidate2 = dataSnapshot.getValue(Candidate.class);
                        if (candidate2 !=null){

                            candidate2.setStatus("Selected");
                            candidate_ref.setValue(candidate2);
                        }
                    }
                });
                final DatabaseReference candidate_ref_j = FirebaseDatabase.getInstance().getReference().child("Users").child(candidate.getId()).child("APPLIED_JOB").child(jobId);
                candidate_ref.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        Candidate candidate2 = dataSnapshot.getValue(Candidate.class);
                        if (candidate2 !=null){
                            candidate2.setStatus("Selected");
                            candidate2.setId(jobId);
                            candidate_ref_j.setValue(candidate2);
                        }
                    }
                });

//                here to send notification of selection
                String title = "Congratulations! You are selected!!!";
                String body ="Your job application is accepted by the company. ";
                sendNotification(jobId+candidate.getId(),title,body);
//                lakhi nakh aama
                DatabaseReference db_notif = FirebaseDatabase.getInstance().getReference("Users").child(candidate.getId()).child("NOTIFICATIONS");
                String id = db_notif.push().getKey();

                Notification notif_object = new Notification(title, body, candidate.getId(), jobId);

                db_notif.child(id).setValue(notif_object).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                        Toast.makeText(context, "Details Save", Toast.LENGTH_LONG).show();

                    }
                });
            }
        });



    }



//    jarur nthi message alg rakh bs   pn method to call krvi pdse n? ha

    private void sendNotification(String subTopic, String title, String body) {


        //RequestQueue initialized
        String url = "http://192.168.43.240:3000/job_notification?topic="+subTopic+"&title="+title+"&body="+body;
        mRequestQueue = Volley.newRequestQueue(context);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(context,"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i(TAG,"Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);
    }
    private boolean checkStoragePermissions(ProfileFragment.Permissions type) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_PERMISSION);
            return false;
        }
        return true;
    }
    private void downloadResume(final String id, final String candidateName) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Resumes").child(id);
        File localFile = null;
        try {
            localFile = File.createTempFile("resumes", "pdf");
            localFile.deleteOnExit();
            final File finalLocalFile = localFile;
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been create
                    openResultingFile(finalLocalFile, candidateName);
                    finalLocalFile.deleteOnExit();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void openResultingFile(File file, String candidateName) {
        final String outputFolderPath = PreferenceHelper.getOutputFolder(context);
        byte[] bytes = FilesUtils.readBinaryFile(file);
        candidateName = "Resume of " + candidateName;
        candidateName = candidateName.replaceAll(" ", "_");
        File pdf = new File(outputFolderPath, candidateName + ".pdf");
        FilesUtils.writeBinaryFile(pdf, bytes);
        Intent pdfIntent = new Intent();
        pdfIntent.setAction(Intent.ACTION_VIEW);

        pdfIntent.setDataAndType(Uri.fromFile(pdf), "application/pdf");

        Uri apkURI = FileProvider.getUriForFile(
                context,
                context.getPackageName() + ".provider", pdf);
        pdfIntent.setDataAndType(apkURI, "application/pdf");
        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (pdfIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(pdfIntent);
        } else {
            Toast.makeText(context, "You don't have any pdf reader!",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return candidateArrayList.size();
    }

    public static class CandidateViewHolder extends RecyclerView.ViewHolder {
        TextView candidateName,status;
        Button acceptCandidate, rejectCandidate;
        CardView candidateCardView;

        public CandidateViewHolder(@NonNull View itemView) {
            super(itemView);
            candidateName = itemView.findViewById(R.id.txt_candidate_name);
            candidateCardView = itemView.findViewById(R.id.candidate_card);
            acceptCandidate = itemView.findViewById(R.id.btn_candidate_accept);
            rejectCandidate = itemView.findViewById(R.id.btn_candidate_reject);
            status = itemView.findViewById(R.id.status);
        }
    }


}
