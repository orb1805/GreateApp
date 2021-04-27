package e.roman.greateapp.activities

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import e.roman.greateapp.R

class PersonViewerActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var loginTV: TextView
    private lateinit var nameTV: TextView
    private lateinit var surnameTV: TextView
    private lateinit var thirdNameTV: TextView
    private lateinit var removeAddBtn: Button
    private lateinit var base: FirebaseFirestore
    private lateinit var sharedPrefs : SharedPreferences
    private var added = true
    private var friendLogin = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_viewer)

        loginTV = findViewById(R.id.tv_login)
        nameTV = findViewById(R.id.tv_name)
        surnameTV = findViewById(R.id.tv_surname)
        thirdNameTV = findViewById(R.id.tv_third_name)
        removeAddBtn = findViewById(R.id.btn_remove_add)
        sharedPrefs = getSharedPreferences(getString(R.string.shared_prefs_name), Context.MODE_PRIVATE)
        friendLogin = intent.getStringExtra(getString(R.string.field_friend_login))
        base = FirebaseFirestore.getInstance()
        base.collection(getString(R.string.field_users)).document(friendLogin).get().addOnSuccessListener {
            loginTV.text = friendLogin
            surnameTV.text = it[getString(R.string.field_second_name)].toString()
            nameTV.text = it[getString(R.string.field_first_name)].toString()
            thirdNameTV.text = it[getString(R.string.field_third_name)].toString()
            if ((it[getString(R.string.field_friends)] as List<*>).contains(sharedPrefs.getString(getString(
                    R.string.field_login
                ), "--").toString())) {
                removeAddBtn.text = getString(R.string.remove_from_friends)
                added = true
            }
            else {
                removeAddBtn.text = getString(R.string.add_to_friends)
                added = false
            }
            removeAddBtn.setOnClickListener(this)
        }
    }

    override fun onClick(p0: View?) {
        if (added)
            base.collection(getString(R.string.field_users)).document(sharedPrefs.getString(getString(
                R.string.field_login
            ), "--").toString()).get().addOnSuccessListener {
                val friends = it.get(getString(R.string.field_friends)) as MutableList<String>
                friends.remove(friendLogin)
                base.collection(getString(R.string.field_users)).document(sharedPrefs.getString(getString(
                    R.string.field_login
                ), "--").toString()).update(getString(R.string.field_friends), friends)
                    .addOnSuccessListener { Log.d("check", "success deletion") }
                added = false
                removeAddBtn.text = getString(R.string.add_to_friends)
            }
        else
            base.collection(getString(R.string.field_users)).document(sharedPrefs.getString(getString(
                R.string.field_login
            ), "--").toString()).get().addOnSuccessListener {
                val friends = it.get(getString(R.string.field_friends)) as MutableList<String>
                friends.add(friendLogin)
                base.collection(getString(R.string.field_users)).document(sharedPrefs.getString(getString(
                    R.string.field_login
                ), "--").toString()).update(getString(R.string.field_friends), friends)
                    .addOnSuccessListener { Log.d("check", "success added") }
                added = true
                removeAddBtn.text = getString(R.string.remove_from_friends)
            }
    }
}