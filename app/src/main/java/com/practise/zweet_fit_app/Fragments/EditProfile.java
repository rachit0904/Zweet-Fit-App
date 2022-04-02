package com.practise.zweet_fit_app.Fragments;

import static android.app.Activity.RESULT_OK;

import static com.practise.zweet_fit_app.Activity.MainActivity.TAG;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.practise.zweet_fit_app.Activity.BlankActivity;
import com.practise.zweet_fit_app.R;
import com.practise.zweet_fit_app.Server.ServerRequests;
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends Fragment implements View.OnClickListener {
    TextInputEditText name,  target, wt, ht,username;
    Button dob;
    ImageView back;
    CircleImageView dp;
    TextView uploadPic, save;
    Uri imageUri;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseUser user;
    ProgressBar progressBar;
    SharedPreferences.Editor preferences;
    SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        uploadPic = view.findViewById(R.id.uploadDp);
        dp = view.findViewById(R.id.profilePic);
        name = view.findViewById(R.id.edit_Name);
        username = view.findViewById(R.id.edit_usname);
        dob = view.findViewById(R.id.edit_dob);
        target = view.findViewById(R.id.edit_Target);
        wt = view.findViewById(R.id.edit_Weight);
        ht = view.findViewById(R.id.edit_Height);
        save = view.findViewById(R.id.save);
        back = view.findViewById(R.id.back);
        progressBar = view.findViewById(R.id.progressBar);
        pref= getActivity().getSharedPreferences("user data", Context.MODE_PRIVATE);
        uploadPic.setOnClickListener(this);
        save.setOnClickListener(this);
        dob.setOnClickListener(this);
        back.setOnClickListener(this);
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        setData();
        return view;
    }

    private void setData() {
        name.setText(pref.getString("name",""));
        username.setText(pref.getString("usname",""));
        dob.setText(pref.getString("dob",""));
        target.setText(pref.getString("target",""));
        wt.setText(pref.getString("wt",""));
        ht.setText(pref.getString("ht",""));
        if(!pref.getString("dp","").isEmpty()){
            Picasso.get().load(pref.getString("dp","")).into(dp);
        }
    }


    @Override
    public void onClick(View view) {
        if(view==back){
            Intent intent=new Intent(getActivity(), BlankActivity.class);
            intent.putExtra("activity","profile");
            startActivity(intent);
            getActivity().finish();
        }
        if(view == dob){
            Calendar cal = Calendar.getInstance();
            int year = LocalDate.now().getYear();
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                    if(y>=LocalDate.now().getYear() || (LocalDate.now().getYear()-y)<=3){
                        Toast.makeText(getContext(), "Enter a valid DOB!", Toast.LENGTH_SHORT).show();
                    }else{
                        m++;
                        dob.setText(d+"/"+ m+"/"+y);
                        preferences=pref.edit();
                        preferences.putString("dob",d+"/"+m+1 +"/"+y);
                        preferences.apply();
                    }
                }
            },year,month,day);

            datePickerDialog.show();
        }
        if (view == uploadPic) {
            choosePicture();
        }
        if (view == save) {
            progressBar.setVisibility(View.VISIBLE);
            upload();
        }
    }

    private void saveData() {
            preferences = pref.edit();
            preferences.putString("name", name.getText().toString());
            preferences.putString("dob", dob.getText().toString());
            preferences.putString("usname",username.getText().toString());
            preferences.putString("target", target.getText().toString());
            preferences.putString("wt", wt.getText().toString());
            preferences.putString("ht", ht.getText().toString());
            preferences.apply();
            if(saveToDb()) {
                progressBar.setVisibility(View.GONE);
                Intent intent = new Intent(getActivity(), BlankActivity.class);
                intent.putExtra("activity", "profile");
                startActivity(intent);
                getActivity().finish();
            }
    }

    private boolean saveToDb() {
        ServerRequests request=new ServerRequests();
        request.updateUsers(
                pref.getString("id",""),
                pref.getString("name",""),
                pref.getString("dob",""),
                pref.getString("wt",""),
                pref.getString("ht", ""),
                pref.getString("target",""),
                "0",
                "true",
                pref.getString("coins",""),
                "1",
                "80",
                "",
                pref.getString("dp","")
        );
        return true;
    }

    private void choosePicture() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            dp.setImageURI(imageUri);
        }
    }

    void upload() {
        StorageReference storageRef = storage.getReference();
        StorageReference mountainImagesRef = storageRef.child("images/" + pref.getString("id",name.getText().toString()) + ".jpg");
        Bitmap bitmap = ((BitmapDrawable) dp.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "onFailure: " + exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                mountainImagesRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task1) {
                        String url = task1.getResult().toString();
                        Log.e(TAG, "onComplete: " + url);
                        if (!url.trim().equals("")) {
                            preferences=pref.edit();
                            preferences.putString("dp",url);
                            preferences.apply();
                            Picasso.get().load(url).into(dp);

                        }
                        saveData();
                    }
                });
            }
        });
    }


}
