package com.emojot.emovyz;
import android.support.annotation.NonNull;

public class EmovyzConfiguration {

    private static EmovyzConfiguration mEmovyzConfiguration;
    private @NonNull String mToken = null;
    private String mClientId;
    private String mClientSecret;

    private EmovyzConfiguration(String token){
        mToken = token;
    }

    private EmovyzConfiguration(String clientId, String clientSecret){
        mClientId = clientId;
        mClientSecret = clientSecret;
    }

    public static EmovyzConfiguration getInstance(){
        if(mEmovyzConfiguration == null){
            throw new IllegalStateException("Please initialize before access emovyz SDK");
        }
        return mEmovyzConfiguration;
    }

    public static void init(@NonNull String token){
        mEmovyzConfiguration = new EmovyzConfiguration(token);
    }

    public static void init(@NonNull String clientId,@NonNull String  clientSecret){
        mEmovyzConfiguration = new EmovyzConfiguration(clientId,clientSecret);
    }

    public String getToken() {
        return "Bearer "+mToken;
    }

    @NonNull
    public void setToken(@NonNull String token) {
         mToken = token;
    }


    public String getClientId(){
        return mClientId;
    }

    public String getClientSecret(){
        return mClientSecret;
    }

}
