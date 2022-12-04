package com.n21.choizy.ui.purchase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.n21.choizy.R;
import com.n21.choizy.StudentDB_Helper;
import com.n21.choizy.model.Order;

import java.util.ArrayList;

public class OngoingCardAdapter extends RecyclerView.Adapter<OngoingCardAdapter.Viewholder> {
    ArrayList<Order> list = new ArrayList<>();
    ongoingListener listener;

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ongoing_order_card,parent,false);
        return new Viewholder(v,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Order order = list.get(position);
        holder.priceTxt.setText(StudentDB_Helper.getFormattedPrice(order.getTotal()));
        holder.orderID.setText(order.getOrderID());

        Glide.with(holder.offerImg).load(order.getCartList().get(0).getOffer().getOfferUrl()).into(holder.offerImg);

        if(order.getPaymentType().equals("Payhere")&&!order.isPaymentDone()){
            holder.scanBtn.setVisibility(View.VISIBLE);
        }else{
            holder.scanBtn.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {

        TextView orderID,priceTxt;
        ImageView offerImg;
        ImageButton scanBtn;

        public Viewholder(@NonNull View itemView,ongoingListener listener) {
            super(itemView);
            orderID = itemView.findViewById(R.id.ongoingOrder_Id);
            priceTxt = itemView.findViewById(R.id.ongoingOrder_IMG);
            offerImg = itemView.findViewById(R.id.orderImage);
            scanBtn = itemView.findViewById(R.id.ongoingOrder_ScanBtn);

            scanBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onScanNeed(position);
                        }
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }

    public void setListener(ongoingListener listener) {
        this.listener = listener;
    }

    public void setList(ArrayList<Order> list) {
        this.list = list;
    }

    public interface ongoingListener{
        void onScanNeed(int position);
        void onItemClick(int position);
    }

}
