package bunpro.jp.bunproapp.fragments.contract;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import bunpro.jp.bunproapp.activities.MainActivity;
import bunpro.jp.bunproapp.models.GrammarPoint;

public class LevelDetailController implements LevelDetailContract.Controller {

    private Context mContext;

    public LevelDetailController(Context context) {
        mContext = context;
    }

    @Override
    public void getGrammarPoints(int position, LevelDetailContract.View v) {
        List<GrammarPoint> grammarPoints = new ArrayList<>();
        List<List<GrammarPoint>> aPoints = ((MainActivity)mContext).getArrangedGrammarPoints();
        if (aPoints.size() > 0) {
            grammarPoints = aPoints.get(position);
        }
        v.updateGrammarPoints(grammarPoints);
    }
}
