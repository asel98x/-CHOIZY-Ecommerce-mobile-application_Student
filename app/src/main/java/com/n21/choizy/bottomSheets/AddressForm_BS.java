package com.n21.choizy.bottomSheets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.n21.choizy.R;
import com.n21.choizy.model.AddressModel;

public class AddressForm_BS extends BottomSheetDialogFragment {

    TextInputLayout houseNoTIL,streetAddressTIL,cityTIL;
    MaterialButton setAdresBtn;
    AddressFormListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.address_form,container,false);
        houseNoTIL = view.findViewById(R.id.addressForm_NoTIL);
        streetAddressTIL = view.findViewById(R.id.addressForm_streetTIL);
        cityTIL = view.findViewById(R.id.addressForm_CityTIL);
        setAdresBtn = view.findViewById(R.id.addressForm_SetAddressBtn);

        setAdresBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){


                if(houseNoTIL.getEditText().getText().toString().trim().isEmpty()){
                    houseNoTIL.setError("House No can't be empty");
                    houseNoTIL.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            houseNoTIL.setError(null);
                            houseNoTIL.requestFocus();
                        }
                    },5000);

                }else if(streetAddressTIL.getEditText().getText().toString().trim().isEmpty()){
                    streetAddressTIL.setError("Street Address can't be empty");
                    streetAddressTIL.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            streetAddressTIL.setError(null);
                            streetAddressTIL.requestFocus();
                        }
                    },5000);

                }else if(cityTIL.getEditText().getText().toString().trim().isEmpty()){
                    cityTIL.setError("City can't be empty");
                    cityTIL.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            cityTIL.setError(null);
                            cityTIL.requestFocus();
                        }
                    },5000);
                }else{
                    AddressModel address = new AddressModel(houseNoTIL.getEditText().getText().toString().trim(),streetAddressTIL.getEditText().getText().toString().trim(),cityTIL.getEditText().getText().toString().trim());
                    listener.onAddressUpdate(address);
                    dismiss();
                }

                }
            }
        });



        return view;
    }

    public interface AddressFormListener{
        void onAddressUpdate(AddressModel address);
    }

    public void setListener(AddressFormListener listener) {
        this.listener = listener;
    }
}
