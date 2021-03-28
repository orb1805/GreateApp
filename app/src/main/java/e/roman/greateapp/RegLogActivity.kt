package e.roman.greateapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.log

class RegLogActivity : AppCompatActivity() {

    private lateinit var login : TextInputEditText
    private lateinit var password : TextInputEditText
    private lateinit var repeatPassword : TextInputEditText
    private lateinit var nextBtn : Button
    private lateinit var sharedPrefs : SharedPreferences
    private lateinit var dataBase : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg_log)

        login = findViewById(R.id.textInputLogin)
        password = findViewById(R.id.textInputPassword)
        repeatPassword = findViewById(R.id.textInputRepeatPassword)
        nextBtn = findViewById(R.id.next_btn)
        sharedPrefs = getSharedPreferences(getString(R.string.shared_prefs_name), Context.MODE_PRIVATE)
        dataBase = FirebaseFirestore.getInstance()
    }

    override fun onResume() {
        super.onResume()
        nextBtn.setOnClickListener {
            if (login.text.toString().isNotEmpty() && password.text.toString().isNotEmpty() && repeatPassword.text.toString().isNotEmpty()){
                val login = login.text.toString()
                val password = password.text.toString()
                val repeatPassword = repeatPassword.text.toString()
                if (password == repeatPassword){
                    dataBase.collection("users").document(login).get().addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists())
                            Toast.makeText(this, "Пользователь с таким логином сущетсвует", Toast.LENGTH_SHORT).show()
                        else{
                            sharedPrefs.edit().putString("login", login).apply()
                            sharedPrefs.edit().putString("password", password).apply()
                            startActivity(Intent(this, RegUnivActivity::class.java))
                        }
                    }
                }
                else
                    Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
            }
        }

        login.addTextChangedListener(textWatcher)
        password.addTextChangedListener(textWatcher)
        repeatPassword.addTextChangedListener(textWatcher)
    }

    private val textWatcher =  object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            if (login.text.toString().isNotEmpty() && password.text.toString().isNotEmpty() && repeatPassword.text.toString().isNotEmpty())
                nextBtn.visibility = View.VISIBLE
            else
                nextBtn.visibility = View.INVISIBLE
        }
    }
}