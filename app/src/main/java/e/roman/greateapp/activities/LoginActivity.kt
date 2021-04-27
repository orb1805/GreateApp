package e.roman.greateapp.activities

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
import com.google.firebase.firestore.QuerySnapshot
import e.roman.greateapp.R
import e.roman.greateapp.controllers.DataBase
import e.roman.greateapp.controllers.User
import e.roman.greateapp.toMD5

class LoginActivity : AppCompatActivity() {

    private lateinit var btnReg: Button
    private lateinit var btnLogin: Button
    private lateinit var login: EditText
    private lateinit var password: EditText
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var db: FirebaseFirestore
    private lateinit var person: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        this.btnReg = findViewById(R.id.btn_reg)
        this.btnLogin = findViewById(R.id.btn_login)
        this.login = findViewById(R.id.login)
        this.password = findViewById(R.id.password)
        this.sharedPrefs = getSharedPreferences(getString(R.string.shared_prefs_name), Context.MODE_PRIVATE)
        this.db = FirebaseFirestore.getInstance()
    }

    override fun onResume() {
        super.onResume()

        btnReg.setOnClickListener {
            val intent = Intent(this, RegNameActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener { checkLogin() }
    }

    private fun checkLogin() {
        val login = this.login.text.toString()
        val password = this.password.text.toString().toMD5()
        if (login.isNotEmpty() && password.isNotEmpty()) {
            DataBase.getFromCollection(
                getString(R.string.field_users),
                login,
                ::checkPassword,
                ::onFailure
            )
        }
        Log.d("check22", "func finished")
    }

    private fun checkPassword(document: DocumentSnapshot?) {
        if (document != null) {
            if (document.data?.get(getString(R.string.field_password))
                    ?.toString() == this.password.text.toString().toMD5()
            ) {
                person = User(
                    this.login.text.toString(),
                    document!![getString(R.string.field_first_name)].toString(),
                    document[getString(R.string.field_second_name)].toString(),
                    document[getString(R.string.field_third_name)].toString(),
                    document[getString(R.string.field_birth_date)].toString(),
                    "--"
                )
                DataBase.getFromCollection(getString(R.string.coll_path_universities), document!![getString(
                    R.string.field_university
                )].toString(), ::savePerson, ::onFailure)
            } else {
                this.onFailure()
            }
        }
    }

    private fun savePerson(doc: DocumentSnapshot?) {
        Log.d("check22", "onSuccess start")
        if (doc != null) {
            sharedPrefs.edit().putString(
                getString(R.string.field_university),
                doc!![getString(R.string.field_name)].toString()
            ).apply()
            Log.d("check22", doc!![getString(R.string.field_name)].toString())
            sharedPrefs.edit().putBoolean(getString(R.string.field_signed), true).apply()
            sharedPrefs.edit()
                .putString(getString(R.string.field_login), this.login.text.toString()).apply()
            sharedPrefs.edit().putString(
                getString(R.string.field_first_name),
                person.name
            ).apply()
            sharedPrefs.edit().putString(
                getString(R.string.field_second_name),
                person.surname
            ).apply()
            sharedPrefs.edit().putString(
                getString(R.string.field_third_name),
                person.thirdName
            ).apply()
            sharedPrefs.edit().putString(
                getString(R.string.field_birth_date),
                person.birthDate
            ).apply()
            DataBase.getWholeCollection(getString(R.string.coll_path_events), ::startMainActivity, ::onFailure)
        }
    }

    private fun startMainActivity(documents: QuerySnapshot) {
        intent = Intent(this, MainScreenActivity::class.java)
        var names = ""
        val login = this.login.text.toString()
        for (doc in documents)
            if (doc[getString(R.string.field_owner)].toString() != login)
                names += "/" + doc[getString(R.string.field_name)]!!.toString()
        val bundle = Bundle()
        bundle.putString("names", names)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun onFailure() {
        Log.d("check22", "onFailure start")
        Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show()
        sharedPrefs.edit().putBoolean(getString(R.string.field_signed), false).apply()
    }

    override fun finish() {
        //чтобы нельзя было выйти из активити через встроенную кнопку "назад"
    }

}