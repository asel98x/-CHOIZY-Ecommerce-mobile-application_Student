package com.n21.choizy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.n21.choizy.R;
import com.n21.choizy.model.Branch;

import org.jetbrains.annotations.NotNull;

public class BranchLArrayAdapter extends ArrayAdapter<Branch> {


    public BranchLArrayAdapter(@NonNull Context context, int resource) {
        super(context, 0);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return CustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable @org.jetbrains.annotations.Nullable View convertView, @NonNull @NotNull ViewGroup parent) {
        return CustomView(position, convertView, parent);
    }

    private View CustomView(int position, View view, ViewGroup viewGroup){
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_dropdown_item_1line, viewGroup, false);
        }

        TextView textView = view.findViewById(android.R.id.text1);

        Branch branch = getItem(position);

        if(branch != null){
            textView.setText(branch.getName());
        }

        return view;
    }
}
