package bunpro.jp.bunproapp.ui.home;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import bunpro.jp.bunproapp.ui.login.LoginActivity;
import bunpro.jp.bunproapp.utils.UserData;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean loginStats = UserData.getInstance(this).getUserLogin();

        if (!loginStats) {

            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        } else {
            Intent mainIntent = new Intent(this, HomeActivity.class);
            startActivity(mainIntent);
        }

        finish();

    }
}
