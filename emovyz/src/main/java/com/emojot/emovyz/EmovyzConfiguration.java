package com.emojot.emovyz;
import android.support.annotation.NonNull;

public class EmovyzConfiguration {

    private static EmovyzConfiguration mEmovyzConfiguration;
    private @NonNull String mToken = null;
    private String mClientId;
    private String mClientSecret;
    private String mCompanyId;

    private EmovyzConfiguration(String token,String companyId){
        mToken = token;
        mCompanyId = companyId;
    }

    private EmovyzConfiguration(String clientId, String clientSecret,String companyId){
        mClientId = clientId;
        mClientSecret = clientSecret;
        mCompanyId = companyId;
    }

    public static EmovyzConfiguration getInstance(){
        if(mEmovyzConfiguration == null){
            throw new IllegalStateException("Please initialize before access emovyz SDK");
        }
        return mEmovyzConfiguration;
    }

    public static void init(@NonNull String token,String companyId){
        mEmovyzConfiguration = new EmovyzConfiguration(token,companyId);
    }

    public static void init(@NonNull String clientId,@NonNull String  clientSecret,String companyId){
        mEmovyzConfiguration = new EmovyzConfiguration(clientId,clientSecret,companyId);
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

    public String getCompanyId(){
        return mCompanyId;
    }

}
