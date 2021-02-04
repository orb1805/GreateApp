package e.roman.greateapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap
import kotlin.math.log

class EventActivity : AppCompatActivity() {

    private lateinit var nameTV: TextView
    private lateinit var layout: LinearLayout
    private lateinit var registerBtn: Button
    private lateinit var base: FirebaseFirestore
    private lateinit var sharedPrefs : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        base = FirebaseFirestore.getInstance()
        layout = findViewById(R.id.layout_event)
        val name = getString(R.string.name) + ": " + intent.getStringExtra("name")
        var textView: TextView
        nameTV = TextView(this)
        nameTV.text = name
        nameTV.textSize = resources.getDimension(R.dimen.text_size)
        layout.addView(nameTV)
        val checks = intent.getStringExtra("checks").split(" ")
        if (checks[0] == "1") {
            textView = TextView(this)
            textView.text = getString(R.string.description) + ": " + intent.getStringExtra("description")
            textView.textSize = resources.getDimension(R.dimen.text_size)
            layout.addView(textView)
        }
        if (checks[1] == "1") {
            textView = TextView(this)
            textView.text =  getString(R.string.date) + ": " + intent.getStringExtra("date")
            textView.textSize = resources.getDimension(R.dimen.text_size)
            layout.addView(textView)
        }
        if (checks[2] == "1") {
            textView = TextView(this)
            textView.text = getString(R.string.time) + ": " + intent.getStringExtra("time")
            textView.textSize = resources.getDimension(R.dimen.text_size)
            layout.addView(textView)
        }
        if (checks[3] == "1") {
            textView = TextView(this)
            textView.text = getString(R.string.number_of_people) + ": " + intent.getStringExtra("people")
            textView.textSize = resources.getDimension(R.dimen.text_size)
            layout.addView(textView)
        }
        if (checks[4] == "1") {
            textView = TextView(this)
            textView.text = getString(R.string.price) + ": " + intent.getStringExtra("price")
            textView.textSize = resources.getDimension(R.dimen.text_size)
            layout.addView(textView)
        }
        if (checks[5] == "1"){
            textView = TextView(this)
            textView.text = getString(R.string.phone) + ": " + intent.getStringExtra("phone")
            textView.textSize = resources.getDimension(R.dimen.text_size)
            layout.addView(textView)
        }

        textView = TextView(this)
        base.collection("registers").whereEqualTo("event", intent.getStringExtra("id").toString()).get().addOnSuccessListener {
            if (it.isEmpty) {
                textView.text = "Зарегестирирваны: пустенько немножко"
            }
            else{
                for (doc in it) {
                    textView.text = "Зарегестрированы: ${doc.get("users").toString()}"
                }
            }
        }
        textView.textSize = resources.getDimension(R.dimen.text_size)
        layout.addView(textView)

        registerBtn = Button(this)
        registerBtn.text = getString(R.string.registr)
        registerBtn.textSize = resources.getDimension(R.dimen.text_size)
        registerBtn.width = LinearLayout.LayoutParams.MATCH_PARENT
        registerBtn.height = LinearLayout.LayoutParams.WRAP_CONTENT
        layout.addView(registerBtn)

        registerBtn.setOnClickListener {
            this.sharedPrefs = getSharedPreferences("file", Context.MODE_PRIVATE)
            base.collection("registers").whereEqualTo("event", intent.getStringExtra("id").toString()).get().addOnSuccessListener { it ->
                if (it.isEmpty) {
                    val document: MutableMap<String, Any> = HashMap()
                    document["event"] = intent.getStringExtra("id").toString()
                    document["users"] = listOf(sharedPrefs.getString("login", "--").toString())
                    base.collection("registers").add(document)
                }
                else {
                    for (doc in it) {
                        val users = doc.get("users") as MutableList<String>
                        val login = sharedPrefs.getString("login", "--").toString()
                        var flag = true
                        for (i in users) {
                            Log.d("tttt", "$i $login")
                            if (i == login)
                                flag = false
                        }
                        if (flag)
                            base.collection("users").document(login).get().addOnSuccessListener { it1 ->
                                val friends = it1["friends"] as MutableList<String>
                                for (i in users)
                                    if (i !in friends) {
                                        friends.add(i)
                                        base.collection("users").document(i).get().addOnSuccessListener {
                                            var friends1 = it["friends"] as MutableList<String>
                                            friends1.add(login)
                                            base.collection("users").document(i).update("friends", friends1)
                                        }
                                    }
                                base.collection("users").document(login).update("friends", friends)
                                users.add(sharedPrefs.getString("login", "--").toString())
                                val document: MutableMap<String, Any> = HashMap()
                                document["event"] = intent.getStringExtra("id").toString()
                                document["users"] = users
                                base.collection("registers").document(doc.id).set(document)
                            }
                    }
                }
            }
        }
    }
}