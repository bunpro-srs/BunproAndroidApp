package bunpro.jp.bunprosrs.service;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import bunpro.jp.bunprosrs.utils.Constants;
import bunpro.jp.bunprosrs.utils.UserData;

public class ApiService {

    public interface CallbackListener {
        void success(JSONObject jsonObject);
        void successAsJSONArray(JSONArray jsonArray);
        void error(ANError anError);
    }

    private Context mContext;

    public ApiService(Context context) {

        mContext = context;

    }

    public void login(String email, String password, final CallbackListener listener) {

        AndroidNetworking.post(Constants.BASE_URL + "login")
                .setContentType("application/x-www-form-urlencoded")
                .setPriority(Priority.HIGH)
                .addBodyParameter("user_login[email]", email)
                .addBodyParameter("user_login[password]", password)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        listener.success(response);

                    }

                    @Override
                    public void onError(ANError anError) {
                        listener.error(anError);
                    }
                });

    }

    public void logout(final CallbackListener listener) {

        String token = UserData.getInstance(mContext).getUserKey();
        AndroidNetworking.delete(Constants.BASE_URL + "logout")
                .setContentType("application/x-www-form-urlencoded")
                .setPriority(Priority.HIGH)
                .addHeaders("Authorization", "Bearer " + token)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        listener.success(null);
                    }

                    @Override
                    public void onError(ANError anError) {

                        listener.error(anError);
                    }
                });

    }

    public void getUser(final CallbackListener listener) {
        String token = UserData.getInstance(mContext).getUserKey();
        AndroidNetworking.get(Constants.BASE_URL + "/user")
                .setPriority(Priority.HIGH)
                .addHeaders("Authorization", "Bearer " + token)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.success(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        listener.error(anError);
                    }
                });
    }

    public void getProgress(final CallbackListener listener) {

        String token = UserData.getInstance(mContext).getUserKey();
        AndroidNetworking.get(Constants.BASE_URL + "user/progress")
                .setPriority(Priority.HIGH)
                .addHeaders("Authorization", "Bearer " + token)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.success(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        listener.error(anError);
                    }
                });

    }


    public void getReviews(final CallbackListener listener) {

        String token = UserData.getInstance(mContext).getUserKey();

        AndroidNetworking.get(Constants.BASE_URL + "reviews/all_reviews")
                .setPriority(Priority.HIGH)
                .addHeaders("Authorization", "Bearer " + token)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        listener.successAsJSONArray(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        listener.error(anError);
                    }
                });
    }

    public void getLessons(final CallbackListener listener) {

        String token = UserData.getInstance(mContext).getUserKey();
        AndroidNetworking.get(Constants.BASE_URL + "lessons")
                .setPriority(Priority.HIGH)
                .addHeaders("Authorization", "Bearer " + token)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        listener.successAsJSONArray(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        listener.error(anError);
                    }
                });
    }

    public void getGrammarPoints(final CallbackListener listener) {

        String token = UserData.getInstance(mContext).getUserKey();
        AndroidNetworking.get(Constants.BASE_URL + "grammar_points")
                .setPriority(Priority.HIGH)
                .addHeaders("Authorization", "Bearer " + token)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        listener.successAsJSONArray(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        listener.error(anError);
                    }
                });

    }

}
