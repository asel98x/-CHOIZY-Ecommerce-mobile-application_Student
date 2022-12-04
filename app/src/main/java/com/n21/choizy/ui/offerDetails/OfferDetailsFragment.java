package com.n21.choizy.ui.offerDetails;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.n21.choizy.bottomSheets.AddToCart_BS;
import com.n21.choizy.bottomSheets.Cart_clear_BS;
import com.n21.choizy.databinding.FragmentOfferDetailsBinding;
import com.n21.choizy.model.Branch;
import com.n21.choizy.model.Cart;
import com.n21.choizy.model.Offer;

import org.jetbrains.annotations.NotNull;


public class OfferDetailsFragment extends Fragment implements AddToCart_BS.AddCartBSListener, Cart_clear_BS.CartClearListener {

    FragmentOfferDetailsBinding binding;
    StudentDB_Helper db_helper;
    ValueEventListener offerDetailsVEL,cartVEL;
    Offer offer;
    AddToCart_BS cart_bs ;
    Cart_clear_BS clear_bs;
    TextView offerName,offerDescription;
    String offerID;
    ImageView offerImg;
    Button addCartBtn;
    boolean needNav = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentOfferDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db_helper = new StudentDB_Helper(getContext());

        offerName = binding.OfferDetailsOfferName;
        offerDescription = binding.OfferDetailsDescrition;
        offerImg = binding.OfferDetailsOfferImg;
        addCartBtn = binding.OfferDetailsAddCartBtn;
        cart_bs = new AddToCart_BS();
        clear_bs = new Cart_clear_BS();

        cart_bs.setListener(this);
        clear_bs.setListener(this);

        offerID = OfferDetailsFragmentArgs.fromBundle(getArguments()).getOfferID();
        loadOfferDetails(offerID);

        binding.purchusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                needNav = true;
                AddCart();
            }
        });

        addCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCart();
            }
        });
    }

    private void AddCart() {
        if(offer== null){
            Snackbar.make(getView(),"Couldn't load Offer Details Try Again",Snackbar.LENGTH_SHORT).show();
            return;
        }
        cartVEL = db_helper.getCartId().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                db_helper.removeEventListener(cartVEL);
                db_helper.getCartId().removeEventListener(cartVEL);
                if(snapshot.exists()){

                    if (snapshot.getChildren().iterator().next().getKey().equals(offer.getBranchID())){

                        for (DataSnapshot one: snapshot.getChildren().iterator().next().getChildren()) {

                            if(one.getKey().equals(offerID)){
                                cart_bs.setOffer(offer);
                                cart_bs.setQut(one.child("Qut").getValue(Integer.class));
                                cart_bs.show(getChildFragmentManager(),"Cart BS");
                                return;
                            }
                        }

                        cart_bs.setOffer(offer);
                        cart_bs.setQut(1);
                        cart_bs.show(getChildFragmentManager(),"Cart BS");

                    }else{
                        clear_bs.show(getChildFragmentManager(),"Clear dialog");
                    }

                }else {
                    cart_bs.setOffer(offer);
                    cart_bs.setQut(1);
                    cart_bs.show(getChildFragmentManager(),"Cart BS");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        cart_bs.setOffer(offer);
//        cart_bs.setQut(1);
//        cart_bs.show(getChildFragmentManager(),"Cart BS");





    }

    private void loadOfferDetails(String offerID){
        if(offerDetailsVEL != null){
            db_helper.removeEventListener(offerDetailsVEL);
        }

        offerDetailsVEL = db_helper.getOfferByID(offerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                offer = snapshot.getValue(Offer.class);
                offer.setOfferId(snapshot.getKey());
                offerName.setText(offer.getTitle());
                offerDescription.setText(offer.getDescription());
                Glide.with(getContext()).load(offer.getOfferUrl()).into(offerImg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onAddCartListener(int qut) {
        db_helper.AddCart(offer,qut).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isComplete()){
                            Snackbar.make(getView(), getString(R.string.offer_add_to_card),Snackbar.LENGTH_SHORT).show();
                            if(needNav){
                                needNav =false;
                                NavDirections navDirections = OfferDetailsFragmentDirections.actionOfferDetailsFragmentToNavigationCart();
                                Navigation.findNavController(binding.getRoot()).navigate(navDirections);
                            }
                        }
                    }
                });
    }

    @Override
    public void onClearClick() {
        db_helper.deleteCart().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    Snackbar.make(getView(),"Cart was cleard",Snackbar.LENGTH_SHORT).show();
                    AddCart();
                }
            }
        });
    }
}