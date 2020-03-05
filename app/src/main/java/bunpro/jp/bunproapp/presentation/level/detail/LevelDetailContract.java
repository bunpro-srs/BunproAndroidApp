package bunpro.jp.bunproapp.presentation.level.detail;

import android.content.Context;

import java.util.List;

import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Review;

public interface LevelDetailContract {

    interface View {
        Context getContext();
    }

    interface Presenter {
        void stop();
        boolean checkSentenceAndLinksExistence();
        List<Review> getReviews();
        List<GrammarPoint> getLessonGrammarPoints(int lessonId);
    }
}
