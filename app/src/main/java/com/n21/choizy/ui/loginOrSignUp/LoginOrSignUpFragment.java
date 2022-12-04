package com.n21.choizy.ui.loginOrSignUp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.n21.choizy.databinding.FragmentLoginOrSignUpBinding;

import org.jetbrains.annotations.NotNull;


public class LoginOrSignUpFragment extends Fragment {

    FragmentLoginOrSignUpBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentLoginOrSignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections navDirections = LoginOrSignUpFragmentDirections.actionLoginOrSignUpFragmentToLoginFragment();
                Navigation.findNavController(binding.getRoot()).navigate(navDirections);
            }
        });


        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections navDirections = LoginOrSignUpFragmentDirections.actionLoginOrSignUpFragmentToSignUpFragment();
                Navigation.findNavController(binding.getRoot()).navigate(navDirections);
            }
        });
    }
}