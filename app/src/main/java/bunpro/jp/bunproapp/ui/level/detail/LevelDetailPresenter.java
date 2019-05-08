package bunpro.jp.bunproapp.ui.level.detail;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import bunpro.jp.bunproapp.activities.MainActivity;
import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.ui.level.detail.LevelDetailContract;

public class LevelDetailPresenter implements LevelDetailContract.Controller {

    private Context mContext;

    public LevelDetailPresenter(Context context) {
        mContext = context;
    }

    @Override
    public void getGrammarPoints(int position, LevelDetailContract.View v) {
        List<GrammarPoint> grammarPoints = new ArrayList<>();
        List<List<GrammarPoint>> aPoints = GrammarPoint.getArrangedGrammarPointList();
        if (aPoints.size() > 0) {
            grammarPoints = aPoints.get(position);
        }
        v.updateGrammarPoints(grammarPoints);
    }
}
