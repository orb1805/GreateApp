package e.roman.greateapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainScreenActivity : AppCompatActivity() {

    public lateinit var btn_person : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        btn_person = findViewById(R.id.btn_person)
    }

    override fun onResume() {
        super.onResume()

        btn_person.setOnClickListener {
            val intent = Intent(this, PersonActivity::class.java)
            startActivity(intent)
        }
    }
}