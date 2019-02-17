package bunpro.jp.bunproapp.service;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bunpro.jp.bunproapp.utils.Constants;
import bunpro.jp.bunproapp.utils.UserData;

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

        AndroidNetworking.post(Constants.BASE_URL_v3 + "login")
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
        AndroidNetworking.delete(Constants.BASE_URL_v3 + "logout")
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
        AndroidNetworking.get(Constants.BASE_URL_v3 + "/user")
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
        AndroidNetworking.get(Constants.BASE_URL_v3 + "user/progress")
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

        AndroidNetworking.get(Constants.BASE_URL_v3 + "reviews/all_reviews")
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
        AndroidNetworking.get(Constants.BASE_URL_v4 + "lessons")
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
        AndroidNetworking.get(Constants.BASE_URL_v4 + "grammar_points")
                .setPriority(Priority.HIGH)
                .addHeaders("Authorization", "Bearer " + token)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            listener.successAsJSONArray(response.getJSONArray("data"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        listener.error(anError);
                    }
                });
    }


    public void getExampleSentences(final CallbackListener listener) {
        String token = UserData.getInstance(mContext).getUserKey();
        AndroidNetworking.get(Constants.BASE_URL_v4 + "example_sentences")
                .setPriority(Priority.HIGH)
                .addHeaders("Authorization", "Bearer " + token)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            listener.successAsJSONArray(response.getJSONArray("data"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        listener.error(anError);
                    }
                });
    }


    public void getSupplimentalLinks(final CallbackListener listener) {
        String token = UserData.getInstance(mContext).getUserKey();
        AndroidNetworking.get(Constants.BASE_URL_v4 + "supplemental_links")
                .setPriority(Priority.HIGH)
                .addHeaders("Authorization", "Bearer " + token)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            listener.successAsJSONArray(response.getJSONArray("data"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        listener.error(anError);
                    }
                });
    }


    public void userEdit(String hideEnglish, String furigana, String lightMode, String bunnyMode, final CallbackListener listener) {

        String token = UserData.getInstance(mContext).getUserKey();
        String url = Constants.BASE_URL_v3 + "user/edit?" + "user[furigana]=\"" + furigana + "\"&user[hide_english]=\"" + hideEnglish + "\"&user[light_mode]=\"" + lightMode + "\"&user[bunny_mode]=\"" + bunnyMode + "\"";
        AndroidNetworking.post(url)
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

}
