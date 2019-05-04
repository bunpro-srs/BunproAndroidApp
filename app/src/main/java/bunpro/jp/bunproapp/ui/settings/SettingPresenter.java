package bunpro.jp.bunproapp.ui.settings;

import android.util.Log;

import com.androidnetworking.error.ANError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import bunpro.jp.bunproapp.activities.MainActivity;
import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Review;
import bunpro.jp.bunproapp.models.Status;
import bunpro.jp.bunproapp.service.ApiService;
import bunpro.jp.bunproapp.service.JsonParser;
import bunpro.jp.bunproapp.ui.status.StatusContract;
import bunpro.jp.bunproapp.ui.status.StatusModel;
import bunpro.jp.bunproapp.utils.UserData;

public class SettingPresenter implements SettingContract.Presenter {
    private SettingContract.View settingsView;

    public SettingPresenter(SettingContract.View settingsView)
    {
        this.settingsView = settingsView;
    }

    @Override
    public void logout() {
        settingsView.setLoadingProgress(true);

        ApiService apiService = new ApiService(settingsView.getContext());
        apiService.logout(new ApiService.ApiCallbackListener() {
            @Override
            public void success(JSONObject jsonObject) {
                settingsView.setLoadingProgress(false);
                UserData.getInstance(settingsView.getContext()).removeUser();
                settingsView.goToLogin();
            }

            @Override
            public void successAsJSONArray(JSONArray jsonArray) {
                settingsView.setLoadingProgress(false);
                Log.e("API Format changed", "JSONArray obtained instead of an JSONObject ! (User reviews)");
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
                    e.printStackTrace();
                }

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
                    e.printStackTrace();
                }
            }
        });

    }
}
