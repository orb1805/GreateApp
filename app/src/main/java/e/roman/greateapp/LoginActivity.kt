package e.roman.greateapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_reg.*
import java.security.MessageDigest
import java.sql.BatchUpdateException

class LoginActivity : AppCompatActivity(), FireBaseListener{

    private lateinit var btn_reg : Button
    private lateinit var btn_login : Button
    private lateinit var login : EditText
    private lateinit var password : EditText
    private lateinit var sharedPrefs : SharedPreferences
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        this.btn_reg = findViewById(R.id.btn_reg)
        this.btn_login = findViewById(R.id.btn_login)
        this.login = findViewById(R.id.login)
        this.password = findViewById(R.id.password)
        this.sharedPrefs = getSharedPreferences("file", Context.MODE_PRIVATE)
        this.db = FirebaseFirestore.getInstance()
    }

    override fun onResume() {
        super.onResume()

        btn_reg.setOnClickListener {
            val intent = Intent(this, RegActivity::class.java)
            startActivity(intent)
        }

        btn_login.setOnClickListener{ checkLogin() }
    }

    private fun checkLogin(){
        val login = this.login.text.toString()
        val password = this.password.text.toString().toMD5()
        if (login.isNotEmpty() && password.isNotEmpty()) {
            db.collection("users").document(login).get().addOnSuccessListener { document ->
                if (document != null) {
                    if (document.data?.get("password")?.toString()  == password) {
                        this.onSuccess(document)
                    } else {
                        this.onFailure()
                    }
                }
                /*else
                    this.onFailure()*/
            }.addOnFailureListener { this.onFailure() }
        }
    }

    override fun finish() {
        //чтобы нельзя было выйти из активити через встроенную кнопку "назад"
    }

    override fun onSuccess(document : DocumentSnapshot?) {
        val intent = Intent(this, MainScreenActivity::class.java)
        sharedPrefs.edit().putBoolean("signed", true).apply()
        sharedPrefs.edit().putString("login", this.login.text.toString()).apply()
        //sharedPrefs.edit().putString("password", document!!["password"].toString()).apply()
        sharedPrefs.edit().putString("first_name", document!!["first_name"].toString()).apply()
        sharedPrefs.edit().putString("second_name", document["second_name"].toString()).apply()
        sharedPrefs.edit().putString("third_name", document["third_name"].toString()).apply()
        sharedPrefs.edit().putString("university", document["university"].toString()).apply()
        sharedPrefs.edit().putString("birth_date", document["birth_date"].toString()).apply()
        startActivity(intent)
    }

    override fun onFailure() {
        Toast.makeText(this, "Ошибка входа", Toast.LENGTH_SHORT).show()
        sharedPrefs.edit().putBoolean("signed", false).apply()
    }

}