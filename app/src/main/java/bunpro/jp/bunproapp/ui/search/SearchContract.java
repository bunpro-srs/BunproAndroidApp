package bunpro.jp.bunproapp.ui.search;

import android.content.Context;

import java.util.List;

import bunpro.jp.bunproapp.models.GrammarPoint;

public interface SearchContract {

    interface View {
        Context getContext();
        void showError(String msg);
        void updateView(List<GrammarPoint> points);
    }

    interface Presenter {
        void getAllWords(int filter);
    }
}
