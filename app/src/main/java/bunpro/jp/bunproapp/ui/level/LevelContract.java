package bunpro.jp.bunproapp.ui.level;

import android.content.Context;

import java.util.List;
import java.util.Map;

import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Review;

public interface LevelContract {

    interface View {
        Context getContext();
        void updateLessons(Map<String, List<GrammarPoint>> pointsByLesson);
    }

    interface Presenter {
        void sortGrammarPoints(String levelStr);
        void arrangeGrammarPoints(Map<String, List<GrammarPoint>> pointsByLesson);
        List<Review> getReviews();
        void stop();
    }
}
