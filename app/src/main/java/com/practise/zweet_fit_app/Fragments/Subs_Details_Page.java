package com.practise.zweet_fit_app.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.practise.zweet_fit_app.R;

public class Subs_Details_Page extends Fragment implements View.OnClickListener {
    ImageView bck;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_subs__details__page, container, false);
        bck=view.findViewById(R.id.bck);
        bck.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if(view==bck){
            getActivity().finish();
        }
    }
}