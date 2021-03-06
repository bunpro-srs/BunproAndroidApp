package bunpro.jp.bunproapp.interactors;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.error.ANError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import bunpro.jp.bunproapp.models.Review;
import bunpro.jp.bunproapp.service.ApiService;
import bunpro.jp.bunproapp.service.JsonParser;
import bunpro.jp.bunproapp.utils.SimpleCallbackListener;
import io.realm.RealmQuery;

public class ReviewInteractor extends BaseInteractor {
    public ReviewInteractor(Context context) {
        super(context);
    }

    private void saveReviews(List<Review> reviews) {
        if (!realm.isClosed()) {
            realm.beginTransaction();
            realm.where(Review.class).findAll().deleteAllFromRealm();
            realm.insertOrUpdate(reviews);
            realm.commitTransaction();
        } else {
            Log.e("REALM_CLOSED", "Cannot update reviews because realm is closed");
        }
    }

    public RealmQuery<Review> loadReviews() {
        return realm.where(Review.class);
    }

    public RealmQuery<Review> getReview(int reviewId) {
        return realm.where(Review.class).equalTo("id", reviewId);
    }

    public void fetchReviews(SimpleCallbackListener callback) {
        ApiService apiService = new ApiService(context);
        apiService.getReviews(new ApiService.ApiCallbackListener() {
            @Override
            public void success(JSONObject jsonObject) {
                List<Review> reviews = JsonParser.getInstance(context).parseReviews(jsonObject);
                saveReviews(reviews);
                callback.success();
            }
            @Override
            public void successAsJSONArray(JSONArray jsonArray) {
                Log.w("API Format changed", "JSONArray obtained instead of an JSONObject ! (Reviews)");
                callback.error("Grammar points API response format changed !");
            }
            @Override
            public void error(ANError anError) {
                Log.e("Error", anError.getErrorBody());
                if (anError.getErrorBody().toLowerCase().contains("access denied")) {
                    emergencyLogout();
                }
                callback.error(anError.getErrorBody());
            }
        });
    }
}
