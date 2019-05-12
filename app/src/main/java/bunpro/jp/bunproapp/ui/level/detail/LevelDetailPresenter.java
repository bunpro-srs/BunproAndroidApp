package bunpro.jp.bunproapp.ui.level.detail;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import bunpro.jp.bunproapp.interactors.ExampleSentenceInteractor;
import bunpro.jp.bunproapp.interactors.ReviewInteractor;
import bunpro.jp.bunproapp.interactors.SupplementalLinkInteractor;
import bunpro.jp.bunproapp.models.ExampleSentence;
import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Review;

public class LevelDetailPresenter implements LevelDetailContract.Presenter {
    LevelDetailContract.View levelDetailView;
    private SupplementalLinkInteractor supplementalLinkInteractor;
    private ExampleSentenceInteractor exampleSentenceInteractor;
    private ReviewInteractor reviewInteractor;

    public LevelDetailPresenter(LevelDetailContract.View levelDetailView) {
        this.levelDetailView = levelDetailView;
        supplementalLinkInteractor = new SupplementalLinkInteractor(this.levelDetailView.getContext());
        exampleSentenceInteractor = new ExampleSentenceInteractor(this.levelDetailView.getContext());
        reviewInteractor = new ReviewInteractor(this.levelDetailView.getContext());
    }

    public void stop() {
        supplementalLinkInteractor.close();
        exampleSentenceInteractor.close();
        reviewInteractor.close();
    }

    @Override
    public void pickGrammarPoint(int position) {
        List<GrammarPoint> grammarPoints = new ArrayList<>();
        List<List<GrammarPoint>> aPoints = GrammarPoint.getArrangedGrammarPointList();
        if (aPoints.size() > 0) {
            grammarPoints = aPoints.get(position);
        }
        this.levelDetailView.updateGrammarPoints(grammarPoints);
    }

    public boolean checkSentenceAndLinksExistence() {
        return !exampleSentenceInteractor.loadExampleSentences().findAll().isEmpty() && !supplementalLinkInteractor.loadSupplementalLinks().findAll().isEmpty();
    }

    public List<Review> getReviews() {
        return reviewInteractor.loadReviews().findAll();
    }
}
