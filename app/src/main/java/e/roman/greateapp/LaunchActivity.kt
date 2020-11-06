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
    private lateinit var shared_prefs : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        //инциализация всех ресурсов
        shared_prefs = getSharedPreferences("file", Context.MODE_PRIVATE)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        val intent : Intent
        val signed = shared_prefs.getBoolean("signed", false)
        intent = if(signed){
            //TODO: проверка на совпадение ранее введенного пароля с тем, что есть в базе
            Intent(this, MainScreenActivity::class.java)
        } else{
            Intent(this, LoginActivity::class.java)
        }

        var names = ""
        val base = FirebaseFirestore.getInstance()
        var i = 0
        base.collection("events").get().addOnSuccessListener {
            for (doc in it) {
                    names += "/" + doc["name"]!!.toString()
                i++
            }
            val bundle = Bundle()
            bundle.putString("names", names)
            intent.putExtras(bundle)

            //intent = Intent(this, LogRegActivity::class.java)
            startActivity(intent)
        }
    }
}