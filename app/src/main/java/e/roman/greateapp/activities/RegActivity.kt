package e.roman.greateapp.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import e.roman.greateapp.R
import e.roman.greateapp.controllers.StudentPage
import e.roman.greateapp.controllers.DataBase
import e.roman.greateapp.controllers.FireBaseListener

class RegActivity : AppCompatActivity(), FireBaseListener {

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
    private lateinit var sharedPrefs : SharedPreferences
    private lateinit var webView : WebView
    private lateinit var page: StudentPage
    private lateinit var context: FireBaseListener

    private lateinit var firstNameStr: String
    private lateinit var secondNameStr: String
    private lateinit var thirdNameStr: String
    private lateinit var universityStr: String
    private lateinit var birthDateStr: String
    private lateinit var loginStr: String
    private lateinit var passwordStr: String
    private lateinit var univId: String

    @SuppressLint("JavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)

        context = this
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
        sharedPrefs = getSharedPreferences(getString(R.string.shared_prefs_name), Context.MODE_PRIVATE)
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
        firstNameStr = firstName.text.toString()
        secondNameStr = secondName.text.toString()
        thirdNameStr = thirdName.text.toString()
        universityStr = university.text.toString()
        birthDateStr = birthDate.text.toString()
        val captcha = captcha.text.toString()
        val gender = if(isMan.isChecked) 1 else if(isWoman.isChecked) 0 else -1
        loginStr = login.text.toString()
        passwordStr = password.text.toString()
        val repeatPassword = repeatPassword.text.toString()


        val universityCallback = object : DataBase.UniversityCallback{
            override fun onCallback(universityId: String) {
                if(universityId == "more") {
                    Log.d("MyLogCheckUniversity", "More than one found")
                }
                else if(universityId == "no") {
                    Log.d("MyLogCheckUniversity", "Not found")
                }
                if(passwordStr != repeatPassword) {
                    Log.d("MyLogCheckPasswords", "Passwords are not equal")
                }

                val checkFormCallback = object: StudentPage.CheckFormCallback {
                    override fun onCallback(result: String) {
                        when (page.isAcceptable) {
                            0 -> { // не найден в реестре
                                Log.d("MyLogCheckData", "Student didn't find")
                            }
                            1 -> { // найден в реестре
                                Log.d("MyLogCheckData", "Student Found")
                                univId = universityId
                                DataBase.addUser(loginStr, passwordStr, firstNameStr, secondNameStr, thirdNameStr,
                                    universityId, birthDateStr, context)
                            }
                            2 -> { // неверная каптча
                                Log.d("MyLogCheckData", "Wrong captcha")
                            }
                            3 -> { // технические шоколадки
                                Log.d("MyLogCheckData", "Technical error")
                            }
                            else -> {
                                Log.d("MyLogCheck", "Don't loaded yet")
                            }
                        }
                    }
                }
                page.checkForm(firstNameStr, secondNameStr, thirdNameStr, universityId, birthDateStr, gender, captcha, checkFormCallback)
            }
        }

        DataBase.checkUniversity(universityStr, universityCallback)
    }

    override fun onSuccess(document: DocumentSnapshot?) {
        sharedPrefs.edit().putBoolean(getString(R.string.field_signed), true).apply()
        sharedPrefs.edit().putString(getString(R.string.field_login), loginStr).apply()
        sharedPrefs.edit().putString(getString(R.string.field_first_name), firstNameStr).apply()
        sharedPrefs.edit().putString(getString(R.string.field_second_name), secondNameStr).apply()
        sharedPrefs.edit().putString(getString(R.string.field_third_name), thirdNameStr).apply()
        dataBase.collection(getString(R.string.coll_path_universities)).document(univId).get().addOnSuccessListener { document ->
            if(document != null)
                sharedPrefs.edit().putString(getString(R.string.field_university), document!![getString(
                    R.string.field_name
                )].toString()).apply()
        }
        sharedPrefs.edit().putString(getString(R.string.field_birth_date), birthDateStr).apply()
        startActivity(Intent(RegActivity@this, MainScreenActivity::class.java))
    }

    override fun onFailure(msg : String?) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        sharedPrefs.edit().putBoolean(getString(R.string.field_signed), false).apply()
    }
}