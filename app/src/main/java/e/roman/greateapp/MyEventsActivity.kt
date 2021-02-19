    package e.roman.greateapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.google.firebase.firestore.FirebaseFirestore

class MyEventsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btnAdd: Button
    private lateinit var layout: LinearLayout
    private lateinit var base: FirebaseFirestore
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var buttonsRegs: MutableList<Button>
    private lateinit var buttonsMy: MutableList<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_events)

        buttonsRegs = mutableListOf()
        buttonsMy = mutableListOf()
        base = FirebaseFirestore.getInstance()
        sharedPrefs = getSharedPreferences("file", Context.MODE_PRIVATE)
        layout = findViewById(R.id.layout_my_events)
        btnAdd = findViewById(R.id.btn_add)
        btnAdd.setOnClickListener {
            val intent = Intent(this, EventCreatorActivity::class.java)
            startActivity(intent)
        }
        //TODO: по-моему дальше пизда начинается
        var count = 0
        base.collection("registers").whereArrayContains("users", sharedPrefs.getString("login", "--").toString()).get().addOnSuccessListener { it1 ->
            for (doc in it1){
                base.collection("events").document(doc["event"].toString()).get().addOnSuccessListener { it2 ->
                    if(it2["owner"] == sharedPrefs.getString("login", "--").toString()) {
                        buttonsMy.add(Button(layout.context))
                        val params = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            wallpaperDesiredMinimumHeight / 6
                        )
                        params.topMargin = 80
                        buttonsMy.last().layoutParams = params
                        buttonsMy.last().text = it2["name"].toString()
                        buttonsMy.last().setBackgroundColor(Color.BLACK)
                        buttonsMy.last().setTextColor(Color.WHITE)
                        buttonsMy.last().setOnClickListener(this)
                        //layout.addView(buttonsMy.last())
                    }
                    else {
                        buttonsRegs.add(Button(layout.context))
                        val params = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            wallpaperDesiredMinimumHeight / 6
                        )
                        params.topMargin = 80
                        buttonsRegs.last().layoutParams = params
                        buttonsRegs.last().text = it2["name"].toString()
                        buttonsRegs.last().setOnClickListener(this)
                    }
                    count++
                    if (count == it1.size())
                        addButtons()
                }
            }
        }
    }

    override fun onClick(button: View?) {
        val text = (button as Button).text.toString()
        var owner = "--"
        val intent = Intent(this, EventActivity::class.java)
        val bundle = Bundle()
        base.collection("events").whereEqualTo("name", text).get().addOnSuccessListener {
            for (doc in it){
                owner = doc["owner"].toString()
                bundle.putString("name", text)
                bundle.putString("owner", owner)
                val checks = doc["checks"].toString().split(" ") //0-description 1-date 2-time 3-people 4-price 5-phone
                bundle.putString("checks", doc["checks"].toString())
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
                bundle.putString("id", doc.id)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun addButtons(){
        for (i in buttonsMy)
            layout.addView(i)
        for (i in buttonsRegs)
            layout.addView(i)
    }
}