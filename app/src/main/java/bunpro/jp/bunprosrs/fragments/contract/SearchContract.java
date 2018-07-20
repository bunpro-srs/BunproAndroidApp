package bunpro.jp.bunprosrs.fragments.contract;

import java.util.List;

import bunpro.jp.bunprosrs.models.GrammarPoint;

public interface SearchContract {

    interface View {
        void showError(String msg);
        void updateView(List<GrammarPoint> points);
    }

    interface Controller {
        void getAllWords(SearchContract.View v, int filter);
    }
}
