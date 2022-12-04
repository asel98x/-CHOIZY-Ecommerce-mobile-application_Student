package com.n21.choizy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.n21.choizy.DBClass;
import com.n21.choizy.R;
import com.n21.choizy.StudentDB_Helper;
import com.n21.choizy.model.Cart;
import com.n21.choizy.model.Offer;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;

public class CartCardAdapter extends RecyclerView.Adapter<CartCardAdapter.Viewholder> {
    ArrayList<Cart> list = new ArrayList<>();


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_card,parent,false);

        return new Viewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Cart cart = list.get(position);
        holder.offerTitle.setText(cart.getOffer().getTitle());
        holder.offerPrice.setText(StudentDB_Helper.getFormattedPrice(cart.getOffer().getPrice()*cart.getQut()));
        holder.offerQut.setText("X"+cart.getQut());
        Glide.with(holder.offerImg).load(cart.getOffer().getOfferUrl()).into(holder.offerImg);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        ImageView offerImg;
        TextView offerTitle,offerPrice,offerQut;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            offerTitle = itemView.findViewById(R.id.Cart_OfferTitle);
            offerPrice = itemView.findViewById(R.id.Cart_OfferPrice);
            offerImg = itemView.findViewById(R.id.Cart_OfferImg);
            offerQut = itemView.findViewById(R.id.Cart_OfferQut);
        }
    }

    public void setList(ArrayList<Cart> list) {
        this.list = list;
    }
}
