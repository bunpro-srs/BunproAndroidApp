package bunpro.jp.bunproapp.ui.level.detail;

import android.content.Context;

import java.util.List;

import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Review;

public interface LevelDetailContract {

    interface View {
        Context getContext();
        void updateGrammarPoints(List<GrammarPoint> pointList);
    }

    interface Presenter {
        void pickGrammarPoint(int position);
        void stop();
        boolean checkSentenceAndLinksExistence();
        List<Review> getReviews();
    }
}
