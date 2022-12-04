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
import com.n21.choizy.model.RecommendedAD;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Recommended_cardAdapter extends RecyclerView.Adapter<Recommended_cardAdapter.View_Holder> {
    ArrayList<RecommendedAD> list = new ArrayList<>();

    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_card,parent,false);
        return new View_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, int position) {
        RecommendedAD ad = list.get(position);
        holder.recommendedTxt.setText(ad.getTitle());
        Glide.with(holder.recoAD).load(ad.getUrl()).centerCrop().into(holder.recoAD);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class RecommendedDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.right =50;
        }
    }

    public class View_Holder extends RecyclerView.ViewHolder {
        ImageView recoAD;
        TextView recommendedTxt;
        public View_Holder(@NonNull View itemView) {
            super(itemView);
            recoAD = itemView.findViewById(R.id.recommendedIMG);
            recommendedTxt = itemView.findViewById(R.id.recommendedTxt);


        }
    }

    public void setList(ArrayList<RecommendedAD> list) {
        this.list = list;
    }
}
