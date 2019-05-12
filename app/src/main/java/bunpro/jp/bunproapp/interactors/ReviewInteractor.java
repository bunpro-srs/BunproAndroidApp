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
import io.realm.Realm;
import io.realm.RealmQuery;

public class ReviewInteractor {
    private Realm realm;
    private Context context;

    public ReviewInteractor(Context context) {
        this.context = context;
        realm = Realm.getDefaultInstance();
    }

    public void close() {
        realm.close();
    }

    private void saveReviews(List<Review> reviews) {
        realm.beginTransaction();
        realm.insertOrUpdate(reviews);
        realm.commitTransaction();
    }

    public RealmQuery<Review> loadReviews() {
        return realm.where(Review.class);
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
                Log.d("Error", anError.getErrorDetail());
                callback.error(anError.getErrorDetail());
            }
        });
    }
}
