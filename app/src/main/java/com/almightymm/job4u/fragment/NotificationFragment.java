package com.almightymm.job4u.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.almightymm.job4u.Adapter.EducationAdapter;
import com.almightymm.job4u.Adapter.NotificationAdapter;
import com.almightymm.job4u.R;
import com.almightymm.job4u.model.EducationDetails;
import com.almightymm.job4u.model.Notification;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class NotificationFragment extends Fragment {

    private static final String TAG = "NotificationFragment";
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;
    String userId;
    RelativeLayout noData;
    RecyclerView edu_rec;
    ArrayList<Notification> notificationArrayList;
    NotificationAdapter notificationAdapter;
    LinearLayoutManager notificationLinearLayoutManager;

    public NotificationFragment() {
        // Required empty public constructor
    }

    RecyclerView notification;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        initPreferences();
        notification = view.findViewById(R.id.notification);
        noData = view.findViewById(R.id.lay4);
        notificationArrayList = new ArrayList<>();
        notificationLinearLayoutManager = new LinearLayoutManager(getContext());
        notificationLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        notification.setLayoutManager(notificationLinearLayoutManager);
            userId = preferences.getString("userId","");
        notificationAdapter  =  new NotificationAdapter(getContext(), notificationArrayList, preferences, preferenceEditor);

        notification.setAdapter(notificationAdapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("NOTIFICATIONS");
        databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot snapshot) {
                Log.e(TAG, "onSuccess: .." );
                notificationArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Notification notification = dataSnapshot.getValue(Notification.class);
                    Log.e(TAG, "onSuccess: hello"+notification.getTitle() );
                    notificationArrayList.add(notification);
                }
                if (notificationArrayList.isEmpty()){
                    noData.setVisibility(View.VISIBLE);
                } else{
                    noData.setVisibility(View.GONE);
                }
                notificationAdapter.filterList(notificationArrayList);


            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notificationArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Notification notification = dataSnapshot.getValue(Notification.class);
                    Log.e(TAG, "onSuccess: hello"+notification.getTitle() );
                    notificationArrayList.add(notification);
                }
                if (notificationArrayList.isEmpty()){
                    noData.setVisibility(View.VISIBLE);
                } else{
                    noData.setVisibility(View.GONE);
                }
                notificationAdapter.filterList(notificationArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void initPreferences() {
        preferences = getActivity().getSharedPreferences("User_Details", MODE_PRIVATE);
        preferenceEditor = preferences.edit();
    }
}