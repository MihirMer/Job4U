package com.almightymm.job4u.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.almightymm.job4u.R;
import com.almightymm.job4u.model.Category;

import java.util.ArrayList;

public class JobCategoryAdapter extends RecyclerView.Adapter<JobCategoryAdapter.JobCategoryHolder>{
Context c;
    ArrayList<Category> categories;
    ArrayList<Category> checkJobCategories = new ArrayList<>();
    public JobCategoryAdapter(Context c, ArrayList<Category> categories) {
        this.c = c;
        this.categories = categories;
    }

    @NonNull
    @Override
    public JobCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_category,parent,false);
        JobCategoryHolder jobCategoryHolder = new JobCategoryHolder(view);

        return jobCategoryHolder;
    }
//DATA
    @Override
    public void onBindViewHolder(@NonNull JobCategoryHolder holder, int position) {
       holder.imageJob.setImageResource(categories.get(position).getC_Image());
       holder.textTitle.setText(categories.get(position).getC_Job_name());
      holder.setItemClickListener(new ItemClickListener() {
          @Override
          public void onItemClick(View v, int pos) {
              CheckBox checkBox= (CheckBox) v;
              if(checkBox.isChecked())
              {
                  checkJobCategories.add(categories.get(pos));
              }
              else if (!checkBox.isChecked())
              {
                  checkJobCategories.remove(categories.get(pos));
              }
          }
      });
    }
//file got ne
    @Override
    public int getItemCount() {
        return categories.size();
    }

    public ArrayList<Category> getList(){
        return  checkJobCategories;
    }



    public class JobCategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CheckBox checkBox;
        ImageView imageJob;
        TextView textTitle;

        ItemClickListener itemClickListener;

        public JobCategoryHolder(@NonNull View itemView) {
            super(itemView);
            imageJob =(ImageView)itemView.findViewById(R.id.img_job_image);
            checkBox =(CheckBox) itemView.findViewById(R.id.checkbox_job);
            textTitle =itemView.findViewById(R.id.txt_titleJob);
            checkBox.setOnClickListener(this);
        }
        public void setItemClickListener(ItemClickListener ic)
        {
            this.itemClickListener = ic;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v,getLayoutPosition());
        }
    }
}



