package e.roman.greateapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        if(signed){
            //TODO: проверка на совпадение ранее введенного пароля с тем, что был введен до этого
            intent = Intent(this, MainScreenActivity::class.java)
        }
        else{
            intent = Intent(this, LogRegActivity::class.java)
        }
        //intent = Intent(this, LogRegActivity::class.java)
        startActivity(intent)
    }
}