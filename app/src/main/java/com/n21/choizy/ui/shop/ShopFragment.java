package com.n21.choizy.ui.shop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.n21.choizy.R;
import com.n21.choizy.StudentDB_Helper;
import com.n21.choizy.adapter.BranchLArrayAdapter;
import com.n21.choizy.adapter.OfferCardAdapter;
import com.n21.choizy.bottomSheets.Feedback_BS;
import com.n21.choizy.databinding.FragmentShopBinding;
import com.n21.choizy.model.Branch;
import com.n21.choizy.model.Company;
import com.n21.choizy.model.Feedback;
import com.n21.choizy.model.Offer;
import com.n21.choizy.model.Student;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ShopFragment extends Fragment implements AdapterView.OnItemSelectedListener, OfferCardAdapter.OfferCardListener,Feedback_BS.feedbackListener {

    FragmentShopBinding binding;
    TextView companyName, companyDescription;
    ImageView compImg,feedbackBtn;
    Spinner branchDropDown;
    RecyclerView offerLV;
    StudentDB_Helper db_helper;
    Company company;
    BranchLArrayAdapter branchLAdapter;
    OfferCardAdapter offerAdapter;
    ValueEventListener branchVEL,offerVEL;
    ArrayList<Offer> offerList;
    String companyID,branchName,branchID;
    Feedback_BS feedback_bs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentShopBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        companyName = binding.ShopCompanyName;
        companyDescription = binding.ShopDescriptionTxt;
        compImg = binding.ShopCompanyImg;
        branchDropDown = binding.ShopCompanyBranchSpinner;
        feedbackBtn = binding.ShopFeedBack;
        offerLV = binding.offerLV;
        db_helper = new StudentDB_Helper(getContext());
        feedback_bs = new Feedback_BS();
        offerAdapter = new OfferCardAdapter();
        offerList = new ArrayList<>();
        branchLAdapter = new BranchLArrayAdapter(getContext(),0);
        companyID = ShopFragmentArgs.fromBundle(getArguments()).getCompanyID();

        branchDropDown.setAdapter(branchLAdapter);
        branchDropDown.setOnItemSelectedListener(this);

        offerAdapter.setListener(this);
        offerLV.setAdapter(offerAdapter);
        offerLV.setLayoutManager(new LinearLayoutManager(getContext()));

        loadBranch();

        db_helper.getCompDetails(companyID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                company = snapshot.getValue(Company.class);
                company.setKey(snapshot.getKey());
                companyName.setText(company.getCompany_name());
                companyDescription.setText(company.getCompanyAbout());
                Glide.with(view).load(company.getImageURL()).into(compImg);
                offerAdapter.setCompanyImgLink(company.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(branchName ==null){
                    return;
                }
                feedback_bs.setBranchName(branchName);
                feedback_bs.setListener(ShopFragment.this);
                feedback_bs.show(getChildFragmentManager(),"FeedBack BS");

            }
        });




//        binding.offer1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    private void loadBranch(){

        branchVEL = db_helper.getBranchList(companyID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    feedbackBtn.setVisibility(View.VISIBLE);
                }else {
                    feedbackBtn.setVisibility(View.INVISIBLE);
                }

                branchLAdapter.clear();
                for (DataSnapshot one: snapshot.getChildren()) {
                    Branch branch = one.getValue(Branch.class);
                    branch.setID(one.getKey());
                    branchLAdapter.add(branch);

                }

                branchLAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadOffer(String branchID){

        if(offerVEL != null){
            db_helper.removeEventListener(offerVEL);
        }

        offerVEL = db_helper.getOffer(branchID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                offerList.clear();
                for (DataSnapshot one: snapshot.getChildren()) {
                    Offer offer = one.getValue(Offer.class);
                    offer.setOfferId(one.getKey());
                    offerList.add(offer);
                }
                offerAdapter.setList(offerList);
                offerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        loadOffer(branchLAdapter.getItem(i).getID());
        branchName = branchLAdapter.getItem(i).getName();
        branchID = branchLAdapter.getItem(i).getID();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClick(int position) {
        NavDirections navDirections = ShopFragmentDirections.actionShopFragmentToOfferDetailsFragment(offerList.get(position).getOfferId());
        Navigation.findNavController(binding.getRoot()).navigate(navDirections);
    }

    @Override
    public void onFeedBackAdded(String feedback) {
        db_helper.studentGetDetailsByKey(db_helper.getStudentKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                db_helper.studentGetDetailsByKey(db_helper.getStudentKey()).removeEventListener(this);
                Student student = snapshot.getValue(Student.class);

                Feedback feedback1 = new Feedback(companyID,student.getStudent_name(),branchID,feedback);
                feedback1.setStudID(db_helper.getStudentKey());
                db_helper.submitFeedBack(feedback1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isComplete()){
                            Snackbar.make(getView(), getString(R.string.thank_feedback),Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}