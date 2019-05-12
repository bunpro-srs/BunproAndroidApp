package bunpro.jp.bunproapp.interactors;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.error.ANError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import bunpro.jp.bunproapp.models.SupplementalLink;
import bunpro.jp.bunproapp.service.ApiService;
import bunpro.jp.bunproapp.service.JsonParser;
import bunpro.jp.bunproapp.utils.SimpleCallbackListener;
import io.realm.Realm;
import io.realm.RealmQuery;

public class SupplementalLinkInteractor {
    private Realm realm;
    private Context context;

    public SupplementalLinkInteractor(Context context) {
        this.context = context;
        realm = Realm.getDefaultInstance();
    }

    public void close() {
        realm.close();
    }

    private void saveSupplementalLinks(List<SupplementalLink> links) {
        realm.beginTransaction();
        realm.insertOrUpdate(links);
        realm.commitTransaction();
    }

    public RealmQuery<SupplementalLink> loadSupplementalLinks() {
        return realm.where(SupplementalLink.class);
    }

    public void fetchSupplementalLinks(SimpleCallbackListener callback) {
        ApiService apiService = new ApiService(context);
        apiService.getSupplementalLinks(new ApiService.ApiCallbackListener() {
            @Override
            public void success(JSONObject jsonObject) {
                Log.w("API Format changed", "JSONObject obtained instead of an JSONArray ! (Supplemental Links)");
                callback.error("Grammar points API response format changed !");
            }

            @Override
            public void successAsJSONArray(JSONArray jsonArray) {
                List<SupplementalLink> supplementalLinks = JsonParser.getInstance(context).parseSupplimentalLinks(jsonArray);
                saveSupplementalLinks(supplementalLinks);
                callback.success();
            }

            @Override
            public void error(ANError anError) {
                Log.d("Error", anError.getErrorDetail());
                callback.error(anError.getErrorDetail());
            }
        });
    }
}
