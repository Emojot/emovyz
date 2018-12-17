package com.emojot.emovyz.network;

import com.emojot.emovyz.EmovyzConfiguration;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderIntercepter implements Interceptor {
    EmovyzConfiguration emovyzConfiguration = EmovyzConfiguration.getInstance();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Request tokenRequest = request.newBuilder()
                .addHeader("companyID", emovyzConfiguration.getCompanyId())
                .addHeader("Content-Type", "application/json")
                .build();

        return chain.proceed(tokenRequest);
    }
}
