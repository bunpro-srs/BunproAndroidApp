package bunpro.jp.bunproapp.interactors;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.error.ANError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import bunpro.jp.bunproapp.models.ExampleSentence;
import bunpro.jp.bunproapp.service.ApiService;
import bunpro.jp.bunproapp.service.JsonParser;
import bunpro.jp.bunproapp.utils.SimpleCallbackListener;
import io.realm.RealmQuery;

public class ExampleSentenceInteractor extends BaseInteractor {
    public ExampleSentenceInteractor(Context context) {
        super(context);
    }

    private void saveExampleSentences(List<ExampleSentence> sentences) {
        if (!realm.isClosed()) {
            realm.beginTransaction();
            realm.where(ExampleSentence.class).findAll().deleteAllFromRealm();
            realm.insertOrUpdate(sentences);
            realm.commitTransaction();
        } else {
            Log.e("REALM_CLOSED", "Cannot update example sentences because realm is closed");
        }
    }

    public RealmQuery<ExampleSentence> loadExampleSentences() {
        return realm.where(ExampleSentence.class);
    }

    public void fetchExampleSentences(SimpleCallbackListener callback) {
        ApiService apiService = new ApiService(context);
        apiService.getExampleSentences(new ApiService.ApiCallbackListener() {
            @Override
            public void success(JSONObject jsonObject) {
                Log.w("API Format changed", "JSONObject obtained instead of an JSONArray ! (Example sentences)");
                callback.error("Grammar points API response format changed !");
            }
            @Override
            public void successAsJSONArray(JSONArray jsonArray) {
                List<ExampleSentence> exampleSentences = JsonParser.getInstance(context).parseExampleSentences(jsonArray);
                saveExampleSentences(exampleSentences);
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
