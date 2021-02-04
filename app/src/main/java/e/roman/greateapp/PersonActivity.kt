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

    private lateinit var loginTV : TextView
    private lateinit var dateOfBirthTV : TextView
    private lateinit var universityTV : TextView
    private lateinit var firstNameTV : TextView
    private lateinit var secondNameTV : TextView
    private lateinit var thirdNameTV : TextView
    private lateinit var logoutBtn : Button
    private lateinit var friendsListBtn : Button
    private lateinit var sharedPrefs : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)

        loginTV = findViewById(R.id.txt_login)
        dateOfBirthTV = findViewById(R.id.txt_date_of_birth)
        universityTV = findViewById(R.id.txt_university)
        firstNameTV = findViewById(R.id.txt_first_name)
        secondNameTV = findViewById(R.id.txt_second_name)
        thirdNameTV = findViewById(R.id.txt_third_name)
        logoutBtn = findViewById(R.id.btn_logout)
        friendsListBtn = findViewById(R.id.btn_friends_list)
        sharedPrefs = getSharedPreferences("file", Context.MODE_PRIVATE)
    }

    override fun onResume() {
        super.onResume()

        if(sharedPrefs.getBoolean("signed", false)) {
            loginTV.text = getString(R.string.login) + ": " + sharedPrefs.getString("login", "--")
            dateOfBirthTV.text = getString(R.string.birthDate) + ": " + sharedPrefs.getString("birth_date", "--")
            universityTV.text = getString(R.string.university) + ": " + sharedPrefs.getString("university", "--")
            firstNameTV.text = getString(R.string.firstName) + ": " + sharedPrefs.getString("first_name", "--")
            secondNameTV.text = getString(R.string.secondName) + ": " + sharedPrefs.getString("second_name", "--")
            thirdNameTV.text = getString(R.string.thirdName) + ": " + sharedPrefs.getString("third_name", "--")
        }

        logoutBtn.setOnClickListener {
            sharedPrefs.edit().putBoolean("signed", false).apply()
            startActivity(Intent(this, LoginActivity::class.java))
        }

        friendsListBtn.setOnClickListener {
            startActivity(Intent(this, FriendsListActivity::class.java))
        }
    }
}