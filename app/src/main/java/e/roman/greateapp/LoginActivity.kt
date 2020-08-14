package e.roman.greateapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_reg.*
import java.sql.BatchUpdateException

class LoginActivity : AppCompatActivity(), FireBaseListener{

    private lateinit var btn_reg : Button
    private lateinit var btn_login : Button
    private lateinit var login : EditText
    private lateinit var password : EditText
    private lateinit var shared_prefs : SharedPreferences
    private lateinit var db : FirebaseFirestore
    //private var suc = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        this.btn_reg = findViewById(R.id.btn_reg)
        this.btn_login = findViewById(R.id.btn_login)
        this.login = findViewById(R.id.login)
        this.password = findViewById(R.id.password)
        this.shared_prefs = getSharedPreferences("file", Context.MODE_PRIVATE)
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
        val password = this.password.text.toString()
        //val q = Suc()
        //val intent : Intent
        db.collection("users").whereEqualTo("login", login).get().addOnSuccessListener { documents ->
            for (document in documents)
                if(document.data["password"].toString() == password) {
                    /*q.putA(true)
                    q.putLogin(login)
                    q.putDate(document.data["date_of_birth"].toString())
                    q.putUniversity(document.data["university"].toString())
                    q.putPassword(document.data["password"].toString())*/
                    //this.putSuc(true)
                    this.onSuccess(document)
                }
                else {
                    //q.putA(false)
                    //this.putSuc(false)
                    //Toast.makeText(this, password + " = " + document.data["password"].toString(), Toast.LENGTH_SHORT).show()
                    this.onFailure()
                }
        }.addOnFailureListener{/*q.putA(false) this.putSuc(false)*/ this.onFailure()
            //Toast.makeText(this, "Ошибка доступа", Toast.LENGTH_SHORT).show()
        }
        //if(q.a){
        /*if(this.suc){
            intent = Intent(this, MainScreenActivity::class.java)
            shared_prefs.edit().putBoolean("signed", true).apply()
            shared_prefs.edit().putString("login", q.login).apply()
            shared_prefs.edit().putString("date_of_birth", q.date_of_birth).apply()
            shared_prefs.edit().putString("university", q.university).apply()
            shared_prefs.edit().putString("password", q.password).apply()
            startActivity(intent)
        }
        else {
            Toast.makeText(this, "Ошибка входа", Toast.LENGTH_SHORT).show()
            shared_prefs.edit().putBoolean("signed", false).apply()
        }*/
    }

    override fun finish() {
        //чтобы нельзя было выйти из активити через встроенную кнопку "назад"
    }

    override fun onSuccess(document : QueryDocumentSnapshot?) {
            val intent = Intent(this, MainScreenActivity::class.java)
            shared_prefs.edit().putBoolean("signed", true).apply()
            shared_prefs.edit().putString("login", document!!["login"].toString()).apply()
            shared_prefs.edit().putString("date_of_birth", document["birth_date"].toString()).apply()
            shared_prefs.edit().putString("university", document["university"].toString()).apply()
            shared_prefs.edit().putString("password", document["password"].toString()).apply()
            startActivity(intent)
    }

    override fun onFailure() {
        Toast.makeText(this, "Ошибка входа", Toast.LENGTH_SHORT).show()
        shared_prefs.edit().putBoolean("signed", false).apply()
    }
}