package com.n21.choizy.ui.paymentOption;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.n21.choizy.R;
import com.n21.choizy.databinding.FragmentPaymentOptionBinding;


public class PaymentOptionFragment extends Fragment {

    FragmentPaymentOptionBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPaymentOptionBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boolean haveAdvance = PaymentOptionFragmentArgs.fromBundle(getArguments()).getHaveAdvance();

        binding.loginDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections navDirections = PaymentOptionFragmentDirections.actionPaymentOptionFragmentToDeliveryFragment();
                Navigation.findNavController(binding.getRoot()).navigate(navDirections);
            }
        });


        binding.btnPickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections navDirections = PaymentOptionFragmentDirections.actionPaymentOptionFragmentToPaymentFragment(haveAdvance);
                Navigation.findNavController(binding.getRoot()).navigate(navDirections);
            }
        });
    }
}