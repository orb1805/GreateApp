package e.roman.greateapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ShareActionProvider
import com.google.android.material.textfield.TextInputEditText

class LogRegActivity : AppCompatActivity() {

    private lateinit var btn_reg : Button
    private lateinit var user : User
    private lateinit var login : TextInputEditText
    private lateinit var university : TextInputEditText
    private lateinit var date_of_birth : TextInputEditText
    private lateinit var password : TextInputEditText
    private lateinit var shared_prefs : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_reg)

        btn_reg = findViewById(R.id.btn_reg)
        login = findViewById(R.id.login)
        university = findViewById(R.id.university)
        date_of_birth = findViewById(R.id.date_of_birth)
        password = findViewById(R.id.password)
        shared_prefs = getPreferences(Context.MODE_PRIVATE)
    }

    override fun onResume() {
        super.onResume()
        btn_reg.setOnClickListener { this.addUser() }
    }

    private fun addUser(){
        val login = login.text.toString()
        val university = university.text.toString()
        val date_of_birth = date_of_birth.text.toString()
        val password = password.text.toString()
        user = User(login, date_of_birth, university, password)
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
}