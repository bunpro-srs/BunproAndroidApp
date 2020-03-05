package bunpro.jp.bunproapp.presentation.home;

import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.List;

import bunpro.jp.bunproapp.models.Review;

public interface HomeContract {
    interface View {
        Context getContext();
        void replaceFragment(Fragment fragment);
        void addFragment(Fragment fragment);
        void popFragment();
        void showToast(String message);
    }

    interface Presenter {
        void fetchData();
        void stop();
        void countProgress(List<Review> reviews);
    }
}
