package com.almightymm.job4u.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.almightymm.job4u.R;
import com.almightymm.job4u.model.Candidate;
import com.almightymm.job4u.model.Job;
import com.almightymm.job4u.model.Notification;
import com.google.android.datatransport.cct.internal.LogEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private Context context;
    private ArrayList<Notification> notificationArrayList;
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;
    private static final String TAG = "NotificationAdapter";
    int i=0;

    public NotificationAdapter(Context context, ArrayList<Notification> notificationArrayList, SharedPreferences preferences, SharedPreferences.Editor preferenceEditor) {
        this.context = context;
        this.notificationArrayList = notificationArrayList;
        this.preferences = preferences;
        this.preferenceEditor = preferenceEditor;
        Log.e(TAG,"------>"+notificationArrayList.size() );
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.notification_list_item, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {

        Notification notification = notificationArrayList.get(position);
        Log.e(TAG, "onBindViewHolder: "+notification.getTitle() );
        holder.jobTitleTextView.setText(notification.getTitle());
        holder.jobBodyTextView.setText(notification.getBody());
        Log.e(TAG, "onBindViewHolder: "+notificationArrayList.size() );


    }

    @Override
    public int getItemCount() {
        return notificationArrayList.size();
    }

    public void filterList(ArrayList<Notification> filteredList) {
        notificationArrayList = filteredList;
        getItemCount();
        notifyDataSetChanged();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitleTextView, jobBodyTextView;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitleTextView = itemView.findViewById(R.id.txt_title);
            jobBodyTextView = itemView.findViewById(R.id.txt_body);
        }
    }
}
