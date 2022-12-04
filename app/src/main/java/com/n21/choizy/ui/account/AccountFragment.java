package com.n21.choizy.ui.account;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.n21.choizy.AuthenticateActivity;
import com.n21.choizy.DBClass;
import com.n21.choizy.MainActivity;
import com.n21.choizy.R;
import com.n21.choizy.StudentDB_Helper;
import com.n21.choizy.adapter.LanguageDialog;
import com.n21.choizy.model.Student;
import com.n21.choizy.myLocal;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment implements LanguageDialog.languageListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinearLayout purchaseBtn,logoutBtn,langBtn;
    private StudentDB_Helper db_helper;
    private TextView accountName;
    private TextView accountEmail;
    private ImageView profileIng;
    private ConstraintLayout loanAmountHolder;
    private TextView loanAmountTxt;
    private ImageView settingBtn;
    private TextView studIDTxt;


    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        db_helper = new StudentDB_Helper(getContext());
        purchaseBtn = view.findViewById(R.id.history);
        accountName = view.findViewById(R.id.account_name);
        accountEmail = view.findViewById(R.id.account_Email);
        profileIng = view.findViewById(R.id.account_profile);
        loanAmountHolder = view.findViewById(R.id.account_loanHolder);
        loanAmountTxt = view.findViewById(R.id.accountLoanTxt);
        settingBtn = view.findViewById(R.id.accountsettingBtn);
        logoutBtn = view.findViewById(R.id.logoutBtn);
        studIDTxt = view.findViewById(R.id.account_name2);
        langBtn = view.findViewById(R.id.langBtn);


        purchaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections navDirections = AccountFragmentDirections.actionNavigationAccountToPurchaseFragment();
                Navigation.findNavController(view).navigate(navDirections);
            }
        });
        loadProfileData();

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections navDirections = AccountFragmentDirections.actionNavigationAccountToProfileFragment();
                Navigation.findNavController(view).navigate(navDirections);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        langBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LanguageDialog dialog = new LanguageDialog();
                dialog.setLanguageListener(AccountFragment.this);
                dialog.show(getChildFragmentManager(),"langDialog");
            }
        });

        return view;
    }

    private void logout(){
        StudentDB_Helper.logout();
        Intent intent = new Intent(requireActivity(), AuthenticateActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void loadProfileData() {
        db_helper.studentGetDetailsByKey(db_helper.getStudentKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    logout();
                    return;
                }
                db_helper.studentGetDetailsByKey(db_helper.getStudentKey()).removeEventListener(this);
                Student student = snapshot.getValue(Student.class);
                accountName.setText(student.getStudent_name());
                accountEmail.setText(student.getStudent_email());
                studIDTxt.setText(student.getStudent_id());

                if(student.getStudent_loan()<5000){
                    loanAmountHolder.setVisibility(View.VISIBLE);
                    loanAmountTxt.setText(StudentDB_Helper.getFormattedPrice(5000-student.getStudent_loan()));
                }

                try {
                    Glide.with(getContext()).load(student.getImageURL()).circleCrop().into(profileIng);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onLangeSelected(String lang) {
        myLocal.setLocale(getContext(),lang);
        Intent intent = new Intent(getActivity(),AuthenticateActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}