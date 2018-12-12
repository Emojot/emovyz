package com.emojot.emovyz.workflow;

import com.google.gson.JsonObject;

public interface WorkflowInteractionCallback {

    void onSuccess(JsonObject data);
    void onError(String errorMsg);
}
