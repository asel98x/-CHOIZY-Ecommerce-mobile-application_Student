package com.n21.choizy.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.n21.choizy.R;
import com.n21.choizy.myLocal;

public class LanguageDialog extends DialogFragment {

    RadioGroup langRG;
    RadioButton engRB,sinRB,tamilRB;
    languageListener languageListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.language_dialog,null);
        builder.setView(view);

        langRG = view.findViewById(R.id.langRG);
        engRB = view.findViewById(R.id.langEng);
        sinRB = view.findViewById(R.id.langSin);
        tamilRB = view.findViewById(R.id.langTamil);

        String lang = myLocal.getLang(getContext());
        switch (lang){
            case "en":
                engRB.setChecked(true);
                break;
            case "si":
                sinRB.setChecked(true);
                break;
            case "ta":
                tamilRB.setChecked(true);
                break;
        }


        langRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.langSin:
                        if(languageListener != null){
                            languageListener.onLangeSelected("si");
                            dismiss();
                        }
                        break;
                    case R.id.langTamil:
                        if(languageListener != null){
                            languageListener.onLangeSelected("ta");
                            dismiss();
                        }
                        break;
                    case R.id.langEng:
                    default:
                        if(languageListener != null){
                            languageListener.onLangeSelected("en");
                            dismiss();
                        }
                        break;
                }
            }
        });



        return builder.create();
    }

    public interface languageListener{
        void onLangeSelected(String lang);
    }

    public void setLanguageListener(LanguageDialog.languageListener languageListener) {
        this.languageListener = languageListener;
    }

    @Override
    public void onResume() {
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
        super.onResume();
    }
}
