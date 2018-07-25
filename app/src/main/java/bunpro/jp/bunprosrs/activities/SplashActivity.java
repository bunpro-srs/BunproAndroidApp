package bunpro.jp.bunprosrs.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import bunpro.jp.bunprosrs.utils.UserData;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean loginStats = UserData.getInstance(this).getUserLogin();

        if (!loginStats) {

            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        } else {
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
        }

        finish();

    }
}
