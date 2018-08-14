package bunpro.jp.bunproapp.fragments.contract;

import java.util.List;

import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Lesson;
import bunpro.jp.bunproapp.models.Review;
import bunpro.jp.bunproapp.models.Status;

public interface StatusContract {
    interface View {
        void showError(String msg);
        void loadingProgress(boolean stats);
        void updateView(List<Status> status);
        void updateUserName(String name);
        void updateReviewStatus(List<Review> reviews);
        void updateLessons(List<Lesson> lessons);
        void updateGrammarPoints(List<GrammarPoint> grammarPoints);
    }

    interface Controller {
        void getStatus(StatusContract.View v);
        void setName(StatusContract.View v);
        void getReviews(StatusContract.View v);
        void getGrammarPoints(StatusContract.View v);
    }
}
