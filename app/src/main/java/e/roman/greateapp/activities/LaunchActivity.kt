package e.roman.greateapp.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import e.roman.greateapp.R

class LaunchActivity : AppCompatActivity() {

    private lateinit var sharedPrefs : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        //инциализация всех ресурсов
        sharedPrefs = getSharedPreferences(getString(R.string.shared_prefs_name), Context.MODE_PRIVATE)
    }

    override fun onResume() {
        super.onResume()

        val intent : Intent
        val signed = sharedPrefs.getBoolean(getString(R.string.field_signed), false)
        if(signed){
            //TODO: проверка на совпадение ранее введенного пароля с тем, что есть в базе
            intent = Intent(this, MainScreenActivity::class.java)
            var names = ""
            val base = FirebaseFirestore.getInstance()
            val login = sharedPrefs.getString(getString(R.string.field_login), "--")
            base.collection(getString(R.string.coll_path_events)).get().addOnSuccessListener {
                for (doc in it)
                    if (doc[getString(R.string.field_owner)].toString() != login)
                        names += "/" + doc[getString(R.string.field_name)]!!.toString()
                val bundle = Bundle()
                bundle.putString("names", names)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        } else
            startActivity(Intent(this, LoginActivity::class.java))
    }
}