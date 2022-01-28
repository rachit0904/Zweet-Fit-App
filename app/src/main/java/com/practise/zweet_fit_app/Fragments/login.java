package com.practise.zweet_fit_app.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.practise.zweet_fit_app.Activity.BlankActivity;
import com.practise.zweet_fit_app.R;


import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class login extends Fragment implements View.OnClickListener {
    TextInputEditText phoneNo;
    Button next;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        phoneNo = view.findViewById(R.id.edit_No);
        next = view.findViewById(R.id.next);
        next.setOnClickListener(this);
//        String no=getActivity().getIntent().getStringExtra("phoneno");
//        if(!no.equals("")){
//            phoneNo.setText(no);
//        }
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == next) {
            //yaha se no leke otp wale frag me send krege for validation okay give me 1 min

            String phoneNum = Objects.requireNonNull(phoneNo.getText()).toString();

// Whenever verification is triggered with the whitelisted number,
// provided it is not set for auto-retrieval, onCodeSent will be triggered.
            FirebaseAuth auth = FirebaseAuth.getInstance();
            PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phoneNum)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(requireActivity())

                    .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onCodeSent(@NonNull String verificationId,
                                               @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            // Save the verification id somewhere
                            // ...
                            Intent intent = new Intent(getActivity(), BlankActivity.class);
                            intent.putExtra("activity", "otp");
                            intent.putExtra("phoneno",phoneNum);
                            intent.putExtra("vid",verificationId);
                            startActivity(intent);
                            // The corresponding whitelisted code above should be used to complete sign-in.
                            //verification id and forece token next activity or fragment me pass krna h
                            //aap pass kroo ye m next page pe verification wali check krlunga
                            //ok
                        }

                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            // Sign in with the credential
                            // ...
                        }

                        @Override
                        public void onVerificationFailed(FirebaseException e) {
                            // ...
                        }
                    })
                    .build();
            PhoneAuthProvider.verifyPhoneNumber(options);



        }
    }
}