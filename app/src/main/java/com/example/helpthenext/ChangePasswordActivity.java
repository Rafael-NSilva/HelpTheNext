package com.example.helpthenext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class ChangePasswordActivity extends AppCompatActivity {
    private Button buttonConfirmChangePassword;
    private TextInputEditText textInputEditTextPassword, textInputEditTextConfirmPassword;
    private TextInputLayout textInputLayoutPassword, textInputLayoutConfirmPassword;
    private String storedError = "";
    private Toolbar toolbar;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        toolbar=findViewById(R.id.toolbar);
        buttonConfirmChangePassword=findViewById(R.id.btnConfirmChangePassword);
        textInputEditTextPassword=findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword=findViewById(R.id.textInputEditTextConfirmPassword);
        textInputLayoutPassword=findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword=findViewById(R.id.textInputLayoutConfirmPassword);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        textInputEditTextPassword.setOnFocusChangeListener((view, b) -> {
            if(textInputEditTextPassword.isFocused() || textInputEditTextConfirmPassword.isFocused()){
                textInputLayoutPassword.setError(null);
                textInputLayoutConfirmPassword.setError(null);
            } else if(!storedError.equals("")) {
                textInputLayoutPassword.setError(storedError);
                textInputLayoutConfirmPassword.setError(storedError);
            }
            });

        textInputEditTextConfirmPassword.setOnFocusChangeListener((view, b) -> {
            if(textInputEditTextConfirmPassword.isFocused() || textInputEditTextPassword.isFocused()){
                textInputLayoutPassword.setError(null);
                textInputLayoutConfirmPassword.setError(null);
            } else if(!storedError.equals("")) {
                textInputLayoutPassword.setError(storedError);
                textInputLayoutConfirmPassword.setError(storedError);
            }
        });

        buttonConfirmChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(textInputEditTextPassword.getText().toString()) &&
                        TextUtils.isEmpty(textInputEditTextConfirmPassword.getText().toString())) {
                    storedError = getResources().getString(R.string.throw_error_empty_field);
                    textInputLayoutPassword.setError(storedError);
                    textInputLayoutConfirmPassword.setError(storedError);
                } else if(!Objects.equals(textInputEditTextConfirmPassword.getText().toString(), textInputEditTextPassword.getText().toString())){
                    storedError = getResources().getString(R.string.throw_error_different_passwords);
                    textInputLayoutConfirmPassword.setError(storedError);
                }
                else{
                    textInputLayoutPassword.setError(null);
                    textInputLayoutConfirmPassword.setError(null);
                    startActivity(new Intent(ChangePasswordActivity.this, UserProfileActivity.class));
                }
                user.updatePassword(textInputEditTextPassword.getText().toString());
            }
        });

        toolbar=findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener((v) -> {
            super.onBackPressed();
        });
    }

}
