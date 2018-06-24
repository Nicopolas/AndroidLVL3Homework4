package com.zakharov.nicolay.androidlvl3homework4;

/**
 * Created by 1 on 24.06.2018.
 */

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestAPIforUser {
    @GET("users/{user}")
    Call<RetrofitModel> loadUser(@Path("user") String user);

    @GET("users/{user}/repos")
    Call<List<ReposRetrofitModel>> loadUsersRepos(@Path("user") String user);
}
