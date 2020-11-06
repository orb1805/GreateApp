package e.roman.greateapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class MainScreenActivity : AppCompatActivity(), View.OnClickListener{

    private lateinit var btnPerson: Button
    private lateinit var btnMyEvents: Button
    private lateinit var layout: LinearLayout
    private lateinit var base: FirebaseFirestore
    private lateinit var buttons: MutableList<Button>
    private lateinit var namesStr: String
    private lateinit var names: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        btnPerson = findViewById(R.id.btn_person)
        btnMyEvents = findViewById(R.id.btn_my_events)
        layout = findViewById(R.id.layout)
        base = FirebaseFirestore.getInstance()
        buttons = mutableListOf()
        namesStr = intent.getStringExtra("names")
        if (namesStr != "") {
            names = namesStr.split("/") as MutableList<String>
            names.removeAt(0)
            this.updateList()
        }
    }

    override fun onRestart() {
        super.onRestart()
        this.updateList()
    }

    private fun updateList(){
        if (buttons.isEmpty()) {
            /*base.collection("events").get().addOnSuccessListener {
                for (doc in it) {
                    buttons.add(Button(layout.context))
                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        wallpaperDesiredMinimumHeight / 6
                    )
                    params.topMargin = 80
                    buttons.last().layoutParams = params
                    buttons.last().text = doc["name"]!!.toString()
                    layout.addView(buttons.last())
                    buttons.last().setOnClickListener(this)
                }
            }*/
            for (i in names.indices){
                buttons.add(Button(layout.context))
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    wallpaperDesiredMinimumHeight / 6
                )
                params.topMargin = 80
                buttons.last().layoutParams = params
                buttons.last().text = names[i]
                layout.addView(buttons.last())
                buttons.last().setOnClickListener(this)
            }
        }
        else{
            val sharedPrefs = getSharedPreferences("file", Context.MODE_PRIVATE)
            val newName = sharedPrefs.getString("new name", "--")
            if (newName != "--"){
                buttons.add(Button(layout.context))
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    wallpaperDesiredMinimumHeight / 6
                )
                params.topMargin = 80
                buttons.last().layoutParams = params
                buttons.last().text = newName
                layout.addView(buttons.last())
                buttons.last().setOnClickListener(this)
                sharedPrefs.edit().remove("new name").apply()
            }
        }
    }

    override fun onClick(view: View?){
        val text = (view as Button).text.toString()
        var descr = "--"
        var owner = "--"
        var date = "--"
        val intent = Intent(this, EventActivity::class.java)
        val bundle = Bundle()
        base.collection("events").whereEqualTo("name", text).get().addOnSuccessListener {
            for (doc in it){
                //descr = doc["description"].toString()
                owner = doc["owner"].toString()
                bundle.putString("name", text)
                bundle.putString("owner", owner)
                /*date = doc["date"].toString()
                bundle.putString("name", text)
                bundle.putString("description", descr)
                bundle.putString("owner", owner)
                bundle.putString("date", date)*/
                val checks = doc["checks"].toString().split(" ") //0-description 1-date 2-time 3-people 4-price 5-phone
                if (checks[0] == "1")
                    bundle.putString("description", doc["description"].toString())
                if (checks[1] == "1")
                    bundle.putString("date", doc["date"].toString())
                if (checks[2] == "1")
                    bundle.putString("time", doc["time"].toString())
                if (checks[3] == "1")
                    bundle.putString("people", doc["people"].toString())
                if (checks[4] == "1")
                    bundle.putString("price", doc["price"].toString())
                if (checks[5] == "1")
                    bundle.putString("phone", doc["phone"].toString())
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        btnPerson.setOnClickListener { startActivity(Intent(this, PersonActivity::class.java)) }
        btnMyEvents.setOnClickListener { startActivity(Intent(this, MyEventsActivity::class.java)) }
    }

    override fun finish() {
        //чтобы нельзя было выйти из активити через встроенную кнопку "назад"
    }
}