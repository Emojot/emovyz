package com.emojot.emovyz.network;

public interface TokenRefreshCallback {
    void onSuccess(String token, int requestCode);
    void onError();
}
