# ProGuard rules for KaifengFTZ WebView App

# Keep WebView related classes
-keepclassmembers class * extends android.webkit.WebViewClient {
    public *;
}
-keepclassmembers class * extends android.webkit.WebChromeClient {
    public *;
}

# Keep JavaScript interface
-keepclassmembers class cn.gov.kaifeng.ftz.** {
    @android.webkit.JavascriptInterface <methods>;
}

# AndroidX
-keep class androidx.** { *; }
-dontwarn androidx.**

# Material
-keep class com.google.android.material.** { *; }
-dontwarn com.google.android.material.**
