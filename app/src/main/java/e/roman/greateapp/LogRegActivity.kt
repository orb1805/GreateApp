package e.roman.greateapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_log_reg.*
import kotlin.concurrent.thread

class LogRegActivity : AppCompatActivity() {

    private lateinit var registrationButton : Button
    private lateinit var user : User
    private lateinit var firstName : TextInputEditText
    private lateinit var secondName : TextInputEditText
    private lateinit var thirdName : TextInputEditText
    private lateinit var university : TextInputEditText
    //private lateinit var birthDate : TextInputEditText
    private lateinit var login : TextInputEditText
    private lateinit var captcha : TextInputEditText
    private lateinit var password : TextInputEditText
    private lateinit var repeatPassword : TextInputEditText
    private lateinit var isMan : RadioButton
    private lateinit var isWoman : RadioButton
    private lateinit var shared_prefs : SharedPreferences
    private lateinit var webView : WebView
    private lateinit var page: StudentPage

    @SuppressLint("JavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_reg)

        registrationButton = findViewById(R.id.buttonRegistration)
        firstName = findViewById(R.id.textInputFirstName)
        secondName = findViewById(R.id.textInputSecondName)
        thirdName = findViewById(R.id.textInputThirdName)
        university = findViewById(R.id.textInputUniversity)
        //birthDate = findViewById(R.id.textInputBirthDate)
        login = findViewById(R.id.textInputLogin)
        password = findViewById(R.id.textInputPassword)
        repeatPassword = findViewById(R.id.textInputRepeatPassword)
        captcha = findViewById(R.id.textInputCaptcha)
        shared_prefs = getPreferences(Context.MODE_PRIVATE)
        isMan = findViewById(R.id.radioButtonM)
        isWoman = findViewById(R.id.radioButtonW)
        webView = findViewById(R.id.webView)
        page = StudentPage("https://www.mos.ru/karta-moskvicha/services-proverka-grazhdanina-v-reestre-studentov/", webView)

        page.loadPage()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onResume() {
        super.onResume()
        registrationButton.setOnClickListener { this.addUser() }
        isMan.setOnClickListener {
            isWoman.isChecked = false;
        }
        isWoman.setOnClickListener {
            isMan.isChecked = false;
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun addUser(){
        val firstName = firstName.text.toString()
        val secondName = secondName.text.toString()
        val thirdName = thirdName.text.toString()
        val university = university.text.toString()
        val captcha = captcha.text.toString()
        val gender = if(isMan.isChecked) 1 else if(isWoman.isChecked) 0 else -1
        Log.d("MyLogGender", gender.toString())
        //val birthDate = birthDate.text.toString()
        val password = password.text.toString()
        val login = login.text.toString()

        page.checkForm(firstName, secondName, thirdName, university, "", gender, captcha)

        thread {
            Thread.sleep(1000)

            if(page.isAcceptable == 0) { // не найден в реестре
                page.loadPage()
            }
            else if(page.isAcceptable == 1) { // найден в реестре
                user = User(login, "", university, password)
                if(user.add_to_db()){
                    shared_prefs.edit().putBoolean("signed", true)
                    val intent = Intent(this, MainScreenActivity::class.java)
                    startActivity(intent)
                }
                else{
                    //TODO: сообщение об ошибке
                    shared_prefs.edit().putBoolean("signed", false)
                }
            }
            else if(page.isAcceptable == 2) { // неверная каптча
                page.loadPage()
            }
            else if(page.isAcceptable == 3) { // технические шоколадки
                page.loadPage()
            }
            else {
                page.loadPage()
            }
        }
    }
}