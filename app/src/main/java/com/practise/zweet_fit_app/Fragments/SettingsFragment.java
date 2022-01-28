package com.practise.zweet_fit_app.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.practise.zweet_fit_app.Activity.BlankActivity;
import com.practise.zweet_fit_app.R;


public class SettingsFragment extends Fragment implements View.OnClickListener {
    CoordinatorLayout subsPlan,units;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_settings, container, false);
        units=view.findViewById(R.id.units);
        subsPlan=view.findViewById(R.id.subsPlan);
        units.setOnClickListener(this);
        subsPlan.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if(view==subsPlan){
            Intent intent=new Intent(getActivity(), BlankActivity.class);
            intent.putExtra("activity","premium");
            startActivity(intent);
        }
        if(view==units){
            AlertDialog.Builder unitDialogBuilder=new AlertDialog.Builder(getContext());
            View dialogView=getLayoutInflater().inflate(R.layout.unit_chooser_dialog,null);
            RadioGroup heightUnit=dialogView.findViewById(R.id.heightUnit);
            RadioGroup weightUnit=dialogView.findViewById(R.id.weightUnit);
            TextView save=dialogView.findViewById(R.id.save);
            unitDialogBuilder.setView(dialogView);
            AlertDialog dialog=unitDialogBuilder.create();
            dialog.setCancelable(false);
            dialog.show();
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RadioButton height=dialogView.findViewById(heightUnit.getCheckedRadioButtonId());
                    RadioButton weight=dialogView.findViewById(weightUnit.getCheckedRadioButtonId());
                    dialog.dismiss();
                }
            });
        }
    }
}