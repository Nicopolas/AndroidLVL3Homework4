package com.zakharov.nicolay.androidlvl3homework4;

/**
 * Created by 1 on 24.06.2018.
 */

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RestAPI {
    @GET("users")
    Call<List<RetrofitModel>> loadUsers();
}
