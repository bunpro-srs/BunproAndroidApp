package bunpro.jp.bunproapp.presentation.status;

import android.content.Context;

import java.util.List;

import bunpro.jp.bunproapp.models.Status;

public interface StatusContract {
    interface View {
        Context getContext();
        void setGlobalLoadingProgress(boolean loading);
        void setReviewsLoadingProgress(boolean loading);
        void showError(String msg);
        void updateUsername(String username);
        void updateReviewTime(String dateUpdated);
        void updateReviewNumbers(int currentReviews, int hourReviews, int dayReviews);
        void updateBadge(int count);
        void refresh();
    }

    interface Presenter {
        List<Status> getStatus();
        void fetchStatus();
        void updateReviews();
        void updateGrammarPoints();
        void updateReviewsInfo();
        boolean checkGrammarPointsAndReviewsExistence();
        void stop();
    }
}
