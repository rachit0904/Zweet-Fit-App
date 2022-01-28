package com.practise.zweet_fit_app.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.practise.zweet_fit_app.Activity.BlankActivity;
import com.practise.zweet_fit_app.Activity.MainActivity;
import com.practise.zweet_fit_app.Activity.SignUp;
import com.practise.zweet_fit_app.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class otp extends Fragment implements View.OnClickListener {
    FirebaseAuth mAuth;
    ImageView bck;
    TextInputEditText otp;
    TextView resend;
    View view;
    Button submit;
    String no = ""; //phone no will be iniitialized here
    private static String TAG = "com.zweet.fitnessapplication.Fragments";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_otp, container, false);
        bck = view.findViewById(R.id.bck);
        otp = view.findViewById(R.id.edit_otp);
        resend = view.findViewById(R.id.resend);
        submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(this);
        resend.setOnClickListener(this);
        bck.setOnClickListener(this);
        no = requireActivity().getIntent().getStringExtra("phoneno");
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == bck) {
            Intent intent = new Intent(getActivity(), BlankActivity.class);
            intent.putExtra("activity", "login");
            intent.putExtra("phoneno", no);
            startActivity(intent);
        }
        if (view == resend) {
            Snackbar.make(view, "OTP resent at " + no + "!", Snackbar.LENGTH_SHORT).show();
        }
        if (view == submit) {
            String verificationId = requireActivity().getIntent().getStringExtra("vid");
            String code = Objects.requireNonNull(otp.getText()).toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithPhoneAuthCredential(credential);


        }
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if(verifyNewUser()) {
                                Intent intent = new Intent(getActivity(), SignUp.class);
                                startActivity(intent);
                                getActivity().finish();
                            }else{
//                                try {
//                                    saveToRealm();
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Snackbar.make(view, "OTP Invalid !", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }


                });
    }





    private boolean verifyNewUser() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("DD-MM-YYYY hh:mm:ss");
        Date now = new Date();
        String[] cur = dateFormat.format(now).split(" ");
        String curDate = cur[0];
        String curDD = curDate.split("-")[0];
        String curMM = curDate.split("-")[1];
        String curYY = curDate.split("-")[2];
        String curTime = cur[1];
        String curT[] = curTime.split(":");
        String curHrs = curT[0];
        String curMin = curT[1];
        Timestamp ts = new Timestamp(new Date(mAuth.getCurrentUser().getMetadata().getCreationTimestamp()));
        Date date = ts.toDate();
        String creation[] = dateFormat.format(date).split(" ");
        String creationDate = creation[0];
        String creationDD = creationDate.split("-")[0];
        String creationMM = creationDate.split("-")[1];
        String creationYY = creationDate.split("-")[2];
        String creationTime = creation[1];
        String creationHrs = creationTime.split(":")[0];
        String creationMin = creationTime.split(":")[1];
        if (Integer.parseInt(creationYY) == Integer.parseInt(curYY)
                && Integer.parseInt(creationMM) == Integer.parseInt(curMM)
                && Integer.parseInt(creationDD) == Integer.parseInt(curDD)) {
            if (Integer.parseInt(curHrs) == Integer.parseInt(creationHrs)
                    && (Integer.parseInt(curMin) - Integer.parseInt(creationMin)) < 2) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}