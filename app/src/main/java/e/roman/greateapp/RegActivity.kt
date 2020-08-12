package e.roman.greateapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText

class RegActivity : AppCompatActivity() {

    private lateinit var btn_reg : Button
    private lateinit var btn_login : Button
    private lateinit var user : User
    private lateinit var login : TextInputEditText
    private lateinit var university : TextInputEditText
    private lateinit var date_of_birth : TextInputEditText
    private lateinit var password : TextInputEditText
    private lateinit var shared_prefs : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)

        btn_reg = findViewById(R.id.btn_reg)
        btn_login = findViewById(R.id.btn_login)
        login = findViewById(R.id.login)
        university = findViewById(R.id.university)
        date_of_birth = findViewById(R.id.date_of_birth)
        password = findViewById(R.id.password)
        shared_prefs = getSharedPreferences("file", Context.MODE_PRIVATE)
    }

    override fun onResume() {
        super.onResume()
        btn_reg.setOnClickListener { this.addUser() }
        btn_login.setOnClickListener { finish() }
    }

    private fun addUser(){
        val login = login.text.toString()
        val university = university.text.toString()
        val date_of_birth = date_of_birth.text.toString()
        val password = password.text.toString()
        user = User(login, date_of_birth, university, password)
        if(user.add_to_db()){
            shared_prefs.edit().putBoolean("signed", true).apply()
            val intent = Intent(this, MainScreenActivity::class.java)
            startActivity(intent)
        }
        else{
            //TODO: сообщение об ошибке
            shared_prefs.edit().putBoolean("signed", false).apply()
        }
    }
}