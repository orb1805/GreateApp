package e.roman.greateapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import com.google.firebase.firestore.FirebaseFirestore

class MyEventsActivity : AppCompatActivity() {

    private lateinit var btnAdd: Button
    private lateinit var layout: LinearLayout
    private lateinit var base: FirebaseFirestore
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var buttons: MutableList<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_events)

        buttons = mutableListOf()
        base = FirebaseFirestore.getInstance()
        sharedPrefs = getSharedPreferences("file", Context.MODE_PRIVATE)
        layout = findViewById(R.id.layout_my_events)
        btnAdd = findViewById(R.id.btn_add)
        btnAdd.setOnClickListener { startActivity(Intent(this, EventCreatorActivity::class.java)) }
        //TODO: по-моему дальше пизда начинается
        base.collection("registers").whereArrayContains("users", sharedPrefs.getString("login", "--").toString()).get().addOnSuccessListener {
            for (doc in it){
                base.collection("events").document(doc["event"].toString()).get().addOnSuccessListener {
                    buttons.add(Button(layout.context))
                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        wallpaperDesiredMinimumHeight / 6
                    )
                    params.topMargin = 80
                    buttons.last().layoutParams = params
                    buttons.last().text = it["name"].toString()
                    layout.addView(buttons.last())
                    //buttons.last().setOnClickListener(this)
                }
            }
        }
    }
}