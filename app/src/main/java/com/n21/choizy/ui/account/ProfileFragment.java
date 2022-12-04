package com.n21.choizy.ui.account;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.n21.choizy.PasswordChangeDialog;
import com.n21.choizy.R;
import com.n21.choizy.StudentDB_Helper;
import com.n21.choizy.model.Student;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements PasswordChangeDialog.onPasswordChange {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextInputLayout emailTIL,mobileTIL,NicTIL,genderTIl,birthdateTIL;
    TextView studID,studName,changePassTxt;
    ImageView profileImg,editImgBtn;
    StudentDB_Helper db_helper;
    ValueEventListener studentProfileListener;
    Student student;
    RequestOptions options;
    private ActivityResultLauncher<Intent> result;
    private Uri ImgUri;


    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view =inflater.inflate(R.layout.fragment_profile, container, false);
        emailTIL=view.findViewById(R.id.profileEmailTIL);
        mobileTIL = view.findViewById(R.id.profileMobileTIL);
        NicTIL = view.findViewById(R.id.profileNICTIL);
        genderTIl = view.findViewById(R.id.profileGenderTIL);
        editImgBtn = view.findViewById(R.id.profileImgBtn);
        birthdateTIL = view.findViewById(R.id.profileBirthDateTIL);
        studID = view.findViewById(R.id.profileStudIDTxt);
        studName = view.findViewById(R.id.profileStudNameTxt);
        profileImg = view.findViewById(R.id.profileStudentImg);
        changePassTxt = view.findViewById(R.id.profileChangePassBtn);
        db_helper = new StudentDB_Helper(getContext());
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round).dontAnimate();

        changePassTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasswordChangeDialog dialog = new PasswordChangeDialog();
                dialog.setListener(ProfileFragment.this);
                dialog.show(getChildFragmentManager(),"Password Change Dialog");
            }
        });

        ColorStateList colorStateList = new ColorStateList(
                new int[][] { new int[] { android.R.attr.state_enabled } },
                new int[] {Color.WHITE});
        emailTIL.setEndIconDrawable(R.drawable.ic_edit);
        emailTIL.setEndIconTintList(colorStateList);
        mobileTIL.setEndIconDrawable(R.drawable.ic_edit);
        mobileTIL.setEndIconTintList(colorStateList);
        mobileTIL.getEditText().setInputType(InputType.TYPE_NULL);
        emailTIL.getEditText().setInputType(InputType.TYPE_NULL);

        emailTIL.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(student == null){
                    return;
                }
                db_helper.textHolderchange(emailTIL);
                if(!emailTIL.isActivated()){
                    if(!emailTIL.getEditText().getText().toString().trim().isEmpty()){
                        student.setStudent_email(emailTIL.getEditText().getText().toString().trim());
                        db_helper.updateStudent(student).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isComplete()){
                                    Snackbar.make(view,"Email Changed",Snackbar.LENGTH_SHORT).show();
                                }

                            }
                        });

                    }else{
                        emailTIL.getEditText().setText(student.getStudent_email());
                    }
                }


            }
        });
        mobileTIL.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(student == null){
                    return;
                }
                db_helper.textHolderchange(mobileTIL);
                if(!mobileTIL.isActivated()){
                    if(!mobileTIL.getEditText().getText().toString().trim().isEmpty()){
                        if(mobileTIL.getEditText().getText().toString().trim().length()<10){
                            mobileTIL.setError("Mobile Number should at contain 10 digits");
                            mobileTIL.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mobileTIL.setError(null);
                                    mobileTIL.requestFocus();
                                    mobileTIL.getEditText().setText(student.getStudent_mobile());
                                }
                            },5000);

                            return;
                        }
                        student.setStudent_mobile(mobileTIL.getEditText().getText().toString().trim());
                        db_helper.updateStudent(student).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isComplete()){
                                    Snackbar.make(view,"Mobile Updated",Snackbar.LENGTH_SHORT).show();
                                }

                            }
                        });

                    }else{
                        mobileTIL.getEditText().setText(student.getStudent_mobile());
                    }
                }


            }
        });

        result = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent intent = result.getData();
                            if(intent != null){
                                try {
                                    Bitmap ImgBit = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), intent.getData());
                                    Glide.with(profileImg).load(ImgBit).circleCrop().into(profileImg);
                                    ImgUri = intent.getData();
                                    if(ImgUri !=null){
                                        uploadImg();
                                    }

                                } catch (IOException e) {
                                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                }

                            }
                        }
                    }
                });

        editImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent img = new Intent();
                img.setType("image/*");
                img.setAction(Intent.ACTION_GET_CONTENT);
                result.launch(img);
            }
        });

        loadProfile();

        return view;
    }

    private void uploadImg() {
        db_helper.uploadImage(ImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        String url = task.getResult().toString();
                        db_helper.deleteImg(student.getImageURL());
                        student.setImageURL(url);
                        db_helper.updateStudent(student).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(getView() != null){
                                    Snackbar.make(getView(),"Profile Image updated",Snackbar.LENGTH_SHORT).show();
                                }

                            }
                        });

                    }
                });

            }
        });

    }


    private void loadProfile() {
        studentProfileListener = db_helper.studentGetDetailsByKey(db_helper.getStudentKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                student = snapshot.getValue(Student.class);
                student.setKeey(snapshot.getKey());
                studName.setText(student.getStudent_name());
                studID.setText(student.getStudent_id());
                emailTIL.getEditText().setText(student.getStudent_email());
                mobileTIL.getEditText().setText(student.getStudent_mobile());
                NicTIL.getEditText().setText(student.getStudent_nic());
                genderTIl.getEditText().setText(student.getStudent_gender());
                birthdateTIL.getEditText().setText(student.getStudent_bday());
                System.out.println(student.getImageURL());
                try {
                    Glide.with(requireContext()).load(student.getImageURL()).circleCrop().into(profileImg);
                }catch (Exception e){
                    e.printStackTrace();
                }

//                Picasso.get().load(student.getImageURL()).into(profileImg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    @Override
    public void onStop() {
        db_helper.studentGetDetailsByKey(db_helper.getStudentKey()).removeEventListener(studentProfileListener);
        super.onStop();
    }

    @Override
    public void NewPassword(String pass) {
        student.setStudent_password(pass);
        db_helper.updateStudent(student).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    Snackbar.make(getView(),"Password Changed",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
}