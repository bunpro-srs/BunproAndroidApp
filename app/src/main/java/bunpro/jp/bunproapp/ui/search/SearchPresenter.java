package bunpro.jp.bunproapp.ui.search;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.error.ANError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bunpro.jp.bunproapp.ui.home.HomeActivity;
import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Review;
import bunpro.jp.bunproapp.service.ApiService;
import bunpro.jp.bunproapp.service.JsonParser;

public class SearchPresenter implements SearchContract.Presenter {
    private SearchContract.View searchView;
    private List<GrammarPoint> searchGrammarPoints;

    public SearchPresenter(SearchContract.View searchView) {
        this.searchView = searchView;
        searchGrammarPoints = new ArrayList<>();
    }


    @Override
    public void getAllWords(final int filter) {
        List<GrammarPoint> grammarPoints = GrammarPoint.getGrammarPointList();
        if (grammarPoints.size() == 0) {
            ApiService apiService = new ApiService(searchView.getContext());
            apiService.getGrammarPoints(new ApiService.ApiCallbackListener() {
                @Override
                public void success(JSONObject jsonObject) {
                    Log.e("API Format changed", "JSONObject obtained instead of an JSONArray ! (Search grammar points)");
                }

                @Override
                public void successAsJSONArray(JSONArray jsonArray) {
                    searchGrammarPoints = JsonParser.getInstance(searchView.getContext()).parseGrammarPoints(jsonArray);
                    searchView.updateView(filtering(filter));
                }

                @Override
                public void error(ANError anError) {
                    searchView.showError(anError.getErrorDetail());
                }
            });

        } else {
            searchGrammarPoints = GrammarPoint.getGrammarPointList();
            searchView.updateView(filtering(filter));
        }
    }

    private List<GrammarPoint> filtering(int filter) {
        Collections.sort(searchGrammarPoints, GrammarPoint.levelComparator);

        List<GrammarPoint> relevantPoints = new ArrayList<>();
        List<Review> reviewsOriginal = Review.getReviewList();
        List<Review> reviewsCopy = new ArrayList<>(reviewsOriginal);

        if (filter == 0) {
            // Filter off : all grammar points active
            relevantPoints = new ArrayList<>(searchGrammarPoints);
        } else if (filter == 1) {
            // Filter on : unlearned grammar points only
            List<GrammarPoint> grammarPointsToRemove = getLearnedGrammarPoints(searchGrammarPoints, reviewsCopy);
            relevantPoints = new ArrayList<>(searchGrammarPoints);
            relevantPoints.removeAll(grammarPointsToRemove);
        } else {
            // Filter on : learned grammar points only
            relevantPoints = getLearnedGrammarPoints(searchGrammarPoints, reviewsCopy);
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
