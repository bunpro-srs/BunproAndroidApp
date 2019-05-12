package bunpro.jp.bunproapp.ui.word;

import java.util.ArrayList;
import java.util.List;

import bunpro.jp.bunproapp.interactors.ExampleSentenceInteractor;
import bunpro.jp.bunproapp.interactors.ReviewInteractor;
import bunpro.jp.bunproapp.interactors.SupplementalLinkInteractor;
import bunpro.jp.bunproapp.ui.home.HomeActivity;
import bunpro.jp.bunproapp.models.ExampleSentence;
import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Review;
import bunpro.jp.bunproapp.models.SupplementalLink;

public class WordDetailPresenter implements WordDetailContract.Presenter {
    private WordDetailContract.View wordDetailView;
    private SupplementalLinkInteractor supplementalLinkInteractor;
    private ExampleSentenceInteractor exampleSentenceInteractor;
    private ReviewInteractor reviewInteractor;

    public WordDetailPresenter(WordDetailContract.View wordDetailView)
    {
        this.wordDetailView = wordDetailView;
        supplementalLinkInteractor = new SupplementalLinkInteractor(this.wordDetailView.getContext());
        exampleSentenceInteractor = new ExampleSentenceInteractor(this.wordDetailView.getContext());
        reviewInteractor = new ReviewInteractor(this.wordDetailView.getContext());
    }

    public void stop() {
        supplementalLinkInteractor.close();
        exampleSentenceInteractor.close();
        reviewInteractor.close();
    }

    @Override
    public Review getReview(GrammarPoint point) {
        Review review = null;
        List<Review> reviews = getReviews();
        if (reviews.size() > 0) {
            for (Review r : reviews) {
                if (r.grammar_point_id == point.id) {
                    review = r;
                    break;
                }
            }
        }
        return review;
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
}
