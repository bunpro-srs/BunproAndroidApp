package bunpro.jp.bunproapp.presentation.login;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;

import bunpro.jp.bunproapp.R;
import bunpro.jp.bunproapp.presentation.BunproWebView;
import bunpro.jp.bunproapp.presentation.home.HomeActivity;
import bunpro.jp.bunproapp.utils.config.Constants;
import bunpro.jp.bunproapp.utils.test.EspressoTestingIdlingResource;
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

    LoginContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        loadingProgress(false);

        mPresenter = new LoginPresenter(this);

    }

    @OnClick(R.id.btnPrivacy) void privacy() {
        BunproWebView.instantiate(this, Constants.PRIVACY_URL);
    }

    @OnClick(R.id.btnLogin) void login() {

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        LoginContract.View loginView = this;

        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(email)) {
            loadingProgress(true);
            // Attempt to login
            mPresenter.login(email, password, new SimpleCallbackListener() {
                @Override
                public void success() {
                    // Attempt to fetch user settings and configure the local ones
                    mPresenter.configureSettings(new SimpleCallbackListener() {
                        @Override
                        public void success() {
                            loadingProgress(false);
                            gotoMain();
                        }

                        @Override
                        public void error(String errorMessage) {
                            loadingProgress(false);
                            EspressoTestingIdlingResource.decrement("login_and_loading");
                            showError(errorMessage);
                        }
                    });
                }

                @Override
                public void error(String errorMessage) {
                    loadingProgress(false);
                    EspressoTestingIdlingResource.decrement("login_and_loading");
                    showError(errorMessage);
                }
            });
        } else {
            showError("Empty email or password");
        }
    }

    @Override
    public void loadingProgress(boolean loading) {
        if (progressBar != null) {
            if (loading) {
                progressBar.setVisibility(View.VISIBLE);
                btnLogin.setTextColor(getResources().getColor(R.color.colorAlmostWhite));
                btnLogin.setEnabled(false);
            } else {
                progressBar.setVisibility(View.GONE);
                btnLogin.setTextColor(getResources().getColor(R.color.default_header_color));
                btnLogin.setEnabled(true);
            }
        }
    }

    @Override
    public void showError(String txt) {
        Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void gotoMain() {

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
