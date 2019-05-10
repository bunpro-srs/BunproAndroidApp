package bunpro.jp.bunproapp.ui.level.detail;

import java.util.List;

import bunpro.jp.bunproapp.models.GrammarPoint;

public interface LevelDetailContract {

    interface View {
        void updateGrammarPoints(List<GrammarPoint> pointList);
    }

    interface Presenter {
        void pickGrammarPoint(int position);
    }
}
