package cn.gov.kaifeng.ftz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowInsetsController;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {

    private static final String HOME_URL = "https://zmt.ftz.kaifeng.gov.cn/";

    private WebView webView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;
    private LinearLayout errorLayout;
    private FrameLayout loadingOverlay;
    private TextView urlDisplayText;
    private ImageButton btnBack;
    private ImageButton btnForward;
    private ImageButton btnRefresh;
    private ImageButton btnHome;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController ctrl = getWindow().getInsetsController();
            if (ctrl != null) ctrl.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            );
        }

        initViews();
        setupWebView();
        setupSwipeRefresh();
        setupBottomBar();

        if (isNetworkAvailable()) {
            webView.loadUrl(HOME_URL);
        } else {
            showError();
        }
    }

    private void initViews() {
        webView        = findViewById(R.id.webview);
        progressBar    = findViewById(R.id.progress_bar);
        swipeRefresh   = findViewById(R.id.swipe_refresh);
        errorLayout    = findViewById(R.id.error_layout);
        loadingOverlay = findViewById(R.id.loading_overlay);
        urlDisplayText = findViewById(R.id.url_display);
        btnBack        = findViewById(R.id.btn_back);
        btnForward     = findViewById(R.id.btn_forward);
        btnRefresh     = findViewById(R.id.btn_refresh);
        btnHome        = findViewById(R.id.btn_home);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setAllowFileAccess(false);
        settings.setGeolocationEnabled(true);
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(true);
        settings.setTextZoom(100);

        // 启用 Cookie
        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);

        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
                errorLayout.setVisibility(View.GONE);
                updateNavButtons();
                if (url != null) urlDisplayText.setText(simplifyUrl(url));
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                updateNavButtons();
                if (url != null) urlDisplayText.setText(simplifyUrl(url));
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                if (failingUrl != null && failingUrl.equals(HOME_URL)) {
                    showError();
                }
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    new Handler(Looper.getMainLooper()).postDelayed(
                        () -> progressBar.setVisibility(View.GONE), 300
                    );
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                // 可扩展：更新标题显示
            }
        });
    }

    private void setupSwipeRefresh() {
        swipeRefresh.setColorSchemeColors(
            getResources().getColor(R.color.primary_color, getTheme())
        );
        swipeRefresh.setOnRefreshListener(() -> {
            if (isNetworkAvailable()) {
                webView.reload();
                errorLayout.setVisibility(View.GONE);
            } else {
                swipeRefresh.setRefreshing(false);
                showError();
            }
        });
    }

    private void setupBottomBar() {
        btnBack.setOnClickListener(v -> {
            if (webView.canGoBack()) webView.goBack();
        });

        btnForward.setOnClickListener(v -> {
            if (webView.canGoForward()) webView.goForward();
        });

        btnRefresh.setOnClickListener(v -> {
            if (isNetworkAvailable()) {
                webView.reload();
            } else {
                showError();
            }
        });

        btnHome.setOnClickListener(v -> {
            webView.loadUrl(HOME_URL);
        });

        // 重试按钮
        View retryBtn = findViewById(R.id.btn_retry);
        if (retryBtn != null) {
            retryBtn.setOnClickListener(v -> {
                if (isNetworkAvailable()) {
                    errorLayout.setVisibility(View.GONE);
                    webView.loadUrl(HOME_URL);
                } else {
                    showError();
                }
            });
        }
    }

    private void updateNavButtons() {
        btnBack.setAlpha(webView.canGoBack() ? 1f : 0.35f);
        btnForward.setAlpha(webView.canGoForward() ? 1f : 0.35f);
    }

    private void showError() {
        errorLayout.setVisibility(View.VISIBLE);
        webView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
            getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        NetworkCapabilities caps = cm.getNetworkCapabilities(cm.getActiveNetwork());
        return caps != null && (
            caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        );
    }

    private String simplifyUrl(String url) {
        if (url == null) return "";
        return url.replace("https://", "").replace("http://", "");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.stopLoading();
            webView.destroy();
        }
        super.onDestroy();
    }
}
