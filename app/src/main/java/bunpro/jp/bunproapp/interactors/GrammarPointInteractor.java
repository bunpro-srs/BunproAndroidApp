package bunpro.jp.bunproapp.interactors;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.error.ANError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.service.ApiService;
import bunpro.jp.bunproapp.service.JsonParser;
import bunpro.jp.bunproapp.utils.SimpleCallbackListener;
import io.realm.RealmQuery;

public class GrammarPointInteractor extends BaseInteractor {
    public GrammarPointInteractor(Context context) {
        super(context);
    }

    private void saveGrammarPoints(List<GrammarPoint> grammarPoints) {
        if (!realm.isClosed()) {
            realm.beginTransaction();
            realm.insertOrUpdate(grammarPoints);
            realm.commitTransaction();
        }
    }

    public RealmQuery<GrammarPoint> loadGrammarPoints() {
        return realm.where(GrammarPoint.class);
    }

    public GrammarPoint loadGrammarPoint(int id) {
        return realm.where(GrammarPoint.class).equalTo("id", id).findFirst();
    }

    public RealmQuery<GrammarPoint> loadLevelGrammarPoints(String level) {
        return realm.where(GrammarPoint.class).equalTo("level", level);
    }

    public RealmQuery<GrammarPoint> loadLessonGrammarPoints(int lesson) {
        return realm.where(GrammarPoint.class).equalTo("lesson_id", lesson);
    }

    public void fetchGrammarPoints(SimpleCallbackListener callback) {
        ApiService apiService = new ApiService(context);
        apiService.getGrammarPoints(new ApiService.ApiCallbackListener() {
            @Override
            public void success(JSONObject jsonObject) {
                Log.w("API Format changed", "JSONObject obtained instead of an JSONArray ! (Grammar points)");
                callback.error("Grammar points API response format changed !");
            }
            @Override
            public void successAsJSONArray(JSONArray jsonArray) {
                List<GrammarPoint> grammarPoints = JsonParser.getInstance(context).parseGrammarPoints(jsonArray);
                saveGrammarPoints(grammarPoints);
                callback.success();
            }
            @Override
            public void error(ANError anError) {
                Log.e("Error", anError.getErrorBody());
                if (anError.getErrorBody().toLowerCase().contains("access denied")) {
                    emergencyLogout();
                }
                callback.error(anError.getErrorBody());
            }
        });
    }
}
