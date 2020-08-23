package e.roman.greateapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.RadioButton
import androidx.annotation.RequiresApi
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.concurrent.thread

class RegActivity : AppCompatActivity() {

    private val dataBase = FirebaseFirestore.getInstance()
    private lateinit var registrationButton : Button
    private lateinit var loginButton : Button
    private lateinit var firstName : TextInputEditText
    private lateinit var secondName : TextInputEditText
    private lateinit var thirdName : TextInputEditText
    private lateinit var university : TextInputEditText
    private lateinit var birthDate : TextInputEditText
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
        setContentView(R.layout.activity_reg)

        registrationButton = findViewById(R.id.buttonRegistration)
        loginButton = findViewById(R.id.buttonLogin)
        firstName = findViewById(R.id.textInputFirstName)
        secondName = findViewById(R.id.textInputSecondName)
        thirdName = findViewById(R.id.textInputThirdName)
        university = findViewById(R.id.textInputUniversity)
        birthDate = findViewById(R.id.textInputBirthDate)
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

    override fun onResume() {
        super.onResume()
        registrationButton.setOnClickListener { this.addUser() }
        loginButton.setOnClickListener { finish() }
        isMan.setOnClickListener {
            isWoman.isChecked = false
        }
        isWoman.setOnClickListener {
            isMan.isChecked = false
        }
    }

    private fun addUser(){
        val firstName = firstName.text.toString()
        val secondName = secondName.text.toString()
        val thirdName = thirdName.text.toString()
        val university = university.text.toString()
        val birthDate = birthDate.text.toString()
        val captcha = captcha.text.toString()
        val gender = if(isMan.isChecked) 1 else if(isWoman.isChecked) 0 else -1
        val login = login.text.toString()
        val password = password.text.toString()
        val repeatPassword = repeatPassword.text.toString()


        val universityCallback = object : DataBase.UniversityCallback{
            override fun onCallback(universityId: String) {
                if(universityId == "more") {
                    Log.d("MyLogCheckUniversity", "More than one found")
                }
                else if(universityId == "no") {
                    Log.d("MyLogCheckUniversity", "Not found")
                }
                if(password != repeatPassword) {
                    Log.d("MyLogCheckPasswords", "Passwords are not equal")
                }

                val checkFormCallback = object: StudentPage.CheckFormCallback {
                    override fun onCallback(result: String) {
                        if(page.isAcceptable == 0) { // не найден в реестре
                            Log.d("MyLogCheckData", "Student didn't find")
                        }
                        else if(page.isAcceptable == 1) { // найден в реестре
                            Log.d("MyLogCheckData", "Student Found")
                            DataBase.addUser(User(login, password, firstName, secondName, thirdName,
                                    universityId, birthDate))
                        }
                        else if(page.isAcceptable == 2) { // неверная каптча
                            Log.d("MyLogCheckData", "Wrong captcha")
                        }
                        else if(page.isAcceptable == 3) { // технические шоколадки
                            Log.d("MyLogCheckData", "Technical error")
                        }
                        else {
                            Log.d("MyLogCheck", "Don't loaded yet")
                        }
                    }
                }
                page.checkForm(firstName, secondName, thirdName, universityId,
                    birthDate, gender, captcha, checkFormCallback)
            }
        }

        DataBase.checkUniversity(university, universityCallback)
    }
}