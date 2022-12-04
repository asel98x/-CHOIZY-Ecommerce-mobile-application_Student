package com.n21.choizy.ui.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;


import com.n21.choizy.R;
import com.n21.choizy.adapter.OnboardingAdapter;
import com.n21.choizy.databinding.FragmentOnboardingBinding;
import com.n21.choizy.model.OnboardingItem;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class OnboardingFragment extends Fragment {

    private OnboardingAdapter onboardingAdapter;
    private LinearLayout onboardingLayoutIndicator;
    private MaterialButton onboardingActionButton;

    private FragmentOnboardingBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentOnboardingBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onboardingLayoutIndicator = binding.onboardingLayoutIndicator;
        onboardingActionButton = binding.buttonOnbardingAction;

        setUpOnboardingItems();

        ViewPager2 onboardingViewPager = binding.onBoardingViewPager;
        onboardingViewPager.setAdapter(onboardingAdapter);
        setUpOnboardingIndicators();
        setCurrentOnboardingIndicator(0);

        onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicator(position);
            }
        });


        onboardingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onboardingViewPager.getCurrentItem() + 1 < onboardingAdapter.getItemCount()){
                    onboardingViewPager.setCurrentItem(onboardingViewPager.getCurrentItem() + 1);
                }else{

                    NavDirections navDirections = OnboardingFragmentDirections.actionOnboardingFragmentToLoginOrSignUpFragment();
                    Navigation.findNavController(binding.getRoot()).navigate(navDirections);

                }
            }
        });

    }


    private void setUpOnboardingItems(){
        List<OnboardingItem> onboardingItems = new ArrayList<>();

        OnboardingItem item1 = new OnboardingItem();
        item1.setTitle("Choose");
        item1.setSubTitle("A Tasty Dish");
        item1.setDescription("Order anything you want from your favourite restaurant..");
        item1.setImg(R.drawable.ic_onboarding1);

        OnboardingItem item2 = new OnboardingItem();
        item2.setTitle("Easy");
        item2.setSubTitle("Payments");
        item2.setDescription("Payment made easy through debit card, credit card  & more ways to pay for your food..");
        item2.setImg(R.drawable.ic_onboarding2);


        OnboardingItem item3 = new OnboardingItem();
        item3.setTitle("Enjoy");
        item3.setSubTitle("The Taste!");
        item3.setDescription("Healthy eating means eating a variety of foods that give you the nutrients you need to maintain your health...");
        item3.setImg(R.drawable.ic_onboarding3);

        onboardingItems.add(item1);
        onboardingItems.add(item2);
        onboardingItems.add(item3);

        onboardingAdapter = new OnboardingAdapter(onboardingItems);

    }

    private void setUpOnboardingIndicators(){
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0, 8, 0);
        for (int i = 0; i < indicators.length; i++){
            indicators[i] = new ImageView(requireContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    requireContext(), R.drawable.onboarding_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            onboardingLayoutIndicator.addView(indicators[i]);
        }
    }


    private void setCurrentOnboardingIndicator(int index){
        int childCount = onboardingLayoutIndicator.getChildCount();
        for (int i = 0; i < childCount; i++){
            ImageView imageView = (ImageView) onboardingLayoutIndicator.getChildAt(i);
            if(i == index){
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(requireContext(), R.drawable.onboarding_indicator_active)
                );
            }else{
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(requireContext(), R.drawable.onboarding_indicator_inactive)
                );
            }
        }
        if(index == onboardingAdapter.getItemCount() - 1){
            onboardingActionButton.setText("Start");
        }else{
            onboardingActionButton.setText("Next");
        }
    }
}