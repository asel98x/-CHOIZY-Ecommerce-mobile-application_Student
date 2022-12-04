package com.n21.choizy.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.n21.choizy.MainActivity;
import com.n21.choizy.StudentDB_Helper;
import com.n21.choizy.databinding.FragmentLoginBinding;
import com.n21.choizy.model.Student;

import org.jetbrains.annotations.NotNull;


public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;
    StudentDB_Helper db_helper;
    EditText studentID,studentPass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db_helper = new StudentDB_Helper(getContext());
        studentID = binding.lgoinStudentID;
        studentPass = binding.loginStudentPass;




        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db_helper.studentGetDetailsById(studentID.getText().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        db_helper.studentGetDetailsById(studentID.getText().toString()).removeEventListener(this);
                        Student student = null;
                        System.out.println(snapshot);

                        if(snapshot.exists()){

                            for (DataSnapshot one: snapshot.getChildren()) {
                                student = one.getValue(Student.class);
                                student.setKeey(one.getKey());
                                break;
                            }

                            if(student.getStudent_password().equals(studentPass.getText().toString().trim())){
                                StudentDB_Helper.setStudentKey(student.getKeey());
                                Intent intent = new Intent(requireActivity(), MainActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }else {
                                studentPass.setError("Password or Student Id is wrong");
                            }


                        }else {
                            studentID.setError("Can't find Student");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
        });

    }
}