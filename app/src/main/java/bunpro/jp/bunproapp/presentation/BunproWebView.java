package bunpro.jp.bunproapp.presentation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.HashMap;
import java.util.Map;

import bunpro.jp.bunproapp.R;
import bunpro.jp.bunproapp.utils.config.Constants;

public class BunproWebView extends AppCompatActivity {
    private Toolbar toolbar;
    private WebView webView;
    private ProgressBar progressBar;

    private boolean backWarning;


    public static void instantiate(Context context, String url) {
        instantiate(context, url, "");
    }
    public static void instantiate(Context context, String url, String token) {
        Intent intent = new Intent(context, BunproWebView.class);
        intent.putExtra("url", url);
        intent.putExtra("token", token);
        intent.putExtra("backWarning", !token.isEmpty());
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String token = intent.getStringExtra("token");
        backWarning = intent.getBooleanExtra("backWarning", false);

        Map<String, String> extraHeaders = new HashMap<>();
        extraHeaders.put("Authorization", "Bearer " + token);

        toolbar = findViewById(R.id.webview_toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_blue_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryGoingBack();
            }
        });

        this.progressBar = findViewById(R.id.webview_progressbar);
        this.webView = findViewById(R.id.webview);

        if (webView != null) {
            WebSettings webSettings = webView.getSettings();
            webSettings.setDomStorageEnabled(true);
            webSettings.setJavaScriptEnabled(true);

            webView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url){
                    String urlHost = Uri.parse(url).getHost();
                    if (urlHost != null && urlHost.endsWith(Constants.BASE_DOMAIN) && url.startsWith("https://")) {
                        view.loadUrl(url, extraHeaders);
                        return false;
                    } else {
                        view.getContext().startActivity(
                                new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        return true;
                    }
                }
                public void onPageFinished(WebView view, String url) {
                    if (progressBar.getVisibility() == View.VISIBLE) {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            });

            webView.loadUrl(url, extraHeaders);
        }
    }

    @Override
    public void onBackPressed() {
        tryGoingBack();
    }

    private void tryGoingBack() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            if (backWarning) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme));
                builder.setTitle(R.string.on_back_button_title);
                builder.setMessage(R.string.on_back_button_message);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            } else {
                finish();
            }
        }
    }
}
