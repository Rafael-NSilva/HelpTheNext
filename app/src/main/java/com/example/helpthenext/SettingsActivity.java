package com.example.helpthenext;

import static android.hardware.Sensor.TYPE_LIGHT;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity implements SensorEventListener{
    private Toolbar toolbar;
    private SwitchMaterial switchTheme, switchLanguage;
    private CheckBox checkBox;
    private ImageView lightModeIcon, darkModeIcon;

    private SensorManager sensorManager;
    private Sensor lightSensor;

    private boolean automaticModes = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.Theme_HelpTheNext_DarkTheme); //when dark mode is enabled, we use the dark them
        } else {
            setTheme(R.style.Theme_HelpTheNext);  //default app theme
        }
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_settings);

        checkBox=findViewById(R.id.checkBox);
        switchTheme=findViewById(R.id.switchTheme);
        lightModeIcon=findViewById(R.id.lightModeIcon);
        darkModeIcon=findViewById(R.id.darkModeIcon);
        switchLanguage=findViewById(R.id.switchLanguage);
        toolbar=findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(view->{
            startActivity(new Intent(this, MapActivity.class));
        });

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(TYPE_LIGHT);


        detectingLightSensor(lightSensor);

        lightModeIcon.setEnabled(false);
        switchTheme.setEnabled(false);
        darkModeIcon.setEnabled(false);
        checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if(checkBox.isChecked()){
                lightModeIcon.setEnabled(false);
                switchTheme.setEnabled(false);
                darkModeIcon.setEnabled(false);
                automaticModes = true;
                sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
            else{
                lightModeIcon.setEnabled(true);
                switchTheme.setEnabled(true);
                darkModeIcon.setEnabled(true);
                automaticModes = false;
                sensorManager.unregisterListener(this);
            }
        });

        switchTheme.setOnCheckedChangeListener((compoundButton, b) -> {
            if(switchTheme.isChecked()){
                switchTheme.getThumbDrawable().setColorFilter(Color.rgb(66, 129, 164), PorterDuff.Mode.MULTIPLY);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            else {
                switchTheme.getThumbDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        if(Locale.getDefault().getLanguage().equals("en")){
            switchLanguage.getThumbDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
            switchLanguage.setChecked(false);
        }
        else if(Locale.getDefault().getLanguage().equals("pt")){
            switchLanguage.getThumbDrawable().setColorFilter(Color.rgb(66, 129, 164), PorterDuff.Mode.MULTIPLY);
            switchLanguage.setChecked(true);
        }
        switchLanguage.setOnCheckedChangeListener((compoundButton, b) -> {
            switchLanguage.setOnClickListener(view -> recreate());
            if(switchLanguage.isChecked()){
                switchLanguage.getThumbDrawable().setColorFilter(Color.rgb(66, 129, 164), PorterDuff.Mode.MULTIPLY);
                setLocale("pt");
            }
            else {
                switchLanguage.getThumbDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                setLocale("en");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(automaticModes) sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == TYPE_LIGHT){
            float value = sensorEvent.values[0];
            System.out.println(value);
            if(value > 4000f){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                switchTheme.setChecked(false);
            }
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                switchTheme.setChecked(true);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void detectingLightSensor(Sensor light){
        if(light==null){
            Toast.makeText(this, "This device has not light sensor :(", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setLocale (String lang) {
        Locale locale = new Locale (lang) ;
        Locale.setDefault(locale) ;
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config ,getBaseContext().getResources().getDisplayMetrics());
        // save data to shared preferences
        SharedPreferences.Editor editor = getSharedPreferences ("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    public void loadLocale (){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE );
        String language = prefs.getString("My_Lang", "");
        setLocale(language);
    }
}