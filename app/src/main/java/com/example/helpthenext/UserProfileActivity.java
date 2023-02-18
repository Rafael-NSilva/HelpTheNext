package com.example.helpthenext;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.helpthenext.dataclasses.DAOUserInfo;
import com.example.helpthenext.dataclasses.UserInfoCallback;
import com.example.helpthenext.dataclasses.UserInformation;

import java.util.List;

public class UserProfileActivity extends AppCompatActivity implements UserInfoCallback {
    private Button buttonEditProfile, buttonChangePassword;
    private TextView fullName, gender, birthdate;
    private ImageView avatar;
    private Toolbar toolbar;

    private DAOUserInfo daoUserInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initComponents();

        buttonEditProfile=findViewById(R.id.btnEditProfile);
        buttonEditProfile.setOnClickListener((v) -> {
            Intent editProfileIntent = new Intent(UserProfileActivity.this, EditProfileActivity.class);
            startActivity(editProfileIntent);
        });

        buttonChangePassword=(Button)findViewById(R.id.btnChangePass);
        buttonChangePassword.setOnClickListener((v) -> {
            startActivity(new Intent(UserProfileActivity.this, ChangePasswordActivity.class));
        });

        toolbar=findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener((v) -> {
            startActivity(new Intent(UserProfileActivity.this, MapActivity.class));
        });

        daoUserInfo.readCurrentUser(this);
    }

    @Override
    public void loadUserInfo(UserInformation userInformation) {
        fullName.setText(userInformation.getFirstName() + " " + userInformation.getLastName());
        gender.setText(userInformation.getGender());
        birthdate.setText(userInformation.getBirthDate());
        daoUserInfo.getImage(userInformation.getProfileImageRef(), this);
    }

    @Override
    public void loadUsersInfo(List<UserInformation> userInformationList) {

    }

    @Override
    public void loadImage(Uri userImage) {
        Glide.with(this).load(userImage).into(avatar);
    }

    public void initComponents(){
        daoUserInfo = new DAOUserInfo();
        fullName=findViewById(R.id.fullName);
        gender=findViewById(R.id.gender);
        birthdate=findViewById(R.id.birthdate);
        avatar=findViewById(R.id.avatar);
    }
}