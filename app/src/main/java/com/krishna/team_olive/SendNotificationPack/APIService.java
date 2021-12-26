package com.krishna.team_olive.SendNotificationPack;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAEnHOHYw:APA91bGPHNBZqJ1eUMa_OKvsdtIhPKMaB5f-hWb5Kfyvp7mnWBXwRJg6yA7Ab4ITFpsBoizfQxAZGu00BjiRAO_mKSZgGts8wm7yWEwZD_PaqCYz_3z1DURRAasTKmvYfy0qrAiIyBlV" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}
