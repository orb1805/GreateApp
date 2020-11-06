package e.roman.greateapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

private lateinit var btnAdd: Button

class MyEventsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_events)

        btnAdd = findViewById(R.id.btn_add)
    }

    override fun onResume() {
        super.onResume()

        btnAdd.setOnClickListener { startActivity(Intent(this, EventCreatorActivity::class.java)) }
    }
}