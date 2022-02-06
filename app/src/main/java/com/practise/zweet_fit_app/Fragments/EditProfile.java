package com.practise.zweet_fit_app.Fragments;

import static android.app.Activity.RESULT_OK;

import static com.practise.zweet_fit_app.Activity.MainActivity.TAG;

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
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends Fragment implements View.OnClickListener {
    TextInputEditText name, dob, target, wt, ht;
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
        dob = view.findViewById(R.id.edit_dob);
        target = view.findViewById(R.id.edit_Target);
        wt = view.findViewById(R.id.edit_Weight);
        ht = view.findViewById(R.id.edit_Height);
        save = view.findViewById(R.id.save);
        progressBar = view.findViewById(R.id.progressBar);
        pref= getActivity().getSharedPreferences("user data", Context.MODE_PRIVATE);
        uploadPic.setOnClickListener(this);
        save.setOnClickListener(this);
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        setData();
        return view;
    }

    private void setData() {
        name.setText(pref.getString("name",""));
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
        if (view == uploadPic) {
            choosePicture();
        }
        if (view == save) {
            progressBar.setVisibility(View.VISIBLE);
            upload();
        }
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
                        progressBar.setVisibility(View.GONE);
                        Intent intent=new Intent(getActivity(), BlankActivity.class);
                        intent.putExtra("activity","profile");
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
            }
        });
    }


}
