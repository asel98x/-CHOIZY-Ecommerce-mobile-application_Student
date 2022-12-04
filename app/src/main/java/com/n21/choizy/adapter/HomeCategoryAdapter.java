package com.n21.choizy.adapter;

import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.n21.choizy.R;
import com.n21.choizy.model.Category;

import java.util.ArrayList;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.View_Holder> {

    ArrayList<Category> list = new ArrayList<>();
    onCategory listener;

    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_category_card,parent,false);
        return new View_Holder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, int position) {
        Category category = list.get(position);
        holder.categoryName.setText(category.getCategory_name());

        Glide.with(holder.categoryImg).load(category.getImageURL()).into(holder.categoryImg);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HCategoryDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
           // super.getItemOffsets(outRect, view, parent, state);
            if(parent.getChildAdapterPosition(view) == 1){
                outRect.left = 40;
            }
            outRect.right = 90;
        }
    }

    public static class View_Holder extends RecyclerView.ViewHolder {
        ImageView  categoryImg;
        TextView categoryName;
        public View_Holder(@NonNull View itemView,onCategory listener) {
            super(itemView);
            categoryImg = itemView.findViewById(R.id.hCatory_Img);
            categoryName = itemView.findViewById(R.id.hCatory_Name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onCategoryClick(position);
                        }
                    }
                }
            });


        }
    }

    public void setList(ArrayList<Category> list) {
        this.list = list;
    }

    public interface onCategory{
        void onCategoryClick(int position);
    }

    public void setListener(onCategory listener) {
        this.listener = listener;
    }
}
