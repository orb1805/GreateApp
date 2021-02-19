package e.roman.greateapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.sign

class LaunchActivity : AppCompatActivity() {

    private lateinit var sharedPrefs : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        //инциализация всех ресурсов
        sharedPrefs = getSharedPreferences("file", Context.MODE_PRIVATE)
    }

    override fun onResume() {
        super.onResume()

        val intent : Intent
        val signed = sharedPrefs.getBoolean("signed", false)
        if(signed){
            //TODO: проверка на совпадение ранее введенного пароля с тем, что есть в базе
            intent = Intent(this, MainScreenActivity::class.java)
            var names = ""
            val base = FirebaseFirestore.getInstance()
            val login = sharedPrefs.getString("login", "--")
            base.collection("events").get().addOnSuccessListener {
                for (doc in it)
                    if (doc["owner"].toString() != login)
                        names += "/" + doc["name"]!!.toString()
                val bundle = Bundle()
                bundle.putString("names", names)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        } else
            startActivity(Intent(this, LoginActivity::class.java))
    }
}