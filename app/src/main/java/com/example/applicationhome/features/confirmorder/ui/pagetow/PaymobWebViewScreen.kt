package com.example.applicationhome.features.confirmorder.ui.pagetow

import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.Keep
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.applicationhome.BuildConfig
import com.example.applicationhome.data.data.model.PaymentState

@Keep
class PaymobJSBridge(private val onResult: (Boolean) -> Unit) {

    @Keep
    @Suppress("unused") // 💡 لإزالة اللون الرمادي وتحذير Unused من Android Studio
    @JavascriptInterface
    fun onPaymentResult(isSuccess: Boolean) {
        onResult(isSuccess)
    }
}

@Composable
fun PaymobWebViewScreen(
    paymentToken : String,
    iframeId : String = BuildConfig.PAYMOB_IFRAME_ID,
    onPaymentStateChanged : (PaymentState) -> Unit
){
    val paymobUrl = "https://accept.paymob.com/api/acceptance/iframes/$iframeId?payment_token=$paymentToken"

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                val cookieManager = CookieManager.getInstance()
                cookieManager.setAcceptCookie(true)
                cookieManager.setAcceptThirdPartyCookies(this, true)

                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    javaScriptCanOpenWindowsAutomatically = true
                    allowFileAccess = true
                    mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                }

                // 2️⃣ ربط الـ JavascriptInterface مع الـ WebView
                addJavascriptInterface(
                    PaymobJSBridge { isSuccess ->
                        post { // ضمان تنفيذ النتيجة على الـ Main Thread الخاصة بالـ UI
                            if (isSuccess) {
                                onPaymentStateChanged(PaymentState.Success)
                            } else {
                                onPaymentStateChanged(PaymentState.Failed)
                            }
                        }
                    },
                    "AndroidBridge"
                )

                webChromeClient = WebChromeClient()

                webViewClient = object : WebViewClient() {

                    // 3️⃣ الاحتفاظ بالـ Redirection الطبيعي في حال قامت Paymob بتحويل الـ URL (مثل صفحات الـ 3DS)
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        val url = request?.url?.toString() ?: return false

                        if (url.contains(BuildConfig.PAYMOB_CALLBACK_URL) || url.contains("success=")) {
                            val isSuccess = request.url.getQueryParameter("success") == "true"
                            val isPending = request.url.getQueryParameter("pending") == "true"

                            when {
                                isSuccess -> onPaymentStateChanged(PaymentState.Success)
                                isPending -> onPaymentStateChanged(PaymentState.Loading)
                                else -> onPaymentStateChanged(PaymentState.Failed)
                            }
                            return true
                        }
                        return super.shouldOverrideUrlLoading(view, request)
                    }

                    // 4️⃣ حقن الـ MutationObserver مرة واحدة فقط عند تحميل الصفحة
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)

                        val observerScript = """
                            (function() {
                                if (window.paymobObserverInjected) return;
                                window.paymobObserverInjected = true;

                                function checkTextAndNotify() {
                                    var text = document.body ? document.body.innerText : '';
                                    if (text.indexOf('"success":true') !== -1 || text.indexOf('"success": true') !== -1) {
                                        window.AndroidBridge.onPaymentResult(true);
                                        return true;
                                    }
                                    if (text.indexOf('"success":false') !== -1 || text.indexOf('"success": false') !== -1) {
                                        window.AndroidBridge.onPaymentResult(false);
                                        return true;
                                    }
                                    return false;
                                }

                                // فحص فوري
                                if (!checkTextAndNotify()) {
                                    // إذا لم يتوفر الـ JSON بعد، ابدأ مراقبة الـ DOM حدثياً عند أي تعديل من Paymob
                                    var observer = new MutationObserver(function(mutations) {
                                        if (checkTextAndNotify()) {
                                            observer.disconnect(); // إيقاف المراقبة فور العثور على النتيجة
                                        }
                                    });
                                    observer.observe(document.body || document.documentElement, {
                                        childList: true,
                                        subtree: true,
                                        characterData: true
                                    });
                                }
                            })();
                        """.trimIndent()

                        view?.evaluateJavascript(observerScript, null)
                    }
                }

                loadUrl(paymobUrl)
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}