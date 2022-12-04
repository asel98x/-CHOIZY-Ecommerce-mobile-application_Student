package com.n21.choizy.adapter;

import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.n21.choizy.R;
import com.n21.choizy.model.Company;

import java.util.ArrayList;

public class ShopByCompCardAdapter extends RecyclerView.Adapter<ShopByCompCardAdapter.View_holder> {

    ArrayList<Company> list = new ArrayList<>();
    onItemClick listener;

    @NonNull
    @Override
    public View_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shoyby_company_card,parent,false);

        return new View_holder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull View_holder holder, int position) {
        Company company = list.get(position);
        holder.compName.setText(company.getCompany_name());
        holder.compBriefIntro.setText(company.getCompanyFeatures());
        holder.offerRange.setText(company.getCompanyDiscountRange());
        holder.compTerm.setText(company.getCompanyTerms());
        Glide.with(holder.itemView).load(company.getImageURL()).into(holder.compImage);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ShopByCompDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.left = 50;
            outRect.right = 50;
            outRect.bottom = 20;
        }
    }

    public static class View_holder extends RecyclerView.ViewHolder {
        TextView compName,compBriefIntro,offerRange,compTerm;
        ImageView compImage;
        public View_holder(@NonNull View itemView,onItemClick listener) {
            super(itemView);
            compName = itemView.findViewById(R.id.ShopyByComp_Name);
            compBriefIntro = itemView.findViewById(R.id.ShopyByComp_BriefIntroTxt);
            offerRange = itemView.findViewById(R.id.ShopyByComp_offerRangeTxt);
            compTerm = itemView.findViewById(R.id.ShopyByComp_TermsTxt);
            compImage = itemView.findViewById(R.id.ShopyByComp_Img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onCompanyClick(position);
                        }
                    }
                }
            });
        }
    }

    public void setList(ArrayList<Company> list) {
        this.list = list;
    }

    public interface onItemClick{
        void onCompanyClick(int position);
    }

    public void setListener(onItemClick listener) {
        this.listener = listener;
    }
}
