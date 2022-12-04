package com.n21.choizy.ui.shopsByCategory;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.n21.choizy.StudentDB_Helper;
import com.n21.choizy.adapter.ShopByCompCardAdapter;
import com.n21.choizy.adapter.ShopBy_CategoryAdapter;
import com.n21.choizy.databinding.FragmentShopsByCategoryBinding;
import com.n21.choizy.model.Category;
import com.n21.choizy.model.Company;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class ShopsByCategoryFragment extends Fragment implements ShopBy_CategoryAdapter.onItemClick,ShopByCompCardAdapter.onItemClick{

    FragmentShopsByCategoryBinding binding;
    RecyclerView categoryLV,companyLV;
    StudentDB_Helper db_helper;
    ShopBy_CategoryAdapter categoryAdapter;
    ShopByCompCardAdapter compCardAdapter;
    ValueEventListener categoryVEL,compVEL;
    ArrayList<Category> categoriesList;
    ArrayList<Company> companiesList;
    SearchView categorySearch;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentShopsByCategoryBinding.inflate(inflater,container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db_helper = new StudentDB_Helper(getContext());
        categoryAdapter = new ShopBy_CategoryAdapter();
        compCardAdapter = new ShopByCompCardAdapter();
        categoriesList = new ArrayList<>();
        companiesList = new ArrayList<>();
        categoryLV = binding.shopByCategoryLV;
        categorySearch = binding.shopBySearchTxt;
        companyLV = binding.shopByCompanyListView;


        categoryAdapter.setListener(this);
        compCardAdapter.setListener(this);


        categoryLV.setAdapter(categoryAdapter);
        categoryLV.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        companyLV.setAdapter(compCardAdapter);
        companyLV.addItemDecoration(new ShopByCompCardAdapter.ShopByCompDecoration());
        companyLV.setLayoutManager(new LinearLayoutManager(getContext()));


        String categoryName = ShopsByCategoryFragmentArgs.fromBundle(getArguments()).getCategoryName();

        categorySearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadCategory(newText);
                return true;
            }
        });

        loadCompany(categoryName);
//        binding.shop1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NavDirections navDirections = ShopsByCategoryFragmentDirections.actionShopsByCategoryFragmentToShopFragment();
//                Navigation.findNavController(binding.getRoot()).navigate(navDirections);
//            }
//        });
    }

    private void loadCategory(String searchTxt){

        if(categoryVEL != null){
            db_helper.getCategories().removeEventListener(categoryVEL);
        }

        categoryVEL = db_helper.getCategories().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoriesList.clear();
                for (DataSnapshot one: snapshot.getChildren()) {
                    Category category = one.getValue(Category.class);
                    category.setKey(one.getKey());

                    if(!searchTxt.isEmpty()){
                        if(category.getCategory_name().toLowerCase().contains(searchTxt.toLowerCase())){
                            categoriesList.add(category);
                        }
                    }else {
                        categoriesList.add(category);
                    }


                }
                categoryAdapter.setList(categoriesList);
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadCompany(String category){
        if(compVEL != null){
            db_helper.removeEventListener(categoryVEL);
        }

        compVEL = db_helper.getCompanyByCategory(category).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                companiesList.clear();

                for (DataSnapshot one: snapshot.getChildren()) {
                    Company company = one.getValue(Company.class);
                    company.setKey(one.getKey());
                    companiesList.add(company);
                }
                compCardAdapter.setList(companiesList);
                compCardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        loadCategory("");
    }

    @Override
    public void onStop() {
        super.onStop();
        db_helper.getCategories().removeEventListener(categoryVEL);
    }

    @Override
    public void onCategorySelected(int position) {
        loadCompany(categoriesList.get(position).getCategory_name());
    }

    @Override
    public void onCompanyClick(int position) {
        NavDirections navDirections = ShopsByCategoryFragmentDirections.actionShopsByCategoryFragmentToShopFragment(companiesList.get(position).getKey());
        Navigation.findNavController(binding.getRoot()).navigate(navDirections);
    }
}