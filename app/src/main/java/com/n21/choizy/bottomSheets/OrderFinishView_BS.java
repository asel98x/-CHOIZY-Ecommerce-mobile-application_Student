package com.n21.choizy.bottomSheets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.n21.choizy.R;
import com.n21.choizy.StudentDB_Helper;
import com.n21.choizy.adapter.OrderListItemAdapter;
import com.n21.choizy.model.Order;

import org.jetbrains.annotations.NotNull;

public class OrderFinishView_BS extends BottomSheetDialogFragment {

    Order orders;
    TextView orderView_PaymentMethod,orderView_Address,totalPrice,orderID,dateTxt,timeTxt;

    RecyclerView orderView_OrderList;
    OrderListItemAdapter adapter;



    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_view,container,false);

        orderView_PaymentMethod = view.findViewById(R.id.orderView_PaymentMethod);
        orderView_Address = view.findViewById(R.id.orderView_Address);
        orderID = view.findViewById(R.id.orderView_orderID);
        totalPrice = view.findViewById(R.id.orderView_Total);
        dateTxt = view.findViewById(R.id.orderView_Date);
        timeTxt = view.findViewById(R.id.orderView_Time);

        orderView_OrderList = view.findViewById(R.id.orderView_OrderList);
        adapter = new OrderListItemAdapter();

        orderView_OrderList.setLayoutManager(new LinearLayoutManager(getContext()));
        orderView_OrderList.setAdapter(adapter);
        adapter.setList(orders.getCartList());
        adapter.notifyDataSetChanged();
        timeTxt.setText(orders.getTime());
        orderID.setText(orders.getOrderID());
        dateTxt.setText(orders.getDate());

        totalPrice.setText(StudentDB_Helper.getFormattedPrice(orders.getTotal()));
        orderView_PaymentMethod.setText("Payment Method - "+orders.getPaymentType());
        if(orders.getOrderType().equals("Delivery")){
            orderView_Address.setText("Address -"+orders.getDelivery());
        }else{
            orderView_Address.setText("Order Type -"+orders.getOrderType());
        }





        return view;
    }

    public OrderFinishView_BS(Order orders) {
        this.orders = orders;
    }
}
