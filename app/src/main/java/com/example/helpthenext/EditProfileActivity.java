package com.example.helpthenext;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.helpthenext.dataclasses.DAOUserInfo;
import com.example.helpthenext.dataclasses.UserInfoCallback;
import com.example.helpthenext.dataclasses.UserInformation;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView birthdateText;
    private Button btnApplyUserChanges;
    private ImageView changeAvatar, avatar;
    private TextInputLayout textInputLayoutFirstName, textInputLayoutLastName;
    private TextInputEditText textInputFirstName, textInputLastName;
    private AutoCompleteTextView autoCompleteTextGender;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private LinearLayout bottomSheetLayoutContainer, linearLayoutParent, linearLayoutImagesViews, linearLayoutTextViews;

    private Uri personImageUri;
    private boolean hasChoosenImage = false;
    private ActivityResultLauncher<Intent> imagePickerResultLauncher;
    private String genderChange;
    private ArrayAdapter<String> arrayAdapter;
    private static final int  EXTERNAL_STORAGE_PERMISSION_CODE=111, CAMERA_REQUEST_CODE=9999, CAMERA_PERMISSION_CODE = 3;

    private DAOUserInfo daoUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        daoUserInfo = new DAOUserInfo();

        changeAvatar = findViewById(R.id.changeAvatar);
        avatar = findViewById(R.id.avatar);
        textInputLayoutFirstName = findViewById(R.id.textInputLayoutFirstName);
        textInputFirstName = findViewById(R.id.textInputFirstName);
        textInputLayoutLastName = findViewById(R.id.textInputLayoutLastName);
        textInputLastName = findViewById(R.id.textInputLastName);
        autoCompleteTextGender = findViewById(R.id.autoCompleteTextGender);
        birthdateText = findViewById(R.id.birthdateText);
        btnApplyUserChanges = findViewById(R.id.btnApplyUserChanges);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener((v) -> {
            super.onBackPressed();
        });

        changeAvatar.setOnClickListener(view -> {
            showDialog();
        });

        arrayAdapter = new ArrayAdapter<>(this, R.layout.dropdown_list_item, getResources().getStringArray(R.array.gender_array));
        autoCompleteTextGender.setAdapter(arrayAdapter);

        autoCompleteTextGender.setOnItemClickListener((parent, view, position, id) -> {
             genderChange = parent.getItemAtPosition(position).toString();
        });

        birthdateText.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfileActivity.this,
                    dateSetListener,
                    year, month, day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
            datePickerDialog.show();
        });

        dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = day + "/" + month + "/" + year;
            birthdateText.setText(date);
        };

        btnApplyUserChanges.setOnClickListener(view -> {
            //Enviar os dados para a db
            if(!TextUtils.isEmpty(textInputFirstName.getText().toString())){
                daoUserInfo.changeInfo(FirebaseAuth.getInstance().getUid(), "firstName", textInputFirstName.getText().toString());
            }
            if(!TextUtils.isEmpty(textInputLastName.getText().toString())){
                daoUserInfo.changeInfo(FirebaseAuth.getInstance().getUid(), "lastName", textInputLastName.getText().toString());
            }
            if (genderChange != null){
                daoUserInfo.changeInfo(FirebaseAuth.getInstance().getUid(), "gender", genderChange);
            }
            if (!birthdateText.getText().toString().equals("")){
                daoUserInfo.changeInfo(FirebaseAuth.getInstance().getUid(), "birthDate", birthdateText.getText().toString());
            }
            daoUserInfo.uploadImage(personImageUri, FirebaseAuth.getInstance().getUid());
            startActivity(new Intent(this, UserProfileActivity.class));
        });
    }

    private void showDialog() {
        final BottomSheetDialog imageChooserDialog = new BottomSheetDialog(this, R.style.CustomBottomSheetDialog);
        View imageChooserDialogView = LayoutInflater.from(this)
                .inflate(R.layout.bottom_sheet_layout, bottomSheetLayoutContainer=findViewById(R.id.bottom_sheet_layout_container) );
        linearLayoutParent = imageChooserDialogView.findViewById(R.id.linearLayoutParent);
        linearLayoutImagesViews = imageChooserDialogView.findViewById(R.id.linearLayoutImagesViews);
        linearLayoutTextViews = imageChooserDialogView.findViewById(R.id.linearLayoutTextViews);

        imageChooserDialogView.findViewById(R.id.camera).setOnClickListener(view -> {
            if(ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
            }
            if(ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);
            }
            openCamera();
        });
        imageChooserDialogView.findViewById(R.id.gallery).setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            imagePickerResultLauncher.launch(intent);
        });
        imageChooserDialogView.findViewById(R.id.cancel).setOnClickListener(view -> imageChooserDialog.dismiss());
        imageChooserDialog.setContentView(imageChooserDialogView);
        imageChooserDialog.show();
    }

    private void openCamera() {
        Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camIntent, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case CAMERA_PERMISSION_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openCamera();
                }
                else{
                    Toast.makeText(this, getResources().getString(R.string.throw_error_permission_denied), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA_REQUEST_CODE){
            Bitmap image = (Bitmap)data.getExtras().get("data");
            avatar.setImageBitmap(image);
            personImageUri = getImageUri(this, image);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        String path =
                MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage,
                        "Title", null);
        return Uri.parse(path);
    }
}


