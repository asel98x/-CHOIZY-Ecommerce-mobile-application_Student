package com.n21.choizy.ui.delivery;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.n21.choizy.bottomSheets.AddressForm_BS;
import com.n21.choizy.databinding.FragmentDeliveryBinding;
import com.n21.choizy.model.AddressModel;
import com.n21.choizy.model.Cart;
import com.n21.choizy.model.Offer;
import com.n21.choizy.model.Order;
import com.n21.choizy.model.Student;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class DeliveryFragment extends Fragment implements AddressForm_BS.AddressFormListener {

    FragmentDeliveryBinding binding;
    ArrayList<Cart> cartList;
    StudentDB_Helper db_helper;
    Spinner payMethod;
    TextView priceTxt,selectAddressTxt;
    AddressModel address;
    ImageView bg;
    Date date = Calendar.getInstance().getTime();
    SimpleDateFormat dateFormat;
    SimpleDateFormat timeFormat ;
    boolean isNav =false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDeliveryBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cartList = new ArrayList<>();
        db_helper = new StudentDB_Helper(getContext());
        bg = binding.ShopCompanyImg;
        timeFormat = new SimpleDateFormat("HH:mm:ss");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        payMethod  = binding.cartDeliveryPayMethod;
        priceTxt = binding.cartDeliveryTotalAmount;
        selectAddressTxt = binding.cartDeliverySelectAddressBtn;
//        NavController navController = NavHostFragment.findNavController(this);

        selectAddressTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressForm_BS addressBS = new AddressForm_BS();
                addressBS.setListener(DeliveryFragment.this);
                addressBS.show(getChildFragmentManager(),"AddressBS");

            }
        });
        loadCartId();


        binding.btnOrderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(address ==null){
                    selectAddressTxt.setError("Please enter address");
                    selectAddressTxt.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            selectAddressTxt.setError(null);
                        }
                    },2000);

                    return;
                }

                if(payMethod.getSelectedItemPosition()==2){
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
                                order.setStudentID(student.getStudent_id());
                                order.setPaymentType("Loan");
                                order.setDelivery(address.getHouseNo()+" "+address.getStreetAddress()+" "+address.getCity());
                                order.setOrderType("Delivery");
                                order.setOrderStatus("Pending");
                                order.setNotificationKey(db_helper.getNotificationKey());
                                order.setPaymentDone(true);
                                order.setDelivery(address.getHouseNo()+" "+address.getStreetAddress()+" "+address.getCity());
                                order.setCartList(cartList);
                                order.setDate(dateFormat.format(date));
                                order.setTime(timeFormat.format(date));

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
//                                            if(!isNav){
//                                                isNav = true;
//                                                if(navController.getCurrentDestination().getId() == R.id.deliveryFragment){
//                                                    navController.navigate(R.id.orderSuccessFragment);
//                                                }
//                                            }
                                            NavDirections navDirections = DeliveryFragmentDirections.actionDeliveryFragmentToOrderSuccessFragment(id1);
                                            Navigation.findNavController(binding.getRoot()).navigate(navDirections);


                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else{
                    db_helper.studentGetDetailsByKey(db_helper.getStudentKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            db_helper.studentGetDetailsByKey(db_helper.getStudentKey()).removeEventListener(this);
                            Student student = snapshot.getValue(Student.class);
                            Order order = new Order();
                            order.setStudentKey(db_helper.getStudentKey()); //
                            order.setStudentName(student.getStudent_name());//
                            order.setStudentMobile(student.getStudent_mobile());
                            order.setNotificationKey(db_helper.getNotificationKey());
                            order.setOrderType("Delivery");//
                            order.setOrderStatus("Pending");
                            order.setDelivery(address.getHouseNo()+" "+address.getStreetAddress()+" "+address.getCity());
                            order.setDate(dateFormat.format(date));
                            order.setTime(timeFormat.format(date));
                            if(payMethod.getSelectedItemPosition() == 3){
                                order.setPaymentType("Payhere");
                            }else {
                                order.setPaymentType(payMethod.getSelectedItem().toString());
                            }
                            order.setPaymentDone(false);//

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
                                        System.out.println("Going success fragment");

//                                        if(!isNav){
//                                            isNav = true;
//                                            if(navController.getCurrentDestination().getId() == R.id.deliveryFragment){
//                                                navController.navigate(R.id.orderSuccessFragment);
//                                            }
//                                         //
//                                        }
                                        NavDirections navDirections = DeliveryFragmentDirections.actionDeliveryFragmentToOrderSuccessFragment(id1);
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
        });


    }

    private void loadCartId() {
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
    public void onAddressUpdate(AddressModel address) {
        this.address = address;
        selectAddressTxt.setText(address.getHouseNo()+"\n"+address.getStreetAddress()+"\n"+address.getCity());
        selectAddressTxt.setGravity(Gravity.NO_GRAVITY);
    }
}