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
        sharedPrefs = getSharedPreferences(getString(R.string.shared_prefs_name), Context.MODE_PRIVATE)
    }

    override fun onResume() {
        super.onResume()

        if(sharedPrefs.getBoolean(getString(R.string.field_signed), false)) {
            loginTV.text = getString(R.string.field_in_profile, getString(R.string.login), sharedPrefs.getString(getString(R.string.field_login), "--"))
            dateOfBirthTV.text = getString(R.string.field_in_profile, getString(R.string.birth_date), sharedPrefs.getString(getString(R.string.field_birth_date), "--"))
            universityTV.text =  getString(R.string.field_in_profile, getString(R.string.university), sharedPrefs.getString(getString(R.string.field_university), "--"))
            firstNameTV.text =  getString(R.string.field_in_profile, getString(R.string.first_name), sharedPrefs.getString(getString(R.string.field_first_name), "--"))
            secondNameTV.text =  getString(R.string.field_in_profile, getString(R.string.second_name), sharedPrefs.getString(getString(R.string.field_second_name), "--"))
            thirdNameTV.text =  getString(R.string.field_in_profile, getString(R.string.third_name), sharedPrefs.getString(getString(R.string.field_third_name), "--"))
        }

        logoutBtn.setOnClickListener {
            sharedPrefs.edit().putBoolean(getString(R.string.field_signed), false).apply()
            startActivity(Intent(this, LoginActivity::class.java))
        }

        friendsListBtn.setOnClickListener {
            startActivity(Intent(this, FriendsListActivity::class.java))
        }
    }
}