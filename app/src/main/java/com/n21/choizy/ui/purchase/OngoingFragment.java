package com.n21.choizy.ui.purchase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.n21.choizy.R;
import com.n21.choizy.StudentDB_Helper;
import com.n21.choizy.bottomSheets.OrderFinishView_BS;
import com.n21.choizy.model.Order;

import java.util.ArrayList;
import java.util.Collections;

import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.StatusResponse;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OngoingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OngoingFragment extends Fragment implements OngoingCardAdapter.ongoingListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final static int PAYHERE_REQUEST = 11011;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView ongoingLV;
    private StudentDB_Helper db_helper;
    private ArrayList<Order> list;
    private OngoingCardAdapter ongoingAdapter;
    private ValueEventListener ongoingOrderVEL;
    private PurchaseViewModel viewModel;
    InitRequest payReq = new InitRequest();

    public OngoingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OngoingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OngoingFragment newInstance(String param1, String param2) {
        OngoingFragment fragment = new OngoingFragment();
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ongoing, container, false);
        ongoingLV = view.findViewById(R.id.ongoingLV);
        db_helper = new StudentDB_Helper(getContext());
        list = new ArrayList<>();

        ongoingAdapter = new OngoingCardAdapter();
        ongoingAdapter.setListener(this);
        ongoingLV.setLayoutManager(new LinearLayoutManager(getContext()));
        ongoingLV.setAdapter(ongoingAdapter);

        viewModel = new ViewModelProvider(requireActivity()).get(PurchaseViewModel.class);

        if(viewModel.getPaymentDetails()!= null){
            String[] strings = viewModel.getPaymentDetails();
            System.out.println(strings+"test       ....");

            if(strings != null) {

                Order order = viewModel.getOrder();


//                    student = snapshot.getValue(Student.class);
                payReq.setMerchantId(strings[1]);
                payReq.setMerchantSecret(strings[2]);
                payReq.setCurrency("LKR");             // Currency code LKR/USD/GBP/EUR/AUD
                payReq.setAmount(order.getTotal());             // Final Amount to be charged
                payReq.setOrderId(order.getOrderID());
                payReq.setCustom1("");
                payReq.setCustom2("");
                payReq.getCustomer().setFirstName(order.getStudentKey() + " ");
                payReq.getCustomer().setLastName(order.getStudentName());
                payReq.getCustomer().setEmail(order.getStudentEmail()+"");
                payReq.getCustomer().setPhone(order.getStudentMobile());
                payReq.setItemsDescription(order.getCartList().get(0).getOffer().getTitle());
                payReq.getCustomer().getAddress().setAddress("");
                payReq.getCustomer().getAddress().setCity("");
                payReq.getCustomer().getAddress().setCountry("");


                Intent intent = new Intent(getContext(), PHMainActivity.class);
                intent.putExtra(PHConstants.INTENT_EXTRA_DATA, payReq);
                PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL);
                startActivityForResult(intent, PAYHERE_REQUEST);
            }
        }

//        viewModel.getPaymentDetails2().observe(getViewLifecycleOwner(), new Observer<String[]>() {
//            @Override
//            public void onChanged(String[] strings) {
//                System.out.println(strings);
//
//                if(strings != null){
//
//                    Order order = viewModel.getOrder();
//
//
////                    student = snapshot.getValue(Student.class);
//                    payReq.setMerchantId(strings[1]);
//                    payReq.setMerchantSecret(strings[2]);
//                    payReq.setCurrency("LKR");             // Currency code LKR/USD/GBP/EUR/AUD
//                    payReq.setAmount(order.getTotal());             // Final Amount to be charged
//                    payReq.setOrderId(order.getOrderID());
//                    payReq.setCustom1("");
//                    payReq.setCustom2("");
//                    payReq.getCustomer().setFirstName(order.getStudentID()+" ");
//                    payReq.getCustomer().setLastName(order.getStudentName());
//                    payReq.getCustomer().setEmail(order.getStudentEmail());
//                    payReq.getCustomer().setPhone(order.getStudentMobile());
//                    payReq.setItemsDescription(order.getCartList().get(0).getOffer().getTitle());
//                    payReq.getCustomer().getAddress().setAddress("");
//                    payReq.getCustomer().getAddress().setCity("");
//                    payReq.getCustomer().getAddress().setCountry("");
//
//
//
//                    Intent intent = new Intent(getContext(), PHMainActivity.class);
//                    intent.putExtra(PHConstants.INTENT_EXTRA_DATA, payReq);
//                    PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL);
//                    startActivityForResult(intent, PAYHERE_REQUEST);
//
//                }
//            }
//        });

        loadOrder();

        return view;
    }

    private void updateOrder(){
        Order order = viewModel.getOrder();
        order.setPaymentDone(true);
        order.setOrderStatus("Finish");
        db_helper.updateOrder(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                viewModel.setOrder(null);
                viewModel.setPayDetails(null);
            }
        });

    }

    private void loadOrder(){

        if(ongoingOrderVEL !=null){
            db_helper.getOngoingOrder().removeEventListener(ongoingOrderVEL);
        }

        ongoingOrderVEL =  db_helper.getOngoingOrder().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(snapshot);
                list.clear();
                for (DataSnapshot one:snapshot.getChildren()) {
                    Order order = one.getValue(Order.class);
                    order.setOrderID(one.getKey());
                    list.add(order);
                }
                Collections.reverse(list);
                ongoingAdapter.setList(list);
                ongoingAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        db_helper.getOngoingOrder().removeEventListener(ongoingOrderVEL);
    }

    @Override
    public void onScanNeed(int position) {
        Order order = list.get(position);
        viewModel.setOrder(order);
        NavDirections navDirections = PurchaseFragmentDirections.actionPurchaseFragmentToQrOngoingFragment();
        Navigation.findNavController(getView()).navigate(navDirections);
    }

    @Override
    public void onItemClick(int position) {
        OrderFinishView_BS order_bs = new OrderFinishView_BS(list.get(position));
        order_bs.show(getChildFragmentManager(),"orderBS");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYHERE_REQUEST && data != null && data.hasExtra(PHConstants.INTENT_EXTRA_RESULT)) {
            PHResponse<StatusResponse> response = (PHResponse<StatusResponse>) data.getSerializableExtra(PHConstants.INTENT_EXTRA_RESULT);
            if (resultCode == Activity.RESULT_OK) {
                String msg;
                if (response != null){
                    if (response.isSuccess()){
                        Snackbar.make(getView(),"Payment Done",Snackbar.LENGTH_SHORT).show();
                        updateOrder();
                    } else{
                        Snackbar.make(getView(),"Something went wrong",Snackbar.LENGTH_SHORT).show();
                    }
                } else{
                    Snackbar.make(getView(), getString(R.string.pay_cancel),Snackbar.LENGTH_SHORT).show();
                }
                // textView.setText(msg);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                if (response != null)
                    Snackbar.make(getView(),response.toString(),Snackbar.LENGTH_SHORT).show();
                else
                    Snackbar.make(getView(),getString(R.string.pay_cancel),Snackbar.LENGTH_SHORT).show();
            }
        }

    }
}