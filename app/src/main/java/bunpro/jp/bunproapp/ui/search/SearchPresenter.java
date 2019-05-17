package bunpro.jp.bunproapp.ui.search;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.error.ANError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bunpro.jp.bunproapp.interactors.ExampleSentenceInteractor;
import bunpro.jp.bunproapp.interactors.GrammarPointInteractor;
import bunpro.jp.bunproapp.interactors.ReviewInteractor;
import bunpro.jp.bunproapp.interactors.SupplementalLinkInteractor;
import bunpro.jp.bunproapp.models.ExampleSentence;
import bunpro.jp.bunproapp.ui.home.HomeActivity;
import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Review;
import bunpro.jp.bunproapp.service.ApiService;
import bunpro.jp.bunproapp.service.JsonParser;

public class SearchPresenter implements SearchContract.Presenter {
    private SearchContract.View searchView;
    private SupplementalLinkInteractor supplementalLinkInteractor;
    private ExampleSentenceInteractor exampleSentenceInteractor;
    private ReviewInteractor reviewInteractor;
    private GrammarPointInteractor grammarPointInteractor;
    private List<GrammarPoint> searchGrammarPoints;

    public SearchPresenter(SearchContract.View searchView) {
        this.searchView = searchView;
        supplementalLinkInteractor = new SupplementalLinkInteractor(this.searchView.getContext());
        exampleSentenceInteractor = new ExampleSentenceInteractor(this.searchView.getContext());
        reviewInteractor = new ReviewInteractor(this.searchView.getContext());
        grammarPointInteractor = new GrammarPointInteractor(this.searchView.getContext());
        searchGrammarPoints = new ArrayList<>();
    }

    public void stop() {
        supplementalLinkInteractor.close();
        exampleSentenceInteractor.close();
        reviewInteractor.close();
        grammarPointInteractor.close();
    }

    @Override
    public void getAllWords(final int filter) {
        List<GrammarPoint> grammarPoints = grammarPointInteractor.loadGrammarPoints().findAll();
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
                    searchView.showError(anError.getErrorBody());
                }
            });

        } else {
            searchGrammarPoints = grammarPointInteractor.loadGrammarPoints().findAll();
            searchView.updateView(filtering(filter));
        }
    }

    private List<GrammarPoint> filtering(int filter) {
        List<GrammarPoint> sortedGramamrPoints = new ArrayList<>(searchGrammarPoints);
        Collections.sort(sortedGramamrPoints, GrammarPoint.levelComparator);

        List<GrammarPoint> relevantPoints = new ArrayList<>();
        List<Review> reviewsOriginal = reviewInteractor.loadReviews().findAll();
        List<Review> reviewsCopy = new ArrayList<>(reviewsOriginal);

        if (filter == 0) {
            // Filter off : all grammar points active
            relevantPoints = new ArrayList<>(sortedGramamrPoints);
        } else if (filter == 1) {
            // Filter on : unlearned grammar points only
            List<GrammarPoint> grammarPointsToRemove = getLearnedGrammarPoints(sortedGramamrPoints, reviewsCopy);
            relevantPoints = new ArrayList<>(sortedGramamrPoints);
            relevantPoints.removeAll(grammarPointsToRemove);
        } else {
            // Filter on : learned grammar points only
            relevantPoints = getLearnedGrammarPoints(sortedGramamrPoints, reviewsCopy);
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

    public boolean checkSentenceAndLinksExistence() {
        return !exampleSentenceInteractor.loadExampleSentences().findAll().isEmpty() && !supplementalLinkInteractor.loadSupplementalLinks().findAll().isEmpty();
    }

    public List<GrammarPoint> getGrammarPoints() {
        return grammarPointInteractor.loadGrammarPoints().findAll();
    }
}
