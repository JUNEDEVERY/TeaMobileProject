package com.example.designmobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login_activity extends AppCompatActivity {
    boolean isDog;
    EditText etEmail, etPassword;
    SharedPreferences sharedPreferences; // для сохранения настроек пользователя
    public static final String APP_PREFERENCES = "mysettings"; // сохранение параметров
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        sharedPreferences = this.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (sharedPreferences != null) // если настройки, а именно емайл не пустой, переходим к паролю{
        {
            etEmail.setText(sharedPreferences.getString("Email", ""));
            etPassword.requestFocus();
        }
    }
    public void goRegistration(View v){
        startActivity(new Intent(this, Registration.class));

    }
    public void checkDog(View v){
        if (etEmail.getText().length() == 0 || etPassword.getText().length() == 0 ) {
            Toast.makeText(this, "Возможно одно или несколько полей были незаполнены", Toast.LENGTH_LONG).show();
            return;
        }
        else{
            // создаем регулярку на проверку собаки в поле емайла
            Pattern pattern = Pattern.compile("@");
            Matcher matcher = pattern.matcher(etEmail.getText().toString());
            isDog = matcher.matches();
            if(isDog == true){
                getLogin();
            }
            else{
                Toast.makeText(this, "Не найден символ, отделяющий имя пользователя и домен - @", Toast.LENGTH_LONG).show();

            }
        }
    }
    private void getLogin(){
        String email = String.valueOf(etEmail.getText());
        String password = String.valueOf(etPassword.getText());

        retrofit2.Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://mskko2021.mad.hakta.pro/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitApi retrofitAPI = retrofit.create(RetrofitApi.class);

        mSend mSend = new mSend(email, password);
        Call<mUser> call = retrofitAPI.User(mSend);

        call.enqueue(new Callback<mUser>() {
            @Override
            public void onResponse(Call<mUser> call, Response<mUser> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(Login_activity.this, "Учетная запись не найдена", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(response.body() != null)
                {
                    if(response.body().getToken() != null)
                    {
                        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                        sharedPreferences.edit().putString("Email", "" + email).apply();
                        sharedPreferences.edit().putString("Avatar", "" + response.body().getAvatar()).apply();
                        sharedPreferences.edit().putString("NickName", "" + response.body().getNickName()).apply();

                        Onboarding.avatar = response.body().getAvatar();
                        Onboarding.nickName = response.body().getNickName();

                        Intent intent = new Intent(Login_activity.this, Main_activity.class);
                        Bundle bundle = new Bundle();

                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<mUser> call, Throwable t) {
                Toast.makeText(Login_activity.this, "При попытке авторизации в системе произошла следующая ошибка " + t.getMessage(), Toast.LENGTH_LONG).show();
            }


        });
    }
}