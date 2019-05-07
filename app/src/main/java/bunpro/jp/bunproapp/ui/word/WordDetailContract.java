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
    }

    interface Presenter {
        Review getReview(GrammarPoint point);
        List<ExampleSentence> getExampleSentences(GrammarPoint point);
        List<SupplementalLink> getSupplementalLinks(GrammarPoint point);
    }
}
