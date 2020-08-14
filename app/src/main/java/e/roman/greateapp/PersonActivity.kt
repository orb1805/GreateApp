package e.roman.greateapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_person.*

class PersonActivity : AppCompatActivity() {

    private lateinit var login : TextView
    private lateinit var date_of_birth : TextView
    private lateinit var university : TextView
    private lateinit var password : TextView
    private lateinit var logout : Button
    private lateinit var shared_prefs : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)

        login = findViewById(R.id.txt_login)
        date_of_birth = findViewById(R.id.txt_date_of_birth)
        university = findViewById(R.id.txt_university)
        password = findViewById(R.id.txt_password)
        logout = findViewById(R.id.btn_logout)
        shared_prefs = getSharedPreferences("file", Context.MODE_PRIVATE)
    }

    override fun onResume() {
        super.onResume()

        //TODO: проверка на наличине данных в файле
        if(shared_prefs.getBoolean("signed", false)) {
            login.text = "Login: " + shared_prefs.getString("login", "--")
            date_of_birth.text = "Date of birth: " + shared_prefs.getString("date_of_birth", "--")
            university.text = "University: " + shared_prefs.getString("university", "--")
            password.text = "Password: " + shared_prefs.getString("password", "--")
        }

        btn_logout.setOnClickListener {
            shared_prefs.edit().putBoolean("signed", false).apply()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}