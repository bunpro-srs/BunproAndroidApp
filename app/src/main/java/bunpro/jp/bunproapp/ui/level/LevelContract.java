package bunpro.jp.bunproapp.ui.level;

import android.content.Context;

import java.util.List;
import java.util.Map;

import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Review;

public interface LevelContract {

    interface View {
        Context getContext();
        void updateLessons();
    }

    interface Presenter {
        List<Review> getReviews();
        List<GrammarPoint> getLevelGrammarPoints(String level);
        Map<Integer, List<GrammarPoint>> getLevelGrammarPointsByLessons(String level);
        void stop();
    }
}
