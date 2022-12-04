package com.n21.choizy.bottomSheets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.n21.choizy.R;

public class Feedback_BS extends BottomSheetDialogFragment {

    String branchName;
    TextView companyName;
    EditText feedbackTxt;
    Button submitBtn;
    feedbackListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feedback_bs,container,false);
        companyName = view.findViewById(R.id.feedbackBS_BranchName);
        feedbackTxt = view.findViewById(R.id.feedbackBS_FeedBackTxt);
        submitBtn = view.findViewById(R.id.feedbackBS_submitTxt);

        companyName.setText(branchName);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!feedbackTxt.getText().toString().trim().isEmpty()){
                    if(listener != null){
                        listener.onFeedBackAdded(feedbackTxt.getText().toString().trim());
                        feedbackTxt.setText("");
                        dismiss();
                    }
                }else{
                    feedbackTxt.setError("Please write feedback");
                    feedbackTxt.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            feedbackTxt.setError(null);
                            feedbackTxt.requestFocus();
                        }
                    },4000);
                }
            }
        });

        return view;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public interface feedbackListener{
        void onFeedBackAdded(String feedback);
    }

    public void setListener(feedbackListener listener) {
        this.listener = listener;
    }
}
