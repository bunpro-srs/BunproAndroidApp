package bunpro.jp.bunprosrs.fragments.contract;

import android.content.Context;

import com.androidnetworking.error.ANError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bunpro.jp.bunprosrs.activities.MainActivity;
import bunpro.jp.bunprosrs.models.GrammarPoint;
import bunpro.jp.bunprosrs.models.Status;
import bunpro.jp.bunprosrs.service.ApiService;
import bunpro.jp.bunprosrs.service.JsonParser;

public class SearchController implements SearchContract.Controller {

    private Context mContext;
    List<GrammarPoint> grammarPoints;

    public SearchController(Context context) {
        mContext = context;
        grammarPoints = ((MainActivity) mContext).getGrammarPoints();
    }


    @Override
    public void getAllWords(final SearchContract.View v, final int filter) {

        if (grammarPoints.size() == 0) {
            ApiService apiService = new ApiService(mContext);
            apiService.getGrammarPoints(new ApiService.CallbackListener() {
                @Override
                public void success(JSONObject jsonObject) {

                }

                @Override
                public void successAsJSONArray(JSONArray jsonArray) {
                    grammarPoints = JsonParser.getInstance(mContext).parseGrammarPoints(jsonArray);
                    v.updateView(filtering(filter));
                }

                @Override
                public void error(ANError anError) {
                    v.showError(anError.getErrorDetail());
                }
            });

        } else {
            v.updateView(filtering(filter));
        }
    }

    private List<GrammarPoint> filtering(int filter) {
        List<GrammarPoint> points = new ArrayList<>();

        Collections.sort(grammarPoints, GrammarPoint.levelComparator);

        if (filter == 0) {
            points = grammarPoints;
        } else if (filter == 1) {

        } else {

        }

        return points;
    }
}
