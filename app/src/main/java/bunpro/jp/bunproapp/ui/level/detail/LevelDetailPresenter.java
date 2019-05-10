package bunpro.jp.bunproapp.ui.level.detail;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import bunpro.jp.bunproapp.models.GrammarPoint;

public class LevelDetailPresenter implements LevelDetailContract.Presenter {
    LevelDetailContract.View levelDetailView;

    public LevelDetailPresenter(LevelDetailContract.View levelDetailView) {
        this.levelDetailView = levelDetailView;
    }

    @Override
    public void pickGrammarPoint(int position) {
        List<GrammarPoint> grammarPoints = new ArrayList<>();
        List<List<GrammarPoint>> aPoints = GrammarPoint.getArrangedGrammarPointList();
        if (aPoints.size() > 0) {
            grammarPoints = aPoints.get(position);
        }
        this.levelDetailView.updateGrammarPoints(grammarPoints);
    }
}
