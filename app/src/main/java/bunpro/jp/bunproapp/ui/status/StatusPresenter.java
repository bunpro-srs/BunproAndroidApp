package bunpro.jp.bunproapp.ui.status;

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

public class StatusPresenter implements StatusContract.Presenter {
    private StatusContract.View statusView;

    public StatusPresenter(StatusContract.View statusView)
    {
        this.statusView = statusView;
    }

    public List<Status> getStatus() {
        return Status.getStatusList();
    }

    public void fetchStatus() {
        // TODO: This should not use context
        ApiService apiService = new ApiService(statusView.getContext());

        apiService.getProgress(new ApiService.ApiCallbackListener() {
            @Override
            public void success(JSONObject jsonObject) {
                statusView.setLoadingProgress(true);

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
                    // Dirty fix condition for missing N1/wrong N2 values
                    if (!s.getName().equals("N2") && !s.getName().equals("N1")) {
                        status.add(s);
                    }
                }

                // TODO: Do not access context within presenter
                MainActivity activity = (MainActivity) statusView.getContext();
                // Dirty fix status for missing N1/wrong N2 values due to inconsistent /user/progress v3 endpoint
                status.add(new Status("N2", activity.n2GrammarPointsLearned.size(), activity.n2GrammarPointsTotal.size()));
                status.add(new Status("N1", activity.n1GrammarPointsLearned.size(), activity.n1GrammarPointsTotal.size()));
                Status.setStatusList(status);
                statusView.refresh();
            }

            @Override
            public void successAsJSONArray(JSONArray jsonArray) {
                Log.e("API Format changed", "JSONArray obtained instead of an JSONObject ! (User progress)");
                statusView.setLoadingProgress(false);
            }

            @Override
            public void error(ANError anError) {
                Log.e("API request error", anError.getErrorDetail());
                statusView.setLoadingProgress(false);
            }
        });
    }

    @Override
    public void fetchReviews() {
        List<Review> rs = ((MainActivity)statusView.getContext()).getReviews();
        if (rs.size() != 0) {
            updateReviewsInfo();
        } else {
            ApiService apiService = new ApiService(statusView.getContext());
            apiService.getReviews(new ApiService.ApiCallbackListener() {
                @Override
                public void success(JSONObject jsonObject) {
                    Review.setReviewList(JsonParser.getInstance(statusView.getContext()).parseReviews(jsonObject));
                    updateReviewsInfo();
                    fetchStatus();
                }

                @Override
                public void successAsJSONArray(JSONArray jsonArray) {
                    Log.e("API Format changed", "JSONArray obtained instead of an JSONObject ! (User reviews)");
                }

                @Override
                public void error(ANError anError) {
                    statusView.showError(anError.getErrorDetail());
                }
            });
        }
    }

    @Override
    public void fetchGrammarPoints() {

        final List<GrammarPoint> points = GrammarPoint.getGrammarPointList();
        if (points.size() == 0) {
            ApiService apiService = new ApiService(statusView.getContext());
            apiService.getGrammarPoints(new ApiService.ApiCallbackListener() {
                @Override
                public void success(JSONObject jsonObject) {
                    Log.e("API Format changed", "JSONObject obtained instead of an JSONArray ! (Grammar points)");
                }

                @Override
                public void successAsJSONArray(JSONArray jsonArray) {
                    GrammarPoint.setGrammarPointList(JsonParser.getInstance(statusView.getContext()).parseGrammarPoints(jsonArray));
                }

                @Override
                public void error(ANError anError) {
                    statusView.showError(anError.getErrorDetail());
                }
            });
        }
    }

    @Override
    public void updateReviewsInfo() {
        // Updating reviews
        statusView.updateReviews(Review.getReviewList());
        // Updating last review time
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat spf= new SimpleDateFormat("hh:mm aaa");
        String dateStr = spf.format(currentTime);
        statusView.updateReviewTime(dateStr);
        // Fetching review count
        if (Review.getReviewList().isEmpty()) {
            Review.setReviewList(((MainActivity)statusView.getContext()).getReviews());
        }
        int pendingReviewCount = 0, withinAnHourReviewCount = 0, withinADayReviewCount = 0;
        for (Review review : Review.getReviewList()) {
            long remainingHours = review.getRemainingHoursBeforeReview();
            if (remainingHours <= 0) {
                pendingReviewCount++;
            } else if (remainingHours == 1) {
                withinAnHourReviewCount++;
            } else if (remainingHours <= 24) {
                withinADayReviewCount++;
            }
        }
        statusView.updateReviewNumbers(pendingReviewCount, withinAnHourReviewCount, withinADayReviewCount);
        // Updating badge
        statusView.updateBadge(Review.getReviewList().size());
    }
}
