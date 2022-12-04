package com.n21.choizy.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.n21.choizy.StudentDB_Helper;
import com.n21.choizy.adapter.HomeCategoryAdapter;
import com.n21.choizy.adapter.ImageSliderAdapter;
import com.n21.choizy.adapter.Recommended_cardAdapter;
import com.n21.choizy.databinding.FragmentHomeBinding;
import com.n21.choizy.model.Category;
import com.n21.choizy.model.RecommendedAD;
import com.n21.choizy.model.UpcomingAD;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeCategoryAdapter.onCategory{

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private StudentDB_Helper db_helper;
    private ImageSliderAdapter ImageSadapter;
    private HomeCategoryAdapter categoryAdapter;
    private RecyclerView categoryLV,recommendedLV;
    private ArrayList<Category> categoriesList;
    private ArrayList<RecommendedAD> recommendedADList;
    private ValueEventListener ImageVEL,categoryVEL,recommendedVEL;
    private Category nearMe;
    private Recommended_cardAdapter recommendedAdapter;
    private SearchView categorySearch;

    private Handler sliderHandler = new Handler();
    private static final int SLIDER_TIME_OUT = 3000;

    ViewPager2 viewPager2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db_helper = new StudentDB_Helper(getContext());
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                db_helper.setNotificationKey(task.getResult());
            }
        });

        viewPager2 = binding.viewPagerImageSlider;
        categoryLV = binding.homeCategoryLV;
        recommendedLV = binding.recommendedLV;
        categorySearch = binding.homeCategorySearch;

        categoriesList = new ArrayList<>();
        recommendedADList = new ArrayList<>();
        categoryAdapter = new HomeCategoryAdapter();
        recommendedAdapter = new Recommended_cardAdapter();

        recommendedLV.setAdapter(recommendedAdapter);
        recommendedLV.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        recommendedLV.addItemDecoration(new Recommended_cardAdapter.RecommendedDecoration());

        nearMe = new Category();
        categoryAdapter.setListener(this);
        categoryLV.setAdapter(categoryAdapter);
        categoryLV.addItemDecoration(new HomeCategoryAdapter.HCategoryDecoration());
        categoryLV.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false));



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


        nearMe.setCategory_name("Near Me");
        nearMe.setImageURL("https://firebasestorage.googleapis.com/v0/b/choizy-606f0.appspot.com/o/nearme.png?alt=media&token=9d59a2b0-c47e-4a2f-baa0-4d186d66c1ae");
//        binding.shopyByCategory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//        Bundle bundle = new Bundle();
//
////            }
//        });

    }

    private void loadRecommendedAD(){
        recommendedVEL = db_helper.getRecommended().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recommendedADList.clear();
                for (DataSnapshot one: snapshot.getChildren()) {
                    RecommendedAD ad = one.getValue(RecommendedAD.class);
                    ad.setAd_ID(one.getKey());
                    recommendedADList.add(ad);
                }
                recommendedAdapter.setList(recommendedADList);
                recommendedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadCategory(String searchTxt){

        if(categoryVEL != null){
            db_helper.getCategories().removeEventListener(categoryVEL);
        }


        categoryVEL = db_helper.getCategories().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoriesList.clear();
                categoriesList.add(nearMe);
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

    private void loadImageSlider() {
        List<UpcomingAD> adList = new ArrayList<>();
        ImageSadapter = new ImageSliderAdapter(null, viewPager2, requireActivity());

        ImageVEL = db_helper.getUpcomingAds().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adList.clear();
                for (DataSnapshot one:snapshot.getChildren()) {
                    UpcomingAD ad = one.getValue(UpcomingAD.class);
                    adList.add(ad);
                }
                ImageSadapter.setImageSliderItems(adList);
                ImageSadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        viewPager2.setAdapter(ImageSadapter);

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(25));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, SLIDER_TIME_OUT);
            }
        });
    }


    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, SLIDER_TIME_OUT);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadImageSlider();
        loadCategory("");
        loadRecommendedAD();
    }

    @Override
    public void onStop() {
        super.onStop();
        db_helper.getUpcomingAds().removeEventListener(ImageVEL);
        db_helper.getCategories().removeEventListener(categoryVEL);
        db_helper.getRecommended().removeEventListener(recommendedVEL);
    }

    @Override
    public void onCategoryClick(int position) {
        if(position == 0){
            NavDirections navDirections = HomeFragmentDirections.actionNavigationHomeToChoizyMap();
            Navigation.findNavController(binding.getRoot()).navigate(navDirections);
        }else {


        Category category =  categoriesList.get(position);
        NavDirections navDirections = HomeFragmentDirections.actionNavigationHomeToShopsByCategoryFragment(category.getCategory_name());
        Navigation.findNavController(binding.getRoot()).navigate(navDirections);
        }
    }
}