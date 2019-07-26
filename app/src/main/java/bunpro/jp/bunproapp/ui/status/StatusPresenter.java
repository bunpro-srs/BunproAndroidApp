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

import bunpro.jp.bunproapp.interactors.GrammarPointInteractor;
import bunpro.jp.bunproapp.interactors.ReviewInteractor;
import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Review;
import bunpro.jp.bunproapp.models.Status;
import bunpro.jp.bunproapp.service.ApiService;
import bunpro.jp.bunproapp.utils.SimpleCallbackListener;

public class StatusPresenter implements StatusContract.Presenter {
    private StatusContract.View statusView;
    private ReviewInteractor reviewInteractor;
    private GrammarPointInteractor grammarPointInteractor;

    public StatusPresenter(StatusContract.View statusView)
    {
        this.statusView = statusView;
        reviewInteractor = new ReviewInteractor(statusView.getContext());
        grammarPointInteractor = new GrammarPointInteractor(statusView.getContext());
    }

    public void stop() {
        reviewInteractor.close();
        grammarPointInteractor.close();
    }

    public List<Status> getStatus() {
        return Status.getStatusList();
    }

    public void fetchStatus() {
//        ApiService apiService = new ApiService(statusView.getContext());

        statusView.setGlobalLoadingProgress(true);
        List<Status> status = new ArrayList<>();
        status.add(new Status("N5", GrammarPoint.getN5GrammarPointsLearned().size(), GrammarPoint.getN5GrammarPointsTotal().size()));
        status.add(new Status("N4", GrammarPoint.getN4GrammarPointsLearned().size(), GrammarPoint.getN4GrammarPointsTotal().size()));
        status.add(new Status("N3", GrammarPoint.getN3GrammarPointsLearned().size(), GrammarPoint.getN3GrammarPointsTotal().size()));
        status.add(new Status("N2", GrammarPoint.getN2GrammarPointsLearned().size(), GrammarPoint.getN2GrammarPointsTotal().size()));
        status.add(new Status("N1", GrammarPoint.getN1GrammarPointsLearned().size(), GrammarPoint.getN1GrammarPointsTotal().size()));
        Status.setStatusList(status);
        statusView.refresh();
        statusView.setGlobalLoadingProgress(false);
        // TODO: Reenable getProgress call when the endpoint works again
//        apiService.getProgress(new ApiService.ApiCallbackListener() {
//            @Override
//            public void success(JSONObject jsonObject) {
//                statusView.setGlobalLoadingProgress(true);
//
//                Iterator iterator = jsonObject.keys();
//                List<Status> status = new ArrayList<>();
//
//                while(iterator.hasNext()){
//
//                    String key = (String)iterator.next();
//                    int sk =0, tk = 0;
//
//                    try {
//                        JSONArray jsonArray = jsonObject.getJSONArray(key);
//                        sk = jsonArray.getInt(0);
//                        tk = jsonArray.getInt(1);
//                    } catch (JSONException e) {
//                        Log.e("JSONException", "Progress status response could not be parsed.");
//                    }
//
//                    Status s = new Status(key, sk, tk);
//                    // Dirty fix condition for missing N1/wrong N2 values
//                    if (!s.getName().equals("N2") && !s.getName().equals("N1")) {
//                        status.add(s);
//                    }
//                }
//
//                // Dirty fix status for missing N1/wrong N2 values due to inconsistent /user/progress v3 endpoint
//                status.add(new Status("N2", GrammarPoint.getN2GrammarPointsLearned().size(), GrammarPoint.getN2GrammarPointsTotal().size()));
//                status.add(new Status("N1", GrammarPoint.getN1GrammarPointsLearned().size(), GrammarPoint.getN1GrammarPointsTotal().size()));
//                Status.setStatusList(status);
//                statusView.refresh();
//                statusView.setGlobalLoadingProgress(false);
//            }
//
//            @Override
//            public void successAsJSONArray(JSONArray jsonArray) {
//                Log.e("API Format changed", "JSONArray obtained instead of an JSONObject ! (User progress)");
//                statusView.setGlobalLoadingProgress(false);
//            }
//
//            @Override
//            public void error(ANError anError) {
//                Log.e("API request error", anError.getErrorBody());
//                statusView.setGlobalLoadingProgress(false);
//            }
//        });
    }

    @Override
    public void updateReviews() {
        List<Review> rs = reviewInteractor.loadReviews().findAll();
        if (rs.size() != 0) {
            updateReviewsInfo();
        } else {
            reviewInteractor.fetchReviews(new SimpleCallbackListener() {
                @Override
                public void success() {
                    updateReviewsInfo();
                    fetchStatus();
                }
                @Override
                public void error(String errorMessage) {
                    statusView.showError(errorMessage);
                }
            });
        }
    }

    @Override
    public void updateGrammarPoints() {
        final List<GrammarPoint> points = grammarPointInteractor.loadGrammarPoints().findAll();
        if (points.size() == 0) {
            grammarPointInteractor.fetchGrammarPoints(new SimpleCallbackListener() {
                @Override
                public void success() {
                }
                @Override
                public void error(String errorMessage) {
                    statusView.showError(errorMessage);
                }
            });
        }
    }

    @Override
    public void updateReviewsInfo() {
        // Updating last review time
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat spf= new SimpleDateFormat("hh:mm aaa");
        String dateStr = spf.format(currentTime);
        statusView.updateReviewTime(dateStr);
        int pendingReviewCount = 0, withinAnHourReviewCount = 0, withinADayReviewCount = 0;
        for (Review review : reviewInteractor.loadReviews().findAll()) {
            if (review.complete) {
                long remainingHours = review.getRemainingHoursBeforeReview();
                if (remainingHours <= 0) {
                    pendingReviewCount++;
                } else if (remainingHours == 1) {
                    withinAnHourReviewCount++;
                } else if (remainingHours <= 24) {
                    withinADayReviewCount++;
                }
            }
        }
        statusView.updateReviewNumbers(pendingReviewCount, withinAnHourReviewCount, withinADayReviewCount);
        // Updating badge
        statusView.updateBadge(pendingReviewCount);
    }

    public boolean checkGrammarPointsAndReviewsExistence() {
        return !grammarPointInteractor.loadGrammarPoints().findAll().isEmpty() && !reviewInteractor.loadReviews().findAll().isEmpty();
    }
}
