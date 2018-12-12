package com.emojot.emovyz.network;

import com.emojot.emovyz.EmovyzConfiguration;
import com.emojot.emovyz.util.Util;
import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokenRefreshHandler {
    ApiEndpoint mApiEndpoint;
    private static final String TAG = TokenRefreshHandler.class.getSimpleName();


    public TokenRefreshHandler(){
        mApiEndpoint = APIClient.getClient().create(ApiEndpoint.class);
    }

    public void getAPIToken(final int requestCode, final TokenRefreshCallback tokenRefreshCallback) {
        Util util = new Util();
        final EmovyzConfiguration emovyzConfiguration = EmovyzConfiguration.getInstance();
        HashMap<String, String> headerMap = new HashMap();
        headerMap.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        headerMap.put("Authorization", util.getAuthorizationCode(emovyzConfiguration.getClientId(),emovyzConfiguration.getClientSecret()));

            mApiEndpoint.getToken(headerMap, "client_credentials").enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        String token = response.body().getAsJsonObject().get("access_token").getAsString();
                        emovyzConfiguration.setToken(token);
                        //authTokenCallback.onAuthTokenCreationSuccess();
                        tokenRefreshCallback.onSuccess(token,requestCode);
                    } else {
                        tokenRefreshCallback.onError();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    t.printStackTrace();
                    tokenRefreshCallback.onError();
                }
            });
    }
}
