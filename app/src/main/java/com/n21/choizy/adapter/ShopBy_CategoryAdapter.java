package com.n21.choizy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.n21.choizy.R;
import com.n21.choizy.model.Category;

import java.util.ArrayList;

public class ShopBy_CategoryAdapter extends RecyclerView.Adapter<ShopBy_CategoryAdapter.View_Holder> {
    ArrayList<Category> list = new ArrayList<>();
    onItemClick listener;

    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopby_category_card,parent,false);

        return new View_Holder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, int position) {
        Category category = list.get(position);
        holder.categoryName.setText(category.getCategory_name());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class View_Holder extends RecyclerView.ViewHolder {
        TextView categoryName;
        public View_Holder(@NonNull View itemView,onItemClick listener) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.shopBy_category_cardTxt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!= null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onCategorySelected(position);
                        }
                    }
                }
            });



        }
    }

    public void setList(ArrayList<Category> list) {
        this.list = list;
    }

    public interface onItemClick{
        void onCategorySelected(int position);
    }

    public void setListener(onItemClick listener) {
        this.listener = listener;
    }
}
