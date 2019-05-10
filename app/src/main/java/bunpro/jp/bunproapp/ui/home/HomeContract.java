package bunpro.jp.bunproapp.ui.home;

import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.List;

import bunpro.jp.bunproapp.models.ExampleSentence;
import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Lesson;
import bunpro.jp.bunproapp.models.Review;
import bunpro.jp.bunproapp.models.Status;
import bunpro.jp.bunproapp.models.SupplementalLink;

public interface HomeContract {
    interface View {
        Context getContext();
        void replaceFragment(Fragment fragment);
        void addFragment(Fragment fragment);
        void popFragment();
    }

    interface Presenter {
        void fetchData();
    }
}
