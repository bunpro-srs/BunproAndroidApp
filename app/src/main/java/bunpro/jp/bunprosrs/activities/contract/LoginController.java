package bunpro.jp.bunprosrs.activities.contract;

import android.content.Context;

import com.androidnetworking.error.ANError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bunpro.jp.bunprosrs.service.ApiService;
import bunpro.jp.bunprosrs.utils.AppData;
import bunpro.jp.bunprosrs.utils.Constants;
import bunpro.jp.bunprosrs.utils.UserData;

public class LoginController implements LoginContract.Controller {

    private Context mContext;
    public LoginController(Context context) {
        mContext = context;
    }


    @Override
    public void login(final LoginContract.View v, String email, String password) {

        v.loadingProgress(true);

        final ApiService apiService = new ApiService(mContext);
        apiService.login(email, password, new ApiService.CallbackListener() {
            @Override
            public void success(JSONObject jsonObject) {

                UserData.getInstance(mContext).setUserLogin();
                if (jsonObject.has("bunpro_api_token")) {

                    try {
                        UserData.getInstance(mContext).setUserKey(jsonObject.getString("bunpro_api_token"));
                        apiService.getUser(new ApiService.CallbackListener() {
                            @Override
                            public void success(JSONObject jsonObject) {

                                v.loadingProgress(false);
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


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                v.gotoMain();
                            }

                            @Override
                            public void successAsJSONArray(JSONArray jsonArray) {

                            }

                            @Override
                            public void error(ANError anError) {

                                v.loadingProgress(false);
                                v.showError(anError.getErrorDetail());
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("errors");
                        if (jsonArray.length() > 0) {
                            JSONObject obj = jsonArray.getJSONObject(0);
                            v.showError(obj.getString("detail"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void successAsJSONArray(JSONArray jsonArray) {

            }

            @Override
            public void error(ANError anError) {

                v.loadingProgress(false);
                v.showError(anError.getErrorDetail());
            }
        });

    }
}
