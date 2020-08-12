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
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_reg.*
import kotlinx.android.synthetic.main.activity_reg.btn_login
import java.sql.BatchUpdateException

class LoginActivity : AppCompatActivity() {

    private lateinit var btn_reg : Button
    private lateinit var btn_login : Button
    private lateinit var login : EditText
    private lateinit var password : EditText
    private lateinit var shared_prefs : SharedPreferences
    private lateinit var db : FirebaseFirestore

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
        val password = this.password.toString()
        val q = Suc()
        val intent : Intent
        db.collection("users").whereEqualTo("login", login).get().addOnSuccessListener { documents ->
            for (document in documents)
                if(document.data["password"] == password)
                    q.putA(true)
                else
                    q.putA(false)
        }.addOnFailureListener{q.putA(false)}
        if(q.a){
            intent = Intent(this, MainScreenActivity::class.java)
            shared_prefs.edit().putBoolean("signed", true).apply()
            startActivity(intent)
        }
        else {
            Toast.makeText(this, "Ошибка входа", Toast.LENGTH_SHORT).show()
            shared_prefs.edit().putBoolean("signed", false).apply()
        }
    }
}