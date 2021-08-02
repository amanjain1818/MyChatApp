package com.example.mychatapp.Fragments;

import com.example.mychatapp.Notifications.Response;
import com.example.mychatapp.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAUm77XjM:APA91bEe4mxOEi_wfawDvTh8TB5Zk9wMeQcxLroAhojK8tdtoAGCW5e4Z7yD8nLiEqj5pAnApOQXbkdWVnlW_QvHs362UxqG8VErtt0j3-qAkqbEMjgj5pqeean7G3SwktyEa_L-XvSz"
            }
    )
    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);
}
