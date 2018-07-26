package bunpro.jp.bunprosrs.fragments.contract;

import java.util.List;

import bunpro.jp.bunprosrs.models.GrammarPoint;

public interface LevelDetailContract {

    interface View {
        void updateGrammarPoints(List<GrammarPoint> pointList);
    }

    interface Controller {
        void getGrammarPoints(int position, LevelDetailContract.View v);
    }
}
