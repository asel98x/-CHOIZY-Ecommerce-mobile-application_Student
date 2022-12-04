package com.n21.choizy.ui.purchase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.n21.choizy.R;
import com.n21.choizy.StudentDB_Helper;
import com.n21.choizy.model.Order;

import java.util.ArrayList;

public class OrderHistoryCardAdapter extends RecyclerView.Adapter<OrderHistoryCardAdapter.View_Holder> {
    ArrayList<Order> list = new ArrayList<>();
    onItemClick listener;

    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_order_card,parent,false);

        return new View_Holder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, int position) {
        Order order = list.get(position);
        holder.orderID.setText(order.getOrderID());
        holder.totalPrice.setText(StudentDB_Helper.getFormattedPrice(order.getTotal()));

        Glide.with(holder.offerImg).load(order.getCartList().get(0).getOffer().getOfferUrl()).into(holder.offerImg);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class View_Holder extends RecyclerView.ViewHolder {
        ImageView offerImg;
        TextView orderID;
        TextView totalPrice;
        public View_Holder(@NonNull View itemView,onItemClick listener) {
            super(itemView);
            offerImg = itemView.findViewById(R.id.historyorderImage);
            orderID = itemView.findViewById(R.id.historyOrder_Id);
            totalPrice = itemView.findViewById(R.id.historyOrder_totalPrice);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemSelected(position);
                        }
                    }
                }
            });


        }
    }

    public void setList(ArrayList<Order> list) {
        this.list = list;
    }

    public interface onItemClick{
        void onItemSelected(int position);
    }

    public void setListener(onItemClick listener) {
        this.listener = listener;
    }
}
