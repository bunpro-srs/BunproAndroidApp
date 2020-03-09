package bunpro.jp.bunproapp.presentation.search;

import android.content.Context;

import java.util.List;

import bunpro.jp.bunproapp.models.GrammarPoint;
import io.realm.RealmResults;

public interface SearchContract {

    interface View {
        Context getContext();
        void showError(String msg);
        void updateView(List<GrammarPoint> points);
    }

    interface Presenter {
        void getAllWords(int filter);
        void stop();
        boolean checkSentenceAndLinksExistence();
        RealmResults<GrammarPoint> getGrammarPoints();
    }
}
