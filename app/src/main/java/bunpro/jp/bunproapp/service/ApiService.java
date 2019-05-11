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

    public interface ApiCallbackListener {
        void success(JSONObject jsonObject);
        void successAsJSONArray(JSONArray jsonArray);
        void error(ANError anError);
    }

    private Context mContext;

    public ApiService(Context context) {
        mContext = context;
    }

    /**
     * Attempt to login to the Bunpro API
     * @param email Email of the user
     * @param password Password of the user
     * @param listener Listener to call once the login is done
     */
    public void login(String email, String password, final ApiCallbackListener listener) {
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

    /**
     * Logout of the Bunpro API
     * @param listener Listener to call once the logout is done
     */
    public void logout(final ApiCallbackListener listener) {
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

    /**
     * Fetch data about the logged user
     * @param listener Listener to call once user data has been fetched
     */
    public void getUser(final ApiCallbackListener listener) {
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

    /**
     * Fetch user progress about different JLPT levels
     * @param listener Listener to call once progress has been fetched
     */
    public void getProgress(final ApiCallbackListener listener) {
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

    /**
     * Fetch all user reviews, including those not ready to be reviewed yet
     * @param listener Listener to call once reviews have been fetched
     */
    public void getReviews(final ApiCallbackListener listener) {
        String token = UserData.getInstance(mContext).getUserKey();
        AndroidNetworking.get(Constants.BASE_URL_v3 + "reviews/all_reviews_total")
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

    /**
     * Fetch lessons
     * @param listener Listener to call once lessons have been fetched
     */
    public void getLessons(final ApiCallbackListener listener) {

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

    /**
     * Fetch all grammar points
     * @param listener Listener to call when grammar points have been fetched
     */
    public void getGrammarPoints(final ApiCallbackListener listener) {
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
                            Log.e("JSONException", "All grammar points could not be parsed.");
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        listener.error(anError);
                    }
                });
    }

    /**
     * Fetch example sentences (very time consuming)
     * @param listener Listener to call once sentences have been fetched
     */
    public void getExampleSentences(final ApiCallbackListener listener) {
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
                            Log.e("JSONException", "All example sentences could not be parsed.");
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        listener.error(anError);
                    }
                });
    }

    /**
     * Fetch the external links related to grammar points
     * @param listener Listener to call once links have been fetched
     */
    public void getSupplementalLinks(final ApiCallbackListener listener) {
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
                            Log.e("JSONException", "All supplemental links could not be parsed.");
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        listener.error(anError);
                    }
                });
    }

    /**
     * Edit user preferences
     * @param hideEnglish Hide english option
     * @param furigana Display furigana option
     * @param lightMode Enabled light mode option
     * @param bunnyMode Enabled bunny mode option
     * @param listener Listener to call once preferences have been updated
     */
    public void userEdit(String hideEnglish, String furigana, String lightMode, String bunnyMode, final ApiCallbackListener listener) {
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
