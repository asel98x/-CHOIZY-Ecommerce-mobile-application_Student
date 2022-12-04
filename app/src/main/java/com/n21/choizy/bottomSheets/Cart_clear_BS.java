package com.n21.choizy.bottomSheets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.n21.choizy.R;

public class Cart_clear_BS extends BottomSheetDialogFragment {

    Button clear,cancel;
    CartClearListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_clear_dialog,container,false);

        clear = view.findViewById(R.id.cartClearBtn);
        cancel = view.findViewById(R.id.cartClearCancelBtn);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(listener != null){
                            listener.onClearClick();
                            dismiss();
                        }
                    }
                });

            }


        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();
            }
        });


        return view;
    }

    public interface CartClearListener{
        void onClearClick();
    }

    public void setListener(CartClearListener listener) {
        this.listener = listener;
    }
}
