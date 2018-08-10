package bunpro.jp.bunproapp.fragments.contract;

import java.util.List;

import bunpro.jp.bunproapp.models.GrammarPoint;

public interface LevelDetailContract {

    interface View {
        void updateGrammarPoints(List<GrammarPoint> pointList);
    }

    interface Controller {
        void getGrammarPoints(int position, LevelDetailContract.View v);
    }
}
