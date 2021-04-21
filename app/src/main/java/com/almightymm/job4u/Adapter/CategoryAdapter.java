package com.almightymm.job4u.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.almightymm.job4u.R;
import com.almightymm.job4u.model.Category;

import java.util.ArrayList;
import java.util.prefs.Preferences;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private Context context;
    private ArrayList<Category> categoryArrayList;
    private SharedPreferences preferences;
    private SharedPreferences.Editor preferenceEditor;
    private int navigation;

    public CategoryAdapter(Context context, ArrayList<Category> categoryArrayList,SharedPreferences preferences, SharedPreferences.Editor preferenceEditor,int navigation) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
        this.preferences = preferences;
        this.preferenceEditor = preferenceEditor;
        this.navigation = navigation;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.category_list_item, parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, final int position) {
        final Category category = categoryArrayList.get(position);
        holder.categoryTitleTextView.setText(category.getC_Job_name());
        holder.categoryImageView.setImageResource(category.getC_Image());
        holder.categoryCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "Position " + positionPlusOne, Toast.LENGTH_SHORT).show();
//                HomeFragmentDirections.ActionHomeFragmentToJobPreviewFragment action = HomeFragmentDirections.actionHomeFragmentToJobPreviewFragment();
//                action.setJobId(job.getId());
//                Navigation.findNavController(v).navigate(action);
                preferenceEditor.putString("categoryName", category.getC_Job_name());
                preferenceEditor.apply();
                preferenceEditor.commit();
                if(navigation == R.id.action_searchFragment_to_jobListFragment){
                Navigation.findNavController(v).navigate(R.id.action_searchFragment_to_jobListFragment);
                }else if (navigation == R.o)

            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{
        TextView categoryTitleTextView;
        ImageView categoryImageView;
        CardView categoryCardView;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryCardView = itemView.findViewById(R.id.category_card);
            categoryImageView = itemView.findViewById(R.id.category_image);
            categoryTitleTextView = itemView.findViewById(R.id.category_title);
        }
    }

    public void filterList(ArrayList<Category> filteredList) {
        categoryArrayList = filteredList;
        getItemCount();
        notifyDataSetChanged();
    }



}
