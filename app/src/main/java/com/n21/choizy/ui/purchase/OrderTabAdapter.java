package com.n21.choizy.ui.purchase;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class OrderTabAdapter extends FragmentStateAdapter {

    public OrderTabAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new OngoingFragment();
            case 1:
                return new HistoryFragment();
        }
        return null;

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
