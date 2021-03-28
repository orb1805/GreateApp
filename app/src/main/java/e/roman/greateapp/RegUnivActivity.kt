package e.roman.greateapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.Toast
import androidx.core.content.edit
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class RegUnivActivity : AppCompatActivity(), FireBaseListener {

    private lateinit var university : TextInputEditText
    private lateinit var captcha : TextInputEditText
    private lateinit var sharedPrefs : SharedPreferences
    private lateinit var webView : WebView
    private lateinit var page: StudentPage
    private lateinit var context: FireBaseListener
    private lateinit var registrationButton : Button
    private val dataBase = FirebaseFirestore.getInstance()

    private lateinit var firstNameStr: String
    private lateinit var secondNameStr: String
    private lateinit var thirdNameStr: String
    private lateinit var universityStr: String
    private lateinit var birthDateStr: String
    private lateinit var loginStr: String
    private lateinit var passwordStr: String
    private lateinit var univId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg_univ)

        context = this
        registrationButton = findViewById(R.id.buttonRegistration)
        university = findViewById(R.id.textInputUniversity)
        captcha = findViewById(R.id.textInputCaptcha)
        sharedPrefs = getSharedPreferences(getString(R.string.shared_prefs_name), Context.MODE_PRIVATE)
        webView = findViewById(R.id.webView)
        page = StudentPage("https://www.mos.ru/karta-moskvicha/services-proverka-grazhdanina-v-reestre-studentov/", webView)
        page.loadPage()
    }

    override fun onResume() {
        super.onResume()

        registrationButton.setOnClickListener { this.addUser() }
        university.addTextChangedListener(textWatcher)
        captcha.addTextChangedListener(textWatcher)
    }

    private fun addUser(){
        firstNameStr = sharedPrefs.getString(getString(R.string.field_first_name), "--").toString()
        secondNameStr = sharedPrefs.getString(getString(R.string.field_second_name), "--").toString()
        thirdNameStr = sharedPrefs.getString(getString(R.string.field_third_name), "--").toString()
        universityStr = university.text.toString()
        birthDateStr = sharedPrefs.getString(getString(R.string.field_birth_date), "--").toString()
        val captcha = captcha.text.toString()
        val gender = sharedPrefs.getInt(getString(R.string.fielde_gender), -1)
        loginStr = sharedPrefs.getString(getString(R.string.field_login), "--").toString()
        passwordStr = sharedPrefs.getString(getString(R.string.field_password), "--").toString()

        val universityCallback = object : DataBase.UniversityCallback{
            override fun onCallback(universityId: String) {
                if(universityId == "more") {
                    Log.d("MyLogCheckUniversity", "More than one found")
                }
                else if(universityId == "no") {
                    Log.d("MyLogCheckUniversity", "Not found")
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

    private val textWatcher =  object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            if (university.text.toString().isNotEmpty() && captcha.text.toString().isNotEmpty())
                registrationButton.visibility = View.VISIBLE
            else
                registrationButton.visibility = View.INVISIBLE
        }
    }

    override fun onSuccess(document: DocumentSnapshot?) {
        sharedPrefs.edit().putBoolean(getString(R.string.field_signed), true).apply()
        dataBase.collection(getString(R.string.coll_path_universities)).document(univId).get().addOnSuccessListener { document ->
            if(document != null)
                sharedPrefs.edit().putString(getString(R.string.field_university), document!![getString(R.string.field_name)].toString()).apply()
        }
        sharedPrefs.edit().putString(getString(R.string.field_birth_date), birthDateStr).apply()
        sharedPrefs.edit().remove(getString(R.string.field_password)).apply()
        startActivity(Intent(this, MainScreenActivity::class.java))
    }

    override fun onFailure(msg : String?) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        sharedPrefs.edit().putBoolean(getString(R.string.field_signed), false).apply()
    }
}