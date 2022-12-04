package com.n21.choizy.ui.payment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.n21.choizy.R;
import com.n21.choizy.StudentDB_Helper;
import com.n21.choizy.databinding.FragmentPaymentBinding;
import com.n21.choizy.model.Cart;
import com.n21.choizy.model.Offer;
import com.n21.choizy.model.Order;
import com.n21.choizy.model.Student;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.StatusResponse;

public class PaymentFragment extends Fragment implements LifecycleObserver {

    FragmentPaymentBinding binding;
    TextView priceTxt;
    ArrayList<Cart> cartList;
    ArrayList<Cart> cartListVM;
    StudentDB_Helper db_helper;
    PaymentFragViewModel viewModel;
    Spinner paymentMethod;
    ArrayAdapter pmLWithOutQr,pmLAll;
    InitRequest payReq = new InitRequest();
    Student student = null;
    String OrderId;
    RadioGroup visiInType;
    ImageView bg;
    String orderType = "orderNow";
    Date date = Calendar.getInstance().getTime();
    SimpleDateFormat dateFormat;
    SimpleDateFormat timeFormat ;
    boolean haveAdvance;

    private final static int PAYHERE_REQUEST = 11010;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = FragmentPaymentBinding.inflate(inflater, container, false);
       return binding.getRoot();
    }




    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db_helper = new StudentDB_Helper(getContext());
        cartList = new ArrayList<>();
        timeFormat = new SimpleDateFormat("HH:mm:ss");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        bg = binding.ShopCompanyImg;
        pmLWithOutQr = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.payment_typesWithQR));
        pmLAll = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.payment_types));
        getActivity().getLifecycle().removeObserver(this);
        viewModel = new ViewModelProvider(requireActivity()).get(PaymentFragViewModel.class);
        haveAdvance = PaymentFragmentArgs.fromBundle(getArguments()).getHaveAdvance();

        if(haveAdvance){
            binding.radioButton2.setVisibility(View.VISIBLE);
        }else{
            binding.radioButton2.setVisibility(View.INVISIBLE);
        }


        visiInType = binding.radioGroupVisitType;
        priceTxt = binding.cartTotalAmount;
        paymentMethod = binding.cartPaymentMethod;

        loadCartId();
        binding.btnOrderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cartList != null){

                    if(paymentMethod.getSelectedItemPosition() == 3){
                        viewModel.setCartList(cartList);
                        viewModel.setPrice(updatePrice());
                        NavDirections navDirections = PaymentFragmentDirections.actionPaymentFragmentToQrScanner();
                        Navigation.findNavController(binding.getRoot()).navigate(navDirections);
                    }else if(paymentMethod.getSelectedItemPosition()==2){
                        db_helper.studentGetDetailsByKey(db_helper.getStudentKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                db_helper.studentGetDetailsByKey(db_helper.getStudentKey()).removeEventListener(this);
                                Student student = snapshot.getValue(Student.class);

                                if(student.getStudent_loan()>updatePrice()){

                                    student.setStudent_loan(student.getStudent_loan()-updatePrice());
                                    db_helper.updateStudent(student);

                                    Order order = new Order();
                                    order.setStudentKey(db_helper.getStudentKey());
                                    order.setStudentName(student.getStudent_name());
                                    order.setStudentMobile(student.getStudent_mobile());
                                    order.setPaymentType("Loan");
                                    order.setOrderStatus("Pending");
                                    order.setOrderType(orderType);
                                    order.setPaymentDone(true);
                                    order.setNotificationKey(db_helper.getNotificationKey());
                                    order.setStudentEmail(student.getStudent_email());
                                    order.setDate(dateFormat.format(date));
                                    order.setTime(timeFormat.format(date));
                                    order.setCartList(cartList);
                                    order.setStudentID(student.getStudent_id());

                                    if(cartList.size()>0){
                                        order.setBranchID(cartList.get(0).getOffer().getBranchID());
                                    }

                                    String id1 = db_helper.genOrderID();

                                    db_helper.AddOrderWithID(order,id1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isComplete()){
                                                Snackbar.make(v,"Order Added",Snackbar.LENGTH_SHORT).show();
                                                db_helper.deleteCart();
                                                NavDirections navDirections = PaymentFragmentDirections.actionPaymentFragmentToOrderSuccessFragment(id1);
                                                Navigation.findNavController(binding.getRoot()).navigate(navDirections);

                                            }
                                        }
                                    });
                                }else {
                                    Snackbar.make(v, getString(R.string.insufficient_credit),Snackbar.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }else {
                    db_helper.studentGetDetailsByKey(db_helper.getStudentKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            db_helper.studentGetDetailsByKey(db_helper.getStudentKey()).removeEventListener(this);
                            Student student = snapshot.getValue(Student.class);
                            Order order = new Order();
                            order.setStudentID(student.getStudent_id());
                            order.setStudentKey(db_helper.getStudentKey());
                            order.setStudentName(student.getStudent_name());
                            order.setStudentMobile(student.getStudent_mobile());
                            order.setOrderType(orderType);
                            order.setNotificationKey(db_helper.getNotificationKey());
                            order.setOrderStatus("Pending");
                            order.setStudentEmail(student.getStudent_email());
                            order.setDate(dateFormat.format(date));
                            order.setTime(timeFormat.format(date));
                            if(paymentMethod.getSelectedItemPosition() == 1){
                                order.setPaymentType("Card");
                            }else if(paymentMethod.getSelectedItemPosition() == 0){
                                order.setPaymentType("Cash");
                            }
                            order.setPaymentDone(false);

                            order.setCartList(cartList);

                            if(cartList.size()>0){
                                order.setBranchID(cartList.get(0).getOffer().getBranchID());
                            }

                            String id1 = db_helper.genOrderID();

                            db_helper.AddOrderWithID(order,id1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isComplete()){
                                        Snackbar.make(v,"Order Added",Snackbar.LENGTH_SHORT).show();
                                        db_helper.deleteCart();
                                        NavDirections navDirections = PaymentFragmentDirections.actionPaymentFragmentToOrderSuccessFragment(id1);
                                        Navigation.findNavController(binding.getRoot()).navigate(navDirections);

                                    }
                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    }



                }

//
            }
        });

        visiInType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
                    case R.id.radio_button_1:
                        orderType = "orderNow";
                        paymentMethod.setAdapter(pmLAll);
                        break;
                    case R.id.radio_button_2:
                        orderType = "orderAdvance";
                        paymentMethod.setAdapter(pmLWithOutQr);
                        break;

                }
            }
        });

        MutableLiveData<String[]> liveData = Navigation.findNavController(view).getCurrentBackStackEntry()
                .getSavedStateHandle()
                .getLiveData("PaymentDetails");
        liveData.observe(getViewLifecycleOwner(), new Observer<String[]>() {
            @Override
            public void onChanged(String[] strings) {
                if(strings != null){

                    cartListVM = viewModel.getCartListN();

                    double price = viewModel.getPrice();


                    if(strings[3].equals(cartListVM.get(0).getOffer().getBranchID())){
                        db_helper.studentGetDetailsByKey(db_helper.getStudentKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                OrderId = db_helper.genOrderID();

                                student = snapshot.getValue(Student.class);
                                payReq.setMerchantId(strings[1]);
                                payReq.setMerchantSecret(strings[2]);
                                payReq.setCurrency("LKR");             // Currency code LKR/USD/GBP/EUR/AUD
                                payReq.setAmount(price);             // Final Amount to be charged
                                payReq.setOrderId(OrderId);
                                payReq.setCustom1("");
                                payReq.setCustom2("");
                                payReq.getCustomer().setFirstName(student.getStudent_id()+" ");
                                payReq.getCustomer().setLastName(student.getStudent_name());
                                payReq.getCustomer().setEmail(student.getStudent_email());
                                payReq.getCustomer().setPhone(student.getStudent_mobile());
                                payReq.setItemsDescription(cartListVM.get(0).getOffer().getTitle());
                                payReq.getCustomer().getAddress().setAddress("");
                                payReq.getCustomer().getAddress().setCity("");
                                payReq.getCustomer().getAddress().setCountry("");

                                Intent intent = new Intent(getContext(), PHMainActivity.class);
                                intent.putExtra(PHConstants.INTENT_EXTRA_DATA, payReq);
                                PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL);
                                startActivityForResult(intent, PAYHERE_REQUEST);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        //binding.btnOrderNow.setText("price Test");
                    }else {
                        Snackbar.make(getView(), getString(R.string.invalid_QR),Snackbar.LENGTH_SHORT).show();
                    }

                }
            }
        });

//        viewModel.getPaymentDetails().observe(getViewLifecycleOwner(), new Observer<String[]>() {
//            @Override
//            public void onChanged(String[] strings) {
//
//                if(strings != null){
//
//                    if(cartList.isEmpty()){
//                        System.out.println("Empty Cart");
//                        return;
//                    }
//                    if(strings[3].equals(cartList.get(0).getOffer().getBranchID())){
//                        db_helper.studentGetDetailsByKey(db_helper.getStudentKey()).addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                                OrderId = db_helper.genOrderID().toString();
//
//                                student = snapshot.getValue(Student.class);
//                                payReq.setMerchantId(strings[1]);
//                                payReq.setMerchantSecret(strings[2]);
//                                payReq.setCurrency("LKR");             // Currency code LKR/USD/GBP/EUR/AUD
//                                payReq.setAmount(updatePrice());             // Final Amount to be charged
//                                payReq.setOrderId(OrderId);
//                                payReq.setCustom1("");
//                                payReq.setCustom2("");
//                                payReq.getCustomer().setFirstName(student.getStudent_id());
//                                payReq.getCustomer().setLastName(student.getStudent_name());
//                                payReq.getCustomer().setEmail(student.getStudent_email());
//                                payReq.getCustomer().setPhone(student.getStudent_mobile());
//                                payReq.setItemsDescription(cartList.get(0).getOffer().getTitle());
//                                payReq.getCustomer().getAddress().setAddress("");
//                                payReq.getCustomer().getAddress().setCity("");
//                                payReq.getCustomer().getAddress().setCountry("");
//
//                                Intent intent = new Intent(getContext(), PHMainActivity.class);
//                                intent.putExtra(PHConstants.INTENT_EXTRA_DATA, payReq);
//                                PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL);
//                                startActivityForResult(intent, PAYHERE_REQUEST);
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//
//                        //binding.btnOrderNow.setText("price Test");
//                    }else {
//                        Snackbar.make(getView(), getString(R.string.invalid_QR),Snackbar.LENGTH_SHORT).show();
//                    }
//
//                }
//
//
//
//            }
//        });
    }

    private void CreateOrder(Student student,String id) {
        Order order = new Order();
        order.setStudentKey(db_helper.getStudentKey());
        order.setStudentName(student.getStudent_name());
        order.setStudentMobile(student.getStudent_mobile());
        order.setPaymentDone(true);
        order.setPaymentType("Payhere");
        order.setOrderType("orderNow");
        order.setOrderStatus("Ready");
        order.setCartList(cartListVM);
        order.setNotificationKey(db_helper.getNotificationKey());
        order.setStudentEmail(student.getStudent_email());
        order.setDate(dateFormat.format(date));
        order.setTime(timeFormat.format(date));
        order.setStudentID(student.getStudent_id());

        if(cartListVM.size()>0){
            order.setBranchID(cartListVM.get(0).getOffer().getBranchID());
        }

        db_helper.AddOrderWithID(order,id).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    Snackbar.make(getView(),"Order Added",Snackbar.LENGTH_SHORT).show();
                    db_helper.deleteCart();
                    NavDirections navDirections = PaymentFragmentDirections.actionPaymentFragmentToOrderSuccessFragment(id);
                    Navigation.findNavController(binding.getRoot()).navigate(navDirections);

                }
            }
        });

    }


    private void loadCartId(){
        db_helper.getCartId().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                db_helper.getCartId().removeEventListener(this);
                if(snapshot.exists()){
                    System.out.println("once");

                for (DataSnapshot one: snapshot.getChildren().iterator().next().getChildren()) {

                    //get offer from there ids
                    db_helper.getOfferByID(one.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot2) {
                            db_helper.removeEventListener(this);

                            Offer offer = snapshot2.getValue(Offer.class);
                            offer.setOfferId(snapshot2.getKey());

                            cartList.add(new Cart(offer,one.child("Qut").getValue(Integer.class)));
                            updatePrice();
                            updateImg();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void updateImg(){
        Glide.with(bg).load(cartList.get(0).getOffer().getOfferUrl()).into(bg);
    }

    private double updatePrice(){
        double total = 0 ;
        for (Cart offer:cartList) {
            total += offer.getOffer().getPrice() * offer.getQut();
        }

        priceTxt.setText(db_helper.getFormattedPrice(total));
        return total;

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
                        CreateOrder(student,OrderId);
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        getActivity().getLifecycle().addObserver(this);
    }
}