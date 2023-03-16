package com.example.designmobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class Onboarding extends AppCompatActivity {

    public static String avatar;
    public static String nickName;
    SharedPreferences sharedPreferences; // для сохранения настроек пользователя
    public static final String APP_PREFERENCES = "mysettings"; // сохранение параметров
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

         sharedPreferences = this.getSharedPreferences(
                APP_PREFERENCES, Context.MODE_PRIVATE);
        if (sharedPreferences != null) // если настройки, а именно емайл не пустой, переходим к паролю{
        {
            avatar = sharedPreferences.getString("Avatar", "");
            nickName =sharedPreferences.getString("NickName", "");
            startActivity(new Intent(this, Main_activity.class));
        }
    }
    public void goRegistration(View v){
        startActivity(new Intent(this, Registration.class));
    }
    public void goAuthorization(View v){
        startActivity(new Intent(this, Login_activity.class));
    }
}