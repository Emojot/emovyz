package com.emojot.emovyz.network;


import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiEndpoint {

    @POST("interactionData")
    Call<JsonObject> postInteractionData(@Header("Authorization") String token, @Body JsonObject interactionObject);

    @GET("interactionBasedSurvey")
    Call<JsonObject> getContextualSurvey(@Header("Authorization") String token, @QueryMap Map<String, String> queryMap);

    @POST("https://dev.emojot.com:9448/oauth2/token")
    Call<JsonObject> getToken(@HeaderMap Map<String, String> headerMap, @Query("grant_type") String clientCredential);

}
