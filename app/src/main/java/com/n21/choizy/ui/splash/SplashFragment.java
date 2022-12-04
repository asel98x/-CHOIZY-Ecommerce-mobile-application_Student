package com.n21.choizy.ui.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.n21.choizy.MainActivity;
import com.n21.choizy.R;
import com.n21.choizy.StudentDB_Helper;

import org.jetbrains.annotations.NotNull;


public class SplashFragment extends Fragment {


    StudentDB_Helper db_helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db_helper = new StudentDB_Helper(getContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(StudentDB_Helper.getFirstLog().isEmpty()){
                    StudentDB_Helper.setFistLog();
                    NavDirections navDirections = SplashFragmentDirections.actionSplashFragmentToOnboardingFragment();
                    Navigation.findNavController(view).navigate(navDirections);
                }else if(StudentDB_Helper.getStudentKey() == null){
                    NavDirections navDirections = SplashFragmentDirections.actionSplashFragmentToLoginOrSignUpFragment();
                    Navigation.findNavController(view).navigate(navDirections);
                }else{
                    Intent intent = new Intent(requireActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }

//                Intent intent = new Intent(requireActivity(), OnboardingActivity.class);
//                startActivity(intent);
//                getActivity().finish();



            }
        }, 6000);

    }
}