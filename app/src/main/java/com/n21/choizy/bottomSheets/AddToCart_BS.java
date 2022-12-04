package com.n21.choizy.bottomSheets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.n21.choizy.R;
import com.n21.choizy.StudentDB_Helper;
import com.n21.choizy.model.Offer;

public class AddToCart_BS extends BottomSheetDialogFragment {

    TextView totalPrice,qutTxt;
    ImageButton addQutBtn,removeQutBtn;
    AddCartBSListener listener;
    MaterialButton addBtn;
    int qut = 1;
    Offer offer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addtocart_bs,container,false);

        if(offer == null){
            dismiss();
            return null;
        }

        totalPrice = view.findViewById(R.id.cartBS_Total);
        addQutBtn = view.findViewById(R.id.cartBS_AddQutBtn);
        removeQutBtn = view.findViewById(R.id.cartBS_removeQutBtn);
        qutTxt = view.findViewById(R.id.cartBS_OfferQut);
        addBtn = view.findViewById(R.id.cartBS_AddBtn);

        if(qut == 1){
            removeQutBtn.setEnabled(false);
        }

        qutTxt.setText(""+qut);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!= null) {
                    listener.onAddCartListener(qut);
                    dismiss();

                }
            }
        });


//        Glide
        calculatePrice();

        addQutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addQut();
            }
        });

        removeQutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeQut();
            }
        });



        return view;
    }

    private void addQut(){
        qut += 1;

        removeQutBtn.setEnabled(true);
        qutTxt.setText(""+qut);
        calculatePrice();
    }

    private void removeQut(){
        qut -=1;
        qutTxt.setText(""+qut);
        if(qut == 1){
            removeQutBtn.setEnabled(false);
        }

        calculatePrice();
    }

    private void calculatePrice(){
        double total = qut *offer.getPrice();

        totalPrice.setText(StudentDB_Helper.getFormattedPrice(total));
    }

    public AddToCart_BS() {

    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public void setQut(int qut) {
        this.qut = qut;
    }

    public interface AddCartBSListener{
        void onAddCartListener(int qut);
    }

    public void setListener(AddCartBSListener listener) {
        this.listener = listener;
    }
}
