package com.almightymm.job4u.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.almightymm.job4u.R;
import com.almightymm.job4u.model.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private Context context;
    private ArrayList<Category> categoryArrayList;

    public CategoryAdapter(Context context, ArrayList<Category> categoryArrayList) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
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
        Category category = categoryArrayList.get(position);
        holder.categoryTitleTextView.setText(category.getC_Job_name());
        holder.categoryImageView.setImageResource(category.getC_Image());
        holder.categoryCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String positionPlusOne = Integer.toString(position+1);
                Toast.makeText(context, "Position " + positionPlusOne, Toast.LENGTH_SHORT).show();
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
