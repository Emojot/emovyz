package com.emojot.emovyz.workflow;

import android.support.annotation.NonNull;
import android.util.Log;

import com.emojot.emovyz.EmovyzConfiguration;
import com.emojot.emovyz.network.APIClient;
import com.emojot.emovyz.network.ApiEndpoint;
import com.emojot.emovyz.network.TokenRefreshCallback;
import com.emojot.emovyz.network.TokenRefreshHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmovyzWorkflow {
    private static final String TAG = EmovyzWorkflow.class.getSimpleName();
    ApiEndpoint mApiEndpoint;
    private EmovyzConfiguration mEmovyzConfiguration;
    TokenRefreshHandler mTokenRefreshHandler;
    private static final int CREATE_INTERACTION = 1;
    private static final int CONTEXTUAL_SURVEY = 2;
    private String mWorkflowId;
    HashMap<Integer, Integer> networkRequestMap = new HashMap<>();

    public EmovyzWorkflow(@NonNull String workflowId) {
        mApiEndpoint = APIClient.getClient().create(ApiEndpoint.class);
        mEmovyzConfiguration = EmovyzConfiguration.getInstance();
        mTokenRefreshHandler = new TokenRefreshHandler();
        mWorkflowId = workflowId;
    }

    public void createInteraction(@NonNull final JsonObject interactionObject, final WorkflowInteractionCallback workflowInteractionCallback) {

        if (getRequestCount(CREATE_INTERACTION) <= 3) {
                if (mEmovyzConfiguration.getToken() != null) {
                    interactionObject.addProperty("interactionWorkflowID", mWorkflowId);
                    mApiEndpoint.postInteractionData(mEmovyzConfiguration.getToken(), interactionObject).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                            if (response.isSuccessful()) {
                                resetRequestCount(CREATE_INTERACTION);
                                String interactionId = response.body().getAsJsonObject().get("interactionID").getAsString();
                                workflowInteractionCallback.onSuccess(response.body().getAsJsonObject());

                            } else if (response.code() == 401) {

                                mTokenRefreshHandler.getAPIToken(CREATE_INTERACTION, new TokenRefreshCallback() {
                                    @Override
                                    public void onSuccess(String token, int requestCode) {
                                        increaseRequestCount(CREATE_INTERACTION);
                                        createInteraction(interactionObject, workflowInteractionCallback);
                                    }

                                    @Override
                                    public void onError() {
                                        resetRequestCount(CREATE_INTERACTION);
                                        workflowInteractionCallback.onError("Error occure while getting token");
                                    }
                                });
                            } else {
                                resetRequestCount(CREATE_INTERACTION);
                                workflowInteractionCallback.onError(getErrorBody(response));
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            t.printStackTrace();
                            resetRequestCount(CREATE_INTERACTION);
                            workflowInteractionCallback.onError("Error occur while getting data");
                        }
                    });
                } else {
                    mTokenRefreshHandler.getAPIToken(CREATE_INTERACTION, new TokenRefreshCallback() {
                        @Override
                        public void onSuccess(String token, int requestCode) {
                            increaseRequestCount(CREATE_INTERACTION);
                            createInteraction(interactionObject, workflowInteractionCallback);
                        }

                        @Override
                        public void onError() {
                            resetRequestCount(CREATE_INTERACTION);
                            workflowInteractionCallback.onError("Error occur while getting token");
                        }
                    });
                }

        } else {
            resetRequestCount(CREATE_INTERACTION);
        }
    }


    public void getContextualSurvey(@NonNull final String interactionId, final WorkflowContextSurveyCallback workflowContextSurveyCallback) {

        if (getRequestCount(CONTEXTUAL_SURVEY) < 3) {

            HashMap<String, String> contextObject = new HashMap();
            contextObject.put("interactionID", interactionId);
            contextObject.put("interactionWorkflowID", mWorkflowId);

            if (mEmovyzConfiguration.getToken() != null) {

                mApiEndpoint.getContextualSurvey(mEmovyzConfiguration.getToken(), contextObject).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        if (response.isSuccessful()) {
                            workflowContextSurveyCallback.onSuccess(response.body().getAsJsonObject());

                        } else if (response.code() == 401) {
                            mTokenRefreshHandler.getAPIToken(CONTEXTUAL_SURVEY, new TokenRefreshCallback() {
                                @Override
                                public void onSuccess(String token, int requestCode) {
                                    increaseRequestCount(CONTEXTUAL_SURVEY);
                                    getContextualSurvey(interactionId, workflowContextSurveyCallback);
                                }

                                @Override
                                public void onError() {
                                    resetRequestCount(CONTEXTUAL_SURVEY);
                                    workflowContextSurveyCallback.onError("Error occur while getting token");
                                }
                            });

                        } else {
                            resetRequestCount(CONTEXTUAL_SURVEY);
                            workflowContextSurveyCallback.onError(getErrorBody(response));

                        }

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        resetRequestCount(CONTEXTUAL_SURVEY);
                        workflowContextSurveyCallback.onError("Error occur while getting data");
                    }
                });
            } else {
                mTokenRefreshHandler.getAPIToken(CONTEXTUAL_SURVEY, new TokenRefreshCallback() {
                    @Override
                    public void onSuccess(String token, int requestCode) {
                        increaseRequestCount(CONTEXTUAL_SURVEY);
                        getContextualSurvey(interactionId, workflowContextSurveyCallback);
                    }

                    @Override
                    public void onError() {
                        resetRequestCount(CONTEXTUAL_SURVEY);
                        workflowContextSurveyCallback.onError("Error occur while getting token");
                    }
                });
            }
        } else {
            resetRequestCount(CONTEXTUAL_SURVEY);
        }
    }

    private int getRequestCount(Integer requestCode) {
        if (networkRequestMap.containsKey(requestCode)) {
            return networkRequestMap.get(requestCode);
        } else {
            networkRequestMap.put(requestCode, 0);
            return 1;
        }
    }

    private void increaseRequestCount(Integer requestCode) {
        if (networkRequestMap.containsKey(requestCode)) {
            Integer newCount = networkRequestMap.get(requestCode) + 1;
            networkRequestMap.put(requestCode, newCount);
        } else {
            networkRequestMap.put(requestCode, 1);
        }
    }

    private void resetRequestCount(Integer requestCode) {
        networkRequestMap.put(requestCode, 0);
    }

    private String getErrorBody(Response response){
        try {
            Gson gson = new Gson();
           return gson.toJson(gson.fromJson(response.errorBody().charStream(),JsonObject.class));
        }
        catch (Exception ex){
            return "Error occur while Json convert";
        }
    }

}
