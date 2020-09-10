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
    private lateinit var dateOfBirth : TextView
    private lateinit var university : TextView
    private lateinit var firstName : TextView
    private lateinit var secondName : TextView
    private lateinit var thirdName : TextView
    private lateinit var logout : Button
    private lateinit var sharedPrefs : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)

        login = findViewById(R.id.txt_login)
        dateOfBirth = findViewById(R.id.txt_date_of_birth)
        university = findViewById(R.id.txt_university)
        firstName = findViewById(R.id.txt_first_name)
        secondName = findViewById(R.id.txt_second_name)
        thirdName = findViewById(R.id.txt_third_name)
        logout = findViewById(R.id.btn_logout)
        sharedPrefs = getSharedPreferences("file", Context.MODE_PRIVATE)
    }

    override fun onResume() {
        super.onResume()

        if(sharedPrefs.getBoolean("signed", false)) {
            login.text = getString(R.string.login) + ": " + sharedPrefs.getString("login", "--")
            dateOfBirth.text = getString(R.string.birthDate) + ": " + sharedPrefs.getString("birth_date", "--")
            university.text = getString(R.string.university) + ": " + sharedPrefs.getString("university", "--")
            firstName.text = getString(R.string.firstName) + ": " + sharedPrefs.getString("first_name", "--")
            secondName.text = getString(R.string.secondName) + ": " + sharedPrefs.getString("second_name", "--")
            thirdName.text = getString(R.string.thirdName) + ": " + sharedPrefs.getString("third_name", "--")
        }

        btn_logout.setOnClickListener {
            sharedPrefs.edit().putBoolean("signed", false).apply()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}