package com.emojot.emovyz.util;

import android.util.Base64;

public class Util {

    public static String getAuthorizationCode(String clientId,String clientSecret){
        String secret = clientId+":"+clientSecret;
        return "Basic "+Base64.encodeToString(secret.getBytes(),Base64.NO_WRAP);
    }
}
