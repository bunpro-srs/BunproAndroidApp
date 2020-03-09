package bunpro.jp.bunproapp.interactors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import bunpro.jp.bunproapp.presentation.login.LoginActivity;
import bunpro.jp.bunproapp.utils.config.UserData;
import io.realm.Realm;

public abstract class BaseInteractor {
    protected Realm realm;
    protected Context context;

    protected BaseInteractor(Context context) {
        this.context = context;
        realm = Realm.getDefaultInstance();
    }

    public void close() {
        realm.close();
    }

    protected void emergencyLogout() {
        UserData.getInstance(context).removeUser();
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        ((Activity)context).finish();
        Toast.makeText(context, "Authentication lost. Please log in again.", Toast.LENGTH_SHORT).show();
    }
}
