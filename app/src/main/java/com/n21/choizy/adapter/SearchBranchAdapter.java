package com.n21.choizy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.n21.choizy.R;
import com.n21.choizy.model.Branch;

import java.util.ArrayList;
import java.util.List;

public class SearchBranchAdapter extends ArrayAdapter<Branch> {

    List<Branch> list = new ArrayList<>();



    public SearchBranchAdapter(@NonNull Context context, int resource) {
        super(context, 0);
    }

    public List<Branch> getList(){
        int size = getCount();
        list.clear();
        for (int i =0 ;i<size;i++) {
            System.out.println(getItem(i));
            list.add(getItem(i));
        }

        return list;

    }

    private View customView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_suggetion_layout,parent,false);
        }

        TextView name = convertView.findViewById(R.id.SearchLayout_BranchName);
        TextView addresss = convertView.findViewById(R.id.SearchLayout_Address);

        Branch branch = getItem(position);

        if(branch!= null){
            name.setText(branch.getName());
            addresss.setText(branch.getNo_adres()+" "+branch.getStreetAddress()+" "+branch.getCity());
        }


        return convertView;
    }

    public void setList(ArrayList<Branch> list) {
        this.list = list;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();
            List<Branch> branches = new ArrayList<>();

            if(charSequence == null || charSequence.length()==0){
                branches.addAll(list);
            }else{

                String searchTxt = charSequence.toString().toLowerCase().trim();

                for (Branch branch : list){
                    if(branch.getName().toLowerCase().contains(searchTxt)||branch.getCity().toLowerCase().contains(searchTxt)||branch.getNo_adres().toLowerCase().contains(searchTxt)||branch.getStreetAddress().toLowerCase().contains(searchTxt)){
                        branches.add(branch);
                    }
                }


            }

            filterResults.values = branches;
            filterResults.count = branches.size();

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            clear();
            addAll((List) filterResults.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return "";
        }
    };


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return customView(position, convertView, parent);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    @Override
    public void add(@Nullable Branch object) {
        list.add(object);
        super.add(object);
    }
}
