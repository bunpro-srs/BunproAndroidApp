package bunpro.jp.bunproapp.presentation.word;

import android.util.Log;

import com.androidnetworking.error.ANError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bunpro.jp.bunproapp.interactors.ExampleSentenceInteractor;
import bunpro.jp.bunproapp.interactors.GrammarPointInteractor;
import bunpro.jp.bunproapp.interactors.ReviewInteractor;
import bunpro.jp.bunproapp.interactors.SupplementalLinkInteractor;
import bunpro.jp.bunproapp.service.ApiService;
import bunpro.jp.bunproapp.presentation.home.HomeActivity;
import bunpro.jp.bunproapp.models.ExampleSentence;
import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Review;
import bunpro.jp.bunproapp.models.SupplementalLink;
import bunpro.jp.bunproapp.utils.SimpleCallbackListener;

public class WordDetailPresenter implements WordDetailContract.Presenter {
    private WordDetailContract.View wordDetailView;
    private SupplementalLinkInteractor supplementalLinkInteractor;
    private ExampleSentenceInteractor exampleSentenceInteractor;
    private ReviewInteractor reviewInteractor;
    private GrammarPointInteractor grammarPointInteractor;

    public WordDetailPresenter(WordDetailContract.View wordDetailView)
    {
        this.wordDetailView = wordDetailView;
        supplementalLinkInteractor = new SupplementalLinkInteractor(this.wordDetailView.getContext());
        exampleSentenceInteractor = new ExampleSentenceInteractor(this.wordDetailView.getContext());
        reviewInteractor = new ReviewInteractor(this.wordDetailView.getContext());
        grammarPointInteractor = new GrammarPointInteractor(this.wordDetailView.getContext());
    }

    public void stop() {
        supplementalLinkInteractor.close();
        exampleSentenceInteractor.close();
        reviewInteractor.close();
        grammarPointInteractor.close();
    }

    @Override
    public void updateReviewByGrammarPoint(int grammarPointId) {
        Review review = null;
        List<Review> reviews = getReviews();
        if (reviews.size() > 0) {
            for (Review r : reviews) {
                if (r.grammar_point_id == grammarPointId) {
                    review = r;
                    break;
                }
            }
        }
        wordDetailView.updateReview(review);
    }

    private void refetchReviews(int grammarPointId, int reviewId, SimpleCallbackListener callback) {
        reviewInteractor.fetchReviews(new SimpleCallbackListener() {
            @Override
            public void success() {
                if (reviewId != -1) {
                    updateReviewById(reviewId);
                } else if (grammarPointId != -1) {
                    updateReviewByGrammarPoint(grammarPointId);
                }
                if (wordDetailView.getContext() instanceof HomeActivity && ((HomeActivity)wordDetailView.getContext()).homePresenter != null) {
                    ((HomeActivity)wordDetailView.getContext()).homePresenter.countProgress(reviewInteractor.loadReviews().findAll());
                }
                callback.success();
            }

            @Override
            public void error(String errorMessage) {
                Log.e("REFETCH_REVIEWS", errorMessage);
                callback.error(errorMessage);
            }
        });
    }

    public void updateReviewById(int reviewId) {
        Review review = reviewInteractor.getReview(reviewId).findFirst();
        wordDetailView.updateReview(review);
    }

    public List<ExampleSentence> fetchExampleSentences(GrammarPoint point) {
        List<ExampleSentence> exampleSentences = exampleSentenceInteractor.loadExampleSentences().findAll();
        List<ExampleSentence> pointExampleSentences = new ArrayList<>();
        if (exampleSentences.size() > 0) {
            for (int i=0;i<exampleSentences.size();i++) {
                if (exampleSentences.get(i).grammar_point_id == point.id) {
                    pointExampleSentences.add(exampleSentences.get(i));
                }
            }
        }
        return pointExampleSentences;
    }

    public List<SupplementalLink> fetchGrammarPointSupplementalLinks(GrammarPoint point) {
        List<SupplementalLink> supplementalLinks = supplementalLinkInteractor.loadSupplementalLinks().findAll();
        List<SupplementalLink> pointSupplementalLinks = new ArrayList<>();
        if (supplementalLinks.size() > 0) {
            for (int i=0;i<supplementalLinks.size();i++) {
                if (supplementalLinks.get(i).grammar_point_id == point.id) {
                    pointSupplementalLinks.add(supplementalLinks.get(i));
                }
            }
        }
        return pointSupplementalLinks;
    }

    public List<Review> getReviews() {
        return reviewInteractor.loadReviews().findAll();
    }

    public GrammarPoint getGrammarPoint(int id) {
        return grammarPointInteractor.loadGrammarPoint(id);
    }

    public void addToReviews(int grammarPointId) {
        ApiService apiService = new ApiService(wordDetailView.getContext());
        wordDetailView.setActionLoading(true);
        apiService.addToReview(grammarPointId, new ApiService.ApiCallbackListener() {
            @Override
            public void success(JSONObject jsonObject) {
                refetchReviews(grammarPointId, -1, new SimpleCallbackListener() {
                    @Override
                    public void success() {
                        wordDetailView.setActionLoading(false);
                        wordDetailView.showToast("The review has been added to your list.");
                    }
                    @Override
                    public void error(String errorMessage) {
                        wordDetailView.showToast("Error while re-fetching reviews after adding one.");
                    }
                });
            }
            @Override
            public void successAsJSONArray(JSONArray jsonArray) {
                refetchReviews(grammarPointId, -1, new SimpleCallbackListener() {
                    @Override
                    public void success() {
                        wordDetailView.setActionLoading(false);
                        wordDetailView.showToast("The review has been added to your list.");
                    }
                    @Override
                    public void error(String errorMessage) {
                        wordDetailView.showToast("Error while re-fetching reviews after adding one.");
                    }
                });
            }
            @Override
            public void error(ANError anError) {
                wordDetailView.setActionLoading(false);
                wordDetailView.showToast("An error occured while adding the review to your list.");
                Log.e("Error", anError.getErrorDetail());
            }
        });
    }

    public void removeFromReviews(int reviewId) {
        ApiService apiService = new ApiService(wordDetailView.getContext());
        wordDetailView.setActionLoading(true);
        apiService.removeReview(reviewId, new ApiService.ApiCallbackListener() {
            @Override
            public void success(JSONObject jsonObject) {
                refetchReviews(-1, reviewId, new SimpleCallbackListener() {
                    @Override
                    public void success() {
                        wordDetailView.setActionLoading(false);
                        wordDetailView.showToast("The review has been removed from your list.");
                    }
                    @Override
                    public void error(String errorMessage) {
                        wordDetailView.showToast("Error while re-fetching reviews after removing one.");
                    }
                });
            }
            @Override
            public void successAsJSONArray(JSONArray jsonArray) {
                refetchReviews(-1, reviewId, new SimpleCallbackListener() {
                    @Override
                    public void success() {
                        wordDetailView.setActionLoading(false);
                        wordDetailView.showToast("The review has been removed from your list.");
                    }
                    @Override
                    public void error(String errorMessage) {
                        wordDetailView.showToast("Error while re-fetching reviews after removing one.");
                    }
                });
            }
            @Override
            public void error(ANError anError) {
                wordDetailView.setActionLoading(false);
                wordDetailView.showToast("An error occured while removing the review from your list.");
                Log.e("Error", anError.getErrorDetail());
            }
        });
    }

    public void resetReviews(int reviewId) {
        ApiService apiService = new ApiService(wordDetailView.getContext());
        wordDetailView.setActionLoading(true);
        apiService.resetReview(reviewId, new ApiService.ApiCallbackListener() {
            @Override
            public void success(JSONObject jsonObject) {
                refetchReviews(-1, reviewId, new SimpleCallbackListener() {
                    @Override
                    public void success() {
                        wordDetailView.setActionLoading(false);
                        wordDetailView.showToast("The review has been reset.");
                    }
                    @Override
                    public void error(String errorMessage) {
                        wordDetailView.showToast("Error while re-fetching reviews after resetting one.");
                    }
                });
            }
            @Override
            public void successAsJSONArray(JSONArray jsonArray) {
                refetchReviews(-1, reviewId, new SimpleCallbackListener() {
                    @Override
                    public void success() {
                        wordDetailView.setActionLoading(false);
                        wordDetailView.showToast("The review has been reset.");
                    }
                    @Override
                    public void error(String errorMessage) {
                        wordDetailView.showToast("Error while re-fetching reviews after resetting one.");
                    }
                });
            }
            @Override
            public void error(ANError anError) {
                wordDetailView.setActionLoading(false);
                wordDetailView.showToast("An error occured while resetting the review in your list.");
                Log.e("Error", anError.getErrorDetail());
            }
        });
    }
}
