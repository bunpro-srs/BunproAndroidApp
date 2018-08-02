package bunpro.jp.bunprosrs.fragments.contract;

import android.content.Context;

import com.androidnetworking.error.ANError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import bunpro.jp.bunprosrs.activities.MainActivity;
import bunpro.jp.bunprosrs.models.GrammarPoint;
import bunpro.jp.bunprosrs.models.Lesson;
import bunpro.jp.bunprosrs.models.Review;
import bunpro.jp.bunprosrs.models.Status;
import bunpro.jp.bunprosrs.service.ApiService;
import bunpro.jp.bunprosrs.service.JsonParser;
import bunpro.jp.bunprosrs.utils.UserData;

public class StatusController implements StatusContract.Controller {

    private Context mContext;

    public StatusController(Context context) {
        mContext = context;
    }


    @Override
    public void getStatus(final StatusContract.View v) {

        ApiService apiService = new ApiService(mContext);
        //v.loadingProgress(true);

        apiService.getProgress(new ApiService.CallbackListener() {
            @Override
            public void success(JSONObject jsonObject) {

                v.loadingProgress(false);

                Iterator iterator = jsonObject.keys();
                List<Status> status = new ArrayList<>();

                while(iterator.hasNext()){

                    String key = (String)iterator.next();
                    int sk =0, tk = 0;

                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray(key);
                        sk = jsonArray.getInt(0);
                        tk = jsonArray.getInt(1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Status s = new Status(key, sk, tk);
                    status.add(s);

                }

                MainActivity activity = (MainActivity) mContext;
                activity.setjlptLevel(status);

                v.updateView(status);

            }

            @Override
            public void successAsJSONArray(JSONArray jsonArray) {

            }

            @Override
            public void error(ANError anError) {
                v.loadingProgress(false);
            }
        });
    }

    @Override
    public void setName(final StatusContract.View v) {

        String name = UserData.getInstance(mContext).getUserName();
        v.updateUserName(name);
    }

    @Override
    public void getReviews(final StatusContract.View v) {

        List<Review> rs = ((MainActivity)mContext).getReviews();
        if (rs.size() != 0) {
            v.updateReviewStatus(rs);
        } else {
            ApiService apiService = new ApiService(mContext);
            apiService.getReviews(new ApiService.CallbackListener() {
                @Override
                public void success(JSONObject jsonObject) {

                }

                @Override
                public void successAsJSONArray(JSONArray jsonArray) {
                    List<Review> reviews = JsonParser.getInstance(mContext).parseReviews(jsonArray);
                    v.updateReviewStatus(reviews);
                }

                @Override
                public void error(ANError anError) {

                    v.showError(anError.getErrorDetail());
                }
            });
        }
    }

    @Override
    public void getLessons(final StatusContract.View v) {

        List<Lesson> lessons = ((MainActivity)mContext).getLessons();
        if (lessons.size() != 0) {
            v.updateLessons(lessons);
        } else {
            ApiService apiService = new ApiService(mContext);
            apiService.getLessons(new ApiService.CallbackListener() {
                @Override
                public void success(JSONObject jsonObject) {

                }

                @Override
                public void successAsJSONArray(JSONArray jsonArray) {
                    List<Lesson> lessons = JsonParser.getInstance(mContext).parseLessons(jsonArray);
                    v.updateLessons(lessons);
                }

                @Override
                public void error(ANError anError) {
                    v.showError(anError.getErrorDetail());
                }
            });
        }

    }

    @Override
    public void getGrammarPoints(final StatusContract.View v) {

        List<GrammarPoint> points = ((MainActivity)mContext).getGrammarPoints();
        if (points.size() != 0) {
            v.updateGrammarPoints(points);
        } else {
            ApiService apiService = new ApiService(mContext);
            apiService.getGrammarPoints(new ApiService.CallbackListener() {
                @Override
                public void success(JSONObject jsonObject) {

                }

                @Override
                public void successAsJSONArray(JSONArray jsonArray) {
                    List<GrammarPoint> grammarPoints = JsonParser.getInstance(mContext).parseGrammarPoints(jsonArray);
                    v.updateGrammarPoints(grammarPoints);
                }

                @Override
                public void error(ANError anError) {
                    v.showError(anError.getErrorDetail());
                }
            });
        }
    }
}
