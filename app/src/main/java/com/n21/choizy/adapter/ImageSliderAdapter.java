package com.n21.choizy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.n21.choizy.R;
import com.n21.choizy.model.ImageSliderItem;
import com.n21.choizy.model.UpcomingAD;

import java.util.ArrayList;
import java.util.List;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder> {

    private List<UpcomingAD> list = new ArrayList<>();
    private ViewPager2 viewPager2;
    private Context context;


    public ImageSliderAdapter(List<ImageSliderItem> imageSliderItems, ViewPager2 viewPager2, Context context) {

        this.viewPager2 = viewPager2;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageSliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slide_item_container,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSliderViewHolder holder, int position) {
        holder.setImage(list.get(position));
        if(position == list.size() - 2){
            viewPager2.post(runnable);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ImageSliderViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView imageView;

        ImageSliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);

        }

        void setImage(UpcomingAD upcomingAD) {
//            imageView.setImageResource(imageSliderItem.getImage());
            Glide.with(context).load(upcomingAD.getUrl()).into(imageView);
        }
    }

    public void setImageSliderItems(List<UpcomingAD> UpcomingItems) {
        this.list = UpcomingItems;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            list.addAll(list);
            notifyDataSetChanged();
        }
    };


}
