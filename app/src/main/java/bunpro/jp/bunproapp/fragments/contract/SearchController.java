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
    private List<GrammarPoint> grammarPoints;

    public SearchController(Context context) {
        mContext = context;
        grammarPoints = new ArrayList<>(((MainActivity) mContext).getGrammarPoints());
    }


    @Override
    public void getAllWords(final SearchContract.View v, final int filter) {

        if (grammarPoints.size() == 0) {
            ApiService apiService = new ApiService(mContext);
            apiService.getGrammarPoints(new ApiService.ApiCallbackListener() {
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
        Collections.sort(grammarPoints, GrammarPoint.levelComparator);

        List<GrammarPoint> relevantPoints = new ArrayList<>();
        List<Review> reviewsOriginal = ((MainActivity)mContext).getReviews();
        List<Review> reviewsCopy = new ArrayList<>(reviewsOriginal);

        if (filter == 0) {
            // Filter off : all grammar points active
            relevantPoints = new ArrayList<>(grammarPoints);
        } else if (filter == 1) {
            // Filter on : unlearned grammar points only
            List<GrammarPoint> grammarPointsToRemove = getLearnedGrammarPoints(grammarPoints, reviewsCopy);
            relevantPoints = new ArrayList<>(grammarPoints);
            relevantPoints.removeAll(grammarPointsToRemove);
        } else {
            // Filter on : learned grammar points only
            relevantPoints = getLearnedGrammarPoints(grammarPoints, reviewsCopy);
        }

        return relevantPoints;
    }

    private List<GrammarPoint> getLearnedGrammarPoints(List<GrammarPoint> grammarPoints, List<Review> userReviews) {
        List<GrammarPoint> relevantPoints = new ArrayList<>();
        if (userReviews.size() > 0) {
            for (GrammarPoint grammarPoint : grammarPoints) {
                for (Review review : userReviews) {
                    if (review.grammar_point_id == grammarPoint.id) {
                        relevantPoints.add(grammarPoint);
                        break;
                    }
                }
            }
        }
        return relevantPoints;
    }
}
