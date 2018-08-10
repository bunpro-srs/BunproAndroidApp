package bunpro.jp.bunproapp.fragments.contract;

import android.content.Context;

import com.androidnetworking.error.ANError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bunpro.jp.bunproapp.activities.MainActivity;
import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Review;
import bunpro.jp.bunproapp.service.ApiService;
import bunpro.jp.bunproapp.service.JsonParser;

public class SearchController implements SearchContract.Controller {

    private Context mContext;
    List<GrammarPoint> grammarPoints;

    public SearchController(Context context) {
        mContext = context;
        grammarPoints = ((MainActivity) mContext).getGrammarPoints();
    }


    @Override
    public void getAllWords(final SearchContract.View v, final int filter) {

        if (grammarPoints.size() == 0) {
            ApiService apiService = new ApiService(mContext);
            apiService.getGrammarPoints(new ApiService.CallbackListener() {
                @Override
                public void success(JSONObject jsonObject) {

                }

                @Override
                public void successAsJSONArray(JSONArray jsonArray) {
                    grammarPoints = JsonParser.getInstance(mContext).parseGrammarPoints(jsonArray);
                    v.updateView(filtering(filter));
                }

                @Override
                public void error(ANError anError) {
                    v.showError(anError.getErrorDetail());
                }
            });

        } else {
            v.updateView(filtering(filter));
        }
    }

    private List<GrammarPoint> filtering(int filter) {

        List<GrammarPoint> points = new ArrayList<>();

        List<Review> reviews = ((MainActivity)mContext).getReviews();

        Collections.sort(grammarPoints, GrammarPoint.levelComparator);

        if (filter == 0) {
            points = grammarPoints;
        } else if (filter == 1) {

            if (reviews.size() > 0) {

                for (GrammarPoint point : grammarPoints) {
                    for (Review review : reviews) {
                        if (review.grammar_point_id == point.id) {

                            points.add(point);
                            break;

                        }
                    }
                }

            }

        } else {

            if (reviews.size() > 0) {

                points = grammarPoints;
                for (int k=0;k<points.size();k++) {
                    for (Review review : reviews) {
                        if (review.grammar_point_id == points.get(k).id) {
                            points.remove(k);
                            break;
                        }
                    }
                }

            } else {
                points = grammarPoints;
            }
        }

        return points;
    }
}
