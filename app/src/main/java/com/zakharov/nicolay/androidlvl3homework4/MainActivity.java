package com.zakharov.nicolay.androidlvl3homework4;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    private TextView mInfoTextView;
    private TextView userNameTextView;
    private ImageView avatarImageView;
    private ProgressBar progressBar;
    private EditText editText;
    RestAPIforUser restAPIforUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        mInfoTextView = (TextView) findViewById(R.id.tvLoad);
        userNameTextView = (TextView) findViewById(R.id.username);
        avatarImageView = (ImageView) findViewById(R.id.avatar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Button btnLoad = (Button) findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener((v) -> onClick());
    }

    public void onClick() {
        mInfoTextView.setText("");
        Retrofit retrofit = null;
        try {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            restAPIforUser = retrofit.create(RestAPIforUser.class);
        } catch (Exception io) {
            mInfoTextView.setText("no retrofit: " + io.getMessage());
            return;
        }
// подготовили вызов на сервер
        Call<RetrofitModel> call = restAPIforUser.loadUser(editText.getText().toString());
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnected()) {
// запускаем
            try {
                progressBar.setVisibility(View.VISIBLE);
                downloadOneUrl(call);
                downloadAndParsingUsersRepos(restAPIforUser.loadUsersRepos(editText.getText().toString()));
            } catch (IOException e) {
                e.printStackTrace();
                mInfoTextView.setText(e.getMessage());
            }
        } else {
            Toast.makeText(this, "Подключите интернет", Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadOneUrl(Call<RetrofitModel> call) throws IOException {
        userNameTextView.setText("");
        call.enqueue(new Callback<RetrofitModel>() {
            @Override
            public void onResponse(Call<RetrofitModel> call, Response<RetrofitModel> response) {
                if (response.isSuccessful()) {
                    if (response != null) {
                        RetrofitModel curRetrofitModel = response.body();
                        userNameTextView.append("\nLogin = " + curRetrofitModel.getLogin() +
                                "\nId = " + curRetrofitModel.getId() +
                                "\n-----------------");
                        setImage(curRetrofitModel.getAvatarUrl());
                    }
                } else {
                    System.out.println("onResponse error: " + response.code());
                    mInfoTextView.setText("onResponse error: " + response.code());
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<RetrofitModel> call, Throwable t) {
                System.out.println("onFailure " + t);
                mInfoTextView.setText("onFailure " + t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void downloadAndParsingUsersRepos(Call<List<ReposRetrofitModel>> call) throws IOException {
        userNameTextView.setText("");
        call.enqueue(new Callback<List<ReposRetrofitModel>>() {
            @Override
            public void onResponse(Call<List<ReposRetrofitModel>> call, Response<List<ReposRetrofitModel>> response) {
                if (response.isSuccessful()) {
                    if (response != null) {
                        ReposRetrofitModel reposRetrofitModel = null;
                        mInfoTextView.append("\nUser's repositories:");
                        for (int i = 0; i < response.body().size(); i++) {
                            reposRetrofitModel = response.body().get(i);
                            mInfoTextView.append("\n " + String.valueOf(i+1) + " " + reposRetrofitModel.getName());
                        }
                        mInfoTextView.append("\n-----------------");
                    }
                } else {
                    System.out.println("onResponse error: " + response.code());
                    mInfoTextView.setText("onResponse error: " + response.code());
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<ReposRetrofitModel>> call, Throwable t) {
                System.out.println("onFailure " + t);
                mInfoTextView.setText("onFailure " + t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setImage(String imageUrl) {
        Glide.with(this)
                .load(imageUrl)
                .into(avatarImageView);
    }
}
