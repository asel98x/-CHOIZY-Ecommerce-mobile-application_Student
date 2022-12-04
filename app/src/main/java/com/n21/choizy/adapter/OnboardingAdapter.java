package com.n21.choizy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.n21.choizy.R;
import com.n21.choizy.model.OnboardingItem;

import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>{

    private List<OnboardingItem> onboardingItemList;

    public OnboardingAdapter(List<OnboardingItem> onboardingItemList) {
        this.onboardingItemList = onboardingItemList;
    }

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnboardingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.onboarding_item_container, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
            holder.setOnboardingData(onboardingItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return onboardingItemList.size();
    }

    class OnboardingViewHolder extends RecyclerView.ViewHolder{

        private TextView textTitle, textSubTitle, textDescription;
        private ImageView imageOnboarding;

        OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textView);
            textSubTitle = itemView.findViewById(R.id.textView2);
            textDescription = itemView.findViewById(R.id.textView3);
            imageOnboarding = itemView.findViewById(R.id.imageView);
        }

        void setOnboardingData(OnboardingItem onboardingItem){
            textTitle.setText(onboardingItem.getTitle());
            textSubTitle.setText(onboardingItem.getSubTitle());
            textDescription.setText(onboardingItem.getDescription());
            imageOnboarding.setImageResource(onboardingItem.getImg());
        }

    }

}
