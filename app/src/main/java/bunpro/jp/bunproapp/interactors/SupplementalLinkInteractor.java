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
import io.realm.RealmQuery;

public class SupplementalLinkInteractor extends BaseInteractor {
    public SupplementalLinkInteractor(Context context) {
        super(context);
    }

    private void saveSupplementalLinks(List<SupplementalLink> links) {
        if (!realm.isClosed()) {
            realm.beginTransaction();
            realm.where(SupplementalLink.class).findAll().deleteAllFromRealm();
            realm.insertOrUpdate(links);
            realm.commitTransaction();
        } else {
            Log.e("REALM_CLOSED", "Cannot update supplemental links because realm is closed");
        }
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
                Log.e("Error", anError.getErrorBody());
                if (anError.getErrorBody().toLowerCase().contains("access denied")) {
                    emergencyLogout();
                }
                callback.error(anError.getErrorBody());
            }
        });
    }
}
