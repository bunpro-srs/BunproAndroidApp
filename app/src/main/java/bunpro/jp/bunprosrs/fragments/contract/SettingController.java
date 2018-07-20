package bunpro.jp.bunprosrs.fragments.contract;

import android.content.Context;

import com.androidnetworking.error.ANError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bunpro.jp.bunprosrs.service.ApiService;
import bunpro.jp.bunprosrs.utils.UserData;

public class SettingController implements SettingContract.Controller {

    private Context mContext;

    public SettingController(Context context) {
        mContext = context;
    }


    @Override
    public void logout(final SettingContract.View v) {

        v.loadingProgress(true);

        ApiService apiService = new ApiService(mContext);
        apiService.logout(new ApiService.CallbackListener() {
            @Override
            public void success(JSONObject jsonObject) {

                v.loadingProgress(false);
                UserData.getInstance(mContext).removeUser();
                v.gotoLogin();
            }

            @Override
            public void successAsJSONArray(JSONArray jsonArray) {

            }

            @Override
            public void error(ANError anError) {

                v.loadingProgress(false);

                String errorBody = anError.getErrorBody();
                try {
                    JSONObject jsonObject = new JSONObject(errorBody);
                    if (jsonObject.has("errors")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("errors");
                        JSONObject obj = jsonArray.getJSONObject(0);
                        v.showError(obj.getString("detail"));

                    } else {
                        v.showError(errorBody);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
