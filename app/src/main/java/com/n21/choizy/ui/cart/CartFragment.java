package com.n21.choizy.ui.cart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.n21.choizy.DBClass;
import com.n21.choizy.StudentDB_Helper;
import com.n21.choizy.adapter.CartCardAdapter;
import com.n21.choizy.databinding.FragmentCartBinding;
import com.n21.choizy.model.Branch;
import com.n21.choizy.model.Cart;
import com.n21.choizy.model.Offer;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.ArrayList;


public class CartFragment extends Fragment {

    FragmentCartBinding binding;
    RecyclerView cartLV;
    TextView priceTxt;
    ImageView bg;
    CartCardAdapter cartAdapter;
    ArrayList<Cart> cartList;
    StudentDB_Helper db_helper;
    String branchID = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCartBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cartLV = binding.cartCartListView;
        priceTxt = binding.cartTotalAmount;
        cartAdapter = new CartCardAdapter();
        cartList = new ArrayList<>();
        db_helper = new StudentDB_Helper(getContext());
        bg = binding.ShopCompanyImg;


        cartLV.setLayoutManager(new LinearLayoutManager(getContext()));
        cartLV.setAdapter(cartAdapter);

        loadCartId();
        binding.btnOrderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(branchID!= null){
                    db_helper.getBranchDetails(branchID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            db_helper.getBranchDetails(branchID).removeEventListener(this);
                            if(snapshot.exists()){
                                Branch branch = snapshot.getValue(Branch.class);

                                NavDirections navDirections;
                                if (branch.isHaveDelivering()){
                                    navDirections = CartFragmentDirections.actionNavigationCartToPaymentOptionFragment(branch.isHaveAdvance());
                                }else{
                                    navDirections = CartFragmentDirections.actionNavigationCartToPaymentFragment(branch.isHaveAdvance());
                                }
                                Navigation.findNavController(binding.getRoot()).navigate(navDirections);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
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
                binding.btnOrderNow.setVisibility(View.VISIBLE);

                branchID = snapshot.getChildren().iterator().next().getKey();
                for (DataSnapshot one: snapshot.getChildren().iterator().next().getChildren()) {

                    //get offer from there ids

                    db_helper.getOfferByID(one.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot2) {
                            db_helper.removeEventListener(this);

                            Offer offer = snapshot2.getValue(Offer.class);
                            offer.setOfferId(snapshot2.getKey());

                            cartList.add(new Cart(offer,one.child("Qut").getValue(Integer.class)));

                            cartAdapter.setList(cartList);
                            cartAdapter.notifyDataSetChanged();
                            updatePrice();
                            updateImg();

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                }else{
                    binding.btnOrderNow.setVisibility(View.INVISIBLE);

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

    private void updatePrice(){
        double total = 0 ;
        for (Cart offer:cartList) {
            total += offer.getOffer().getPrice() * offer.getQut();
        }

        priceTxt.setText(db_helper.getFormattedPrice(total));

    }

}