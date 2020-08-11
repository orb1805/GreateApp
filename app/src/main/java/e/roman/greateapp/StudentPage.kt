package e.roman.greateapp

import android.os.Build
import android.util.Log
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi

class StudentPage(private val url: String, private val webView: WebView) {
    var isAcceptable = -1 // 0 - не найден, 1 - найден, 2 - неверная каптча, 3 - технические шоколадки
    init {
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
    }

    fun loadPage() {
        webView.loadUrl(url)

        webView.webViewClient = object: WebViewClient() {
            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.d("MyLogFinish", "finished")

                webView.evaluateJavascript("(function() {" +
                        "var captcha_div = document.createElement('div');" +
                        "var body = document.getElementsByTagName('body')[0];" +
                        "var html_el = document.getElementsByTagName('html')[0];" +
                        "captcha_div.innerHTML = document.getElementById('captcha-image').outerHTML;" +
                        "html_el.insertBefore(captcha_div, body);" +
                        "body.style.visibility = 'hidden';" +
                        "var captcha = document.getElementsByClassName('captcha-image-container')[0];" +
                        "var captcha_div = document.createElement('div');" +
                        "captcha_div.innerHTML = captcha.outerHTML;" +
                        "invisible_div.style.display = 'none';" +
                        "return 'ok'; })();") {
                    Log.d("MyLogLoad", it)
                }
            }
        }
    }

    fun checkForm(firstName: String, secondName: String, thirdName: String, university: String,
                birthDate: String, gender: Int, captcha: String) {

        var loaded = false
        var jQuery = "javascript:" +
                "var first_name = '$firstName';" +
                "var second_name = '$secondName';" +
                "var third_name = '$thirdName';" +
                "var university = '$university';" +
                "var captcha = '$captcha';" +
                "var gender = $gender;" +
                "var in_first_name = document.getElementById('fdo-container-filter-input-name');" +
                "var in_second_name = document.getElementById('fdo-container-filter-input-surname');" +
                "var in_third_name = document.getElementById('fdo-container-filter-input-middlename');" +
                "var in_captcha = document.getElementById('captcha-input');" +
                "var in_university = document.getElementById('dropdown').getElementsByTagName('input')[0];" +
                "var in_birth_date = document.getElementById('fdo-container-filter-input-birthdate');" +
                "var in_gender = document.getElementsByClassName('fdo-container-filter-radio  fdo-container-filter-input-required ')[0].getElementsByTagName('input'); " +
                "if(gender == 1) in_gender[0].checked = true; else in_gender[1].checked = true; " +
                "in_first_name.value = first_name;" +
                "in_second_name.value = second_name;" +
                "in_third_name.value = third_name;" +
                "in_captcha.value = captcha;" +
                "in_university.value = '006';" +
                "in_birth_date.value = '02.07.2000';" +
                "var btn = document.getElementsByClassName('submit-btn')[0];" +
                "btn.click();"
        webView.loadUrl(jQuery)

        webView.webViewClient = object: WebViewClient() {
            override fun onRenderProcessGone(
                view: WebView?,
                detail: RenderProcessGoneDetail?
            ): Boolean {
                Log.d("MyLogFill", "finished")
                return super.onRenderProcessGone(view, detail)
            }

            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
                if(loaded)
                    return
                jQuery = "(function() {" +
                        "var found_msg = document.getElementById('message__1');" +
                        "var not_found_msg = document.getElementById('message__0');" +
                        "var technical_fail_msg = document.getElementById('message__999');" +
                        "var wrong_captcha_msg = document.getElementById('message__92');" +
                        "if(found_msg.style.display == 'block') return 'found';" +
                        "else if(not_found_msg.style.display == 'block') return 'not_found';" +
                        "else if(technical_fail_msg.style.display == 'block') return 'tech_fail';" +
                        "else if(wrong_captcha_msg.style.display == 'block') return 'wrong_captcha';" +
                        "else return 'no_result'; })()"
                webView.evaluateJavascript(jQuery) {
                    if(it != "\"no_result\"") {
                        loaded = true
                        Log.d("MyLogCheck", it)
                        if(it == "\"not_found\"")
                            isAcceptable = 0
                        if(it == "\"found\"")
                            isAcceptable = 1
                        if(it == "\"wrong_captcha\"")
                            isAcceptable = 2
                        if(it == "\"tech_fail\"")
                            isAcceptable = 3
                    }
                }
            }
        }
    }
}