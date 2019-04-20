package bunpro.jp.bunproapp.fragments.contract;

import android.content.Context;
import android.util.SparseIntArray;

import com.androidnetworking.error.ANError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import bunpro.jp.bunproapp.activities.MainActivity;
import bunpro.jp.bunproapp.models.SupplementalLink;
import bunpro.jp.bunproapp.utils.UserData;
import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Review;
import bunpro.jp.bunproapp.models.Status;
import bunpro.jp.bunproapp.service.ApiService;
import bunpro.jp.bunproapp.service.JsonParser;
import io.realm.Realm;
import io.realm.RealmResults;

public class StatusController implements StatusContract.Controller {

    private Context mContext;

    public StatusController(Context context) {
        mContext = context;
    }

    @Override
    public void getStatus(final StatusContract.View v) {

        ApiService apiService = new ApiService(mContext);

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
                    // Dirty fix condition for missing N1/wrong N2 values
                    if (!s.name.equals("N2") && !s.name.equals("N1")) {
                        status.add(s);
                    }

                }

                MainActivity activity = (MainActivity) mContext;

                // Dirty fix status for missing N1/wrong N2 values due to inconsistent /user/progress v3 endpoint
                status.add(new Status("N2", activity.n2GrammarPointsLearned.size(), activity.n2GrammarPointsTotal.size()));
                status.add(new Status("N1", activity.n1GrammarPointsLearned.size(), activity.n1GrammarPointsTotal.size()));

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
                    List<Review> reviews = JsonParser.getInstance(mContext).parseReviews(jsonObject);
                    v.updateReviewStatus(reviews);
                }

                @Override
                public void successAsJSONArray(JSONArray jsonArray) {
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

        final List<GrammarPoint> points = ((MainActivity)mContext).getGrammarPoints();
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
                    saveSupplementalLinks(grammarPoints);
                    v.updateGrammarPoints(grammarPoints);
                }

                @Override
                public void error(ANError anError) {
                    v.showError(anError.getErrorDetail());
                }
            });
        }
    }

    private void saveSupplementalLinks(List<GrammarPoint> points) {

//        if (points.size() > 0) {
//            for (int k=0;k<points.size();k++) {
//                List<SupplementalLink> links = points.get(k).supplemental_links;
//                if (links.size() > 0) {
//                    for (int i=0;i<links.size();i++) {
//                        SupplementalLink link = links.get(i);
//                        Realm realm = Realm.getDefaultInstance();
//                        RealmResults<SupplementalLink> results = realm.where(SupplementalLink.class).equalTo("id", link.id).findAll();
//                        if (results.size() == 0) {
//
//                            realm.beginTransaction();
//                            realm.copyToRealm(link);
//                            realm.commitTransaction();
//
//                        }
//                    }
//                }
//            }
//        }
    }
}
