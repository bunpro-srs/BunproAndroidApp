package bunpro.jp.bunprosrs.fragments.contract;

import java.util.List;

import bunpro.jp.bunprosrs.models.Lesson;
import bunpro.jp.bunprosrs.models.Review;
import bunpro.jp.bunprosrs.models.Status;

public interface StatusContract {
    interface View {
        void showError(String msg);
        void loadingProgress(boolean stats);
        void updateView(List<Status> status);
        void updateUserName(String name);
        void updateReviewStatus(List<Review> reviews);
        void updateLessons(List<Lesson> lessons);
    }

    interface Controller {
        void getStatus(StatusContract.View v);
        void setName(StatusContract.View v);
        void getReviews(StatusContract.View v);
        void getLessons(StatusContract.View v);
    }
}
