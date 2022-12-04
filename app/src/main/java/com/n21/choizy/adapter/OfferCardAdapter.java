package com.n21.choizy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.n21.choizy.R;
import com.n21.choizy.model.Offer;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class OfferCardAdapter extends RecyclerView.Adapter<OfferCardAdapter.Viewholder> {

    ArrayList<Offer> list = new ArrayList<>();
    String companyImgLink=null;
    OfferCardListener listener;


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_card,parent,false);
        return new Viewholder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Offer offer = list.get(position);
        holder.offerTitle.setText(offer.getTitle());

        Glide.with(holder.offerImg).load(offer.getOfferUrl()).into(holder.offerImg);
        if(companyImgLink != null){
            Glide.with(holder.compImg).load(companyImgLink).into(holder.compImg);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView offerImg;
        TextView offerTitle;
        CircleImageView compImg;
        public Viewholder(@NonNull View itemView,OfferCardListener listener) {
            super(itemView);
            offerImg = itemView.findViewById(R.id.offerCard_offerImg);
            offerTitle = itemView.findViewById(R.id.offerCard_OfferTitle);
            compImg = itemView.findViewById(R.id.offerCard_CompanyImg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });


        }
    }

    public void setList(ArrayList<Offer> list) {
        this.list = list;
    }

    public void setCompanyImgLink(String companyImgLink) {
        this.companyImgLink = companyImgLink;
    }

    public interface OfferCardListener{
        void onItemClick(int position);
    }

    public void setListener(OfferCardListener listener) {
        this.listener = listener;
    }
}
