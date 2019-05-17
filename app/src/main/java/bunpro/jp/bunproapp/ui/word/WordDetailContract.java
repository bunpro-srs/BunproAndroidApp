package bunpro.jp.bunproapp.ui.word;

import android.content.Context;

import java.util.List;

import bunpro.jp.bunproapp.models.ExampleSentence;
import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Review;
import bunpro.jp.bunproapp.models.SupplementalLink;

public interface WordDetailContract {
    interface View {
        Context getContext();
        void showToast(String message);
    }

    interface Presenter {
        Review getReview(GrammarPoint point);
        List<ExampleSentence> fetchExampleSentences(GrammarPoint point);
        List<SupplementalLink> fetchGrammarPointSupplementalLinks(GrammarPoint point);
        List<Review> getReviews();
        GrammarPoint getGrammarPoint(int id);
        void addToReviews(int grammarPointId);
        void removeFromReviews(int reviewId);
        void resetReviews(int reviewId);
        void stop();
    }
}
