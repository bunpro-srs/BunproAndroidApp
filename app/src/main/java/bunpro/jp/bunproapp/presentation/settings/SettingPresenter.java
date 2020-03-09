package bunpro.jp.bunproapp.presentation.settings;

import android.util.Log;

import com.androidnetworking.error.ANError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bunpro.jp.bunproapp.service.ApiService;
import bunpro.jp.bunproapp.utils.test.EspressoTestingIdlingResource;
import bunpro.jp.bunproapp.utils.config.UserData;

public class SettingPresenter implements SettingContract.Presenter {
    private SettingContract.View settingsView;

    public SettingPresenter(SettingContract.View settingsView)
    {
        this.settingsView = settingsView;
    }

    @Override
    public void logout() {
        EspressoTestingIdlingResource.increment("logout");
        settingsView.setLoadingProgress(true);

        ApiService apiService = new ApiService(settingsView.getContext());
        apiService.logout(new ApiService.ApiCallbackListener() {
            @Override
            public void success(JSONObject jsonObject) {
                settingsView.setLoadingProgress(false);
                UserData.getInstance(settingsView.getContext()).removeUser();
                settingsView.goToLogin();
                EspressoTestingIdlingResource.decrement("logout");
            }

            @Override
            public void successAsJSONArray(JSONArray jsonArray) {
                settingsView.setLoadingProgress(false);
                Log.e("API Format changed", "JSONArray obtained instead of an JSONObject ! (User reviews)");
                EspressoTestingIdlingResource.decrement("logout");
            }

            @Override
            public void error(ANError anError) {
                settingsView.setLoadingProgress(false);

                String errorBody = anError.getErrorBody();
                try {
                    JSONObject jsonObject = new JSONObject(errorBody);
                    if (jsonObject.has("errors")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("errors");
                        JSONObject obj = jsonArray.getJSONObject(0);
                        settingsView.showError(obj.getString("detail"));

                    } else {
                        settingsView.showError(errorBody);
                    }
                } catch (JSONException e) {
                    Log.e("JSONException", "Logout error response could not be parsed.");
                }
                EspressoTestingIdlingResource.decrement("logout");
            }
        });
    }

    @Override
    public void submitSettings(String hideEnglish, String furigana, String lightMode, String bunnyMode) {
        settingsView.setLoadingProgress(true);
        ApiService apiService = new ApiService(settingsView.getContext());
        apiService.userEdit(hideEnglish, furigana, lightMode, bunnyMode, new ApiService.ApiCallbackListener() {
            @Override
            public void success(JSONObject jsonObject) {
                settingsView.setLoadingProgress(false);
                if (jsonObject == null) {
                    settingsView.notifyUpdate();
                }
            }

            @Override
            public void successAsJSONArray(JSONArray jsonArray) {
                Log.w("API Format changed", "JSONArray obtained instead of an JSONObject ! (Setting update)");
            }

            @Override
            public void error(ANError anError) {
                settingsView.setLoadingProgress(false);
                String errorBody = anError.getErrorBody();
                try {
                    JSONObject jsonObject = new JSONObject(errorBody);
                    if (jsonObject.has("errors")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("errors");
                        JSONObject obj = jsonArray.getJSONObject(0);
                        settingsView.showError(obj.getString("detail"));

                    } else {
                        settingsView.showError(errorBody);
                    }
                } catch (JSONException e) {
                    Log.e("JSONException", "Setting update error response could not be parsed.");
                }
            }
        });

    }
}
