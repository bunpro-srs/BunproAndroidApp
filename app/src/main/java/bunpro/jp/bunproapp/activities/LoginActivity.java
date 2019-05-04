package bunpro.jp.bunproapp.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.wuadam.awesomewebview.AwesomeWebView;

import bunpro.jp.bunproapp.R;
import bunpro.jp.bunproapp.activities.contract.LoginContract;
import bunpro.jp.bunproapp.utils.Constants;
import bunpro.jp.bunproapp.utils.EspressoTestingIdlingResource;
import bunpro.jp.bunproapp.utils.SimpleCallbackListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    @BindView(R.id.btnLogin) Button btnLogin;
    @BindView(R.id.btnPrivacy) Button btnPrivacy;

    @BindView(R.id.etEmail) EditText etEmail;
    @BindView(R.id.etPassword) EditText etPassword;

    @BindView(R.id.spin_kit) SpinKitView progressBar;

    LoginContract.Controller mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        loadingProgress(false);

        mController = new LoginController(this);

    }

    @OnClick(R.id.btnPrivacy) void privacy() {
        new AwesomeWebView.Builder(this).showUrl(false).show(Constants.PRIVACY_URL);
    }

    @OnClick(R.id.btnLogin) void login() {

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        LoginContract.View loginView = this;

        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(email)) {
            // Attempt to login
            mController.login(email, password, new SimpleCallbackListener() {
                @Override
                public void success() {
                    // Attempt to fetch user settings and configure the local ones
                    mController.configureSettings(new SimpleCallbackListener() {
                        @Override
                        public void success() {
                            gotoMain();
                        }

                        @Override
                        public void error(String errorMessage) {
                            EspressoTestingIdlingResource.decrement("login_and_loading");
                            showError(errorMessage);
                        }
                    });
                }

                @Override
                public void error(String errorMessage) {
                    EspressoTestingIdlingResource.decrement("login_and_loading");
                    showError(errorMessage);
                }
            });
        }
    }


    @Override
    public void loadingProgress(boolean stats) {
        if (progressBar != null) {
            if (stats) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showError(String txt) {

        Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void gotoMain() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
