package com.n21.choizy.ui.purchase;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.n21.choizy.R;
import com.n21.choizy.StudentDB_Helper;
import com.n21.choizy.bottomSheets.OrderFinishView_BS;
import com.n21.choizy.model.Order;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment implements OrderHistoryCardAdapter.onItemClick {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<Order> orderList;
    OrderHistoryCardAdapter historyCardAdapter;
    StudentDB_Helper db_helper;
    ValueEventListener ongoingVEL;
    RecyclerView HistoryOrderLV;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        HistoryOrderLV = view.findViewById(R.id.HistoryOrderLV);
        orderList = new ArrayList<>();
        db_helper = new StudentDB_Helper(getContext());
        historyCardAdapter = new OrderHistoryCardAdapter();

        HistoryOrderLV.setLayoutManager(new LinearLayoutManager(getContext()));
        HistoryOrderLV.setAdapter(historyCardAdapter);

        historyCardAdapter.setListener(this);



        loadOrderHistory();
        return view;
    }

    private void loadOrderHistory(){
        ongoingVEL = db_helper.getHistoryOrder().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(snapshot);
                orderList.clear();
                for (DataSnapshot one:snapshot.getChildren()) {
                    Order order = one.getValue(Order.class);
                    order.setOrderID(one.getKey());
                    orderList.add(order);
                }
                Collections.reverse(orderList);
                historyCardAdapter.setList(orderList);
                historyCardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public void onItemSelected(int position) {
        OrderFinishView_BS order_bs = new OrderFinishView_BS(orderList.get(position));
        order_bs.show(getChildFragmentManager(),"orderBS_History");
    }
}