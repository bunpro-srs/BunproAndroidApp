package bunpro.jp.bunproapp.ui.login;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.error.ANError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bunpro.jp.bunproapp.utils.AppData;
import bunpro.jp.bunproapp.utils.Constants;
import bunpro.jp.bunproapp.utils.EspressoTestingIdlingResource;
import bunpro.jp.bunproapp.utils.SimpleCallbackListener;
import bunpro.jp.bunproapp.utils.UserData;
import bunpro.jp.bunproapp.service.ApiService;

public class LoginPresenter implements LoginContract.Presenter {

    private ApiService apiService;
    private Context mContext;

    public LoginPresenter(Context context) {
        mContext = context;
    }

    @Override
    public void login(String email, String password, final SimpleCallbackListener callback) {

        EspressoTestingIdlingResource.increment("login_and_loading");
        apiService = new ApiService(mContext);
        apiService.login(email, password, new ApiService.ApiCallbackListener() {
            @Override
            public void success(JSONObject jsonObject) {
                UserData.getInstance(mContext).setUserLogin();
                String errorMessage = "";
                if (jsonObject.has("bunpro_api_token")) {
                    try {
                        Log.d("Bunpro API token", jsonObject.getString("bunpro_api_token"));
                        UserData.getInstance(mContext).setUserKey(jsonObject.getString("bunpro_api_token"));
                        callback.success();
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        errorMessage = "Unable to parse JSON Bunpro API token !";
                    }
                } else {
                    errorMessage = "Unable to find Bunpro API token within response !";
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("errors");
                        if (jsonArray.length() > 0) {
                            JSONObject obj = jsonArray.getJSONObject(0);
                            errorMessage = obj.getString("detail");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callback.error(errorMessage);
            }

            @Override
            public void successAsJSONArray(JSONArray jsonArray) {
                Log.w("API Format changed", "JSONArray obtained instead of an JSONObject ! (Login)");
                callback.error("Login API response format changed !");
            }

            @Override
            public void error(ANError anError) {
                callback.error(anError.getErrorDetail());
            }
        });

    }

    public void configureSettings(final SimpleCallbackListener callback) {
        apiService.getUser(new ApiService.ApiCallbackListener() {
            @Override
            public void success(JSONObject jsonObject) {
                try {
                    String hideEnglish = jsonObject.getString("hide_english");
                    if (hideEnglish.equalsIgnoreCase("no")) {
                        AppData.getInstance(mContext).setHideEnglish(Constants.SETTING_HIDE_ENGLISH_NO);
                    } else {
                        AppData.getInstance(mContext).setHideEnglish(Constants.SETTING_HIDE_ENGLISH_YES);
                    }

                    String furigana = jsonObject.getString("furigana");
                    if (furigana.equalsIgnoreCase("on")) {
                        AppData.getInstance(mContext).setFurigana(Constants.SETTING_FURIGANA_ALWAYS);
                    } else if (furigana.equalsIgnoreCase("off")) {
                        AppData.getInstance(mContext).setFurigana(Constants.SETTING_FURIGANA_NEVER);
                    } else {
                        AppData.getInstance(mContext).setFurigana(Constants.SETTING_FURIGANA_WANIKANI);
                    }

                    String username = jsonObject.getString("username");
                    UserData.getInstance(mContext).setUserName(username);

                    String lightMode = jsonObject.getString("light_mode");
                    if (lightMode.equalsIgnoreCase("off")) {
                        AppData.getInstance(mContext).setLightMode(Constants.SETTING_LIGHT_MODE_OFF);
                    } else {
                        AppData.getInstance(mContext).setLightMode(Constants.SETTING_LIGHT_MODE_ON);
                    }

                    String bunnyMode = jsonObject.getString("bunny_mode");
                    if (bunnyMode.equalsIgnoreCase("off")) {
                        AppData.getInstance(mContext).setBunnyMode(Constants.SETTING_BUNNY_MODE_OFF);
                    } else {
                        AppData.getInstance(mContext).setBunnyMode(Constants.SETTING_BUNNY_MODE_ON);
                    }

                    boolean subscription = jsonObject.getBoolean("subscriber");
                    if (subscription) {
                        AppData.getInstance(mContext).setSubscription(Constants.SETTING_SUBSCRIPTION_YES);
                    } else {
                        AppData.getInstance(mContext).setSubscription(Constants.SETTING_SUBSCRIPTION_NO);
                    }
                    callback.success();
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.error("Unable to parse user settings JSON response !");
                }
            }

            @Override
            public void successAsJSONArray(JSONArray jsonArray) {
                Log.w("API Format changed", "JSONArray obtained instead of an JSONObject ! (User settings)");
                callback.error("User settings API response format changed !");
            }

            @Override
            public void error(ANError anError) {
                callback.error(anError.getErrorDetail());
            }
        });
    }
}
