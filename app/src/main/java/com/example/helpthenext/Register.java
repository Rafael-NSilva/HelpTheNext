package com.example.helpthenext;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Register extends AppCompatActivity {
    EditText firstName, lastName, password, confPassword, mail;
    Button signInBtn;
    TextView goLoginBtn;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    RadioGroup radioGroup;
    RadioButton radioButton;
    RadioButton other;
    TextView birthdayDate;
     DatePickerDialog.OnDateSetListener dateSetListener;
     Date btdDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstName = findViewById(R.id.firstname);
        lastName = findViewById(R.id.lastname);
        password = findViewById(R.id.pass);
        confPassword = findViewById(R.id.confPass);
        mail = findViewById(R.id.email);
        goLoginBtn = findViewById(R.id.goLog);
        signInBtn = findViewById(R.id.signBtn);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        radioGroup = findViewById(R.id.radioGroup);
        other = findViewById(R.id.radioButton3);


        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        birthdayDate = (TextView) findViewById(R.id.bDate);
        birthdayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(Register.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        dateSetListener,
                        year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String pattern = "dd-MM-yyyy";
                btdDate = new GregorianCalendar(year, month, day).getTime();
                Date currentDate = new Date();
                if(btdDate.before(currentDate)) {
                    String missingDateText = day + "-" + (month + 1) + "-" + year;
                    birthdayDate.setText(missingDateText);
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(Register.this).create();
                    alertDialog.setTitle(getString(R.string.alert_title_wrong_date));
                    alertDialog.setMessage(getString(R.string.alert_desc_wrong_date_after));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.alert_accept),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    alertDialog.show();
                }


            }
        };

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fName = firstName.getText().toString().trim();
                String lName = lastName.getText().toString().trim();
                String email = mail.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String confPass = confPassword.getText().toString().trim();
                int radioBtnID = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioBtnID);
                String radioBtnText = radioButton.getText().toString().trim();
                String date = birthdayDate.getText().toString();
                if (TextUtils.isEmpty(fName)) {
                    firstName.setError("First Name is Required.");
                    return;
                }
                if (TextUtils.isEmpty(lName)) {
                    lastName.setError("Last Name is Required.");
                    return;
                }
                if (TextUtils.isEmpty(radioBtnText)) {
                    other.setError("Genre is Required.");
                    return;
                }
                if (date.equals(" Data de Nascimento")) {
                    birthdayDate.setError("Birth Date is Required.");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    mail.setError("Email is Required.");
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    password.setError("Password is Required.");
                    return;
                }
                if (pass.length() < 4) {
                    password.setError("Password must be >= 4 characters.");
                    return;
                }
                if (!pass.equals(confPass)) {
                    confPassword.setError("Password Incorrect.");
                    return;
                }


                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "User Created", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(auth.getUid())) {
                            Toast.makeText(getApplicationContext(), "Email is already in use", Toast.LENGTH_SHORT).show();
                        } else {
                            databaseReference.child("Users").child(auth.getUid().toString()).child("firstName").setValue(fName);
                            databaseReference.child("Users").child(auth.getUid().toString()).child("lastName").setValue(lName);
                            databaseReference.child("Users").child(auth.getUid().toString()).child("email").setValue(email);
                            databaseReference.child("Users").child(auth.getUid().toString()).child("gender").setValue(radioBtnText);
                            databaseReference.child("Users").child(auth.getUid().toString()).child("birthDate").setValue(date);
                            databaseReference.child("Users").child(auth.getUid().toString()).child("profileImageRef").setValue("gs://help-the-next.appspot.com/users/Sample_User_Icon.png");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                startActivity(new Intent(getApplicationContext(), MapActivity.class));
            }
        });
        goLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }
}