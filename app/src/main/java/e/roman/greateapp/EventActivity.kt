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

    private lateinit var layout: LinearLayout
    /*private lateinit var registerBtn: Button
    private lateinit var editBtn: Button*/
    private lateinit var functionalBtn: Button
    private lateinit var base: FirebaseFirestore
    private lateinit var sharedPrefs : SharedPreferences
    private lateinit var login: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        this.sharedPrefs = getSharedPreferences("file", Context.MODE_PRIVATE)
        login = sharedPrefs.getString("login", "--").toString()
        var notOwner = true
        base = FirebaseFirestore.getInstance()
        layout = findViewById(R.id.layout_event)
        addTV(R.string.name, "name")
        addTV(R.string.owner, "owner")
        val checks = intent.getStringExtra("checks").split(" ")
        if (checks[0] == "1")
            addTV(R.string.description, "description")
        if (checks[1] == "1")
            addTV(R.string.date, "date")
        if (checks[2] == "1")
            addTV(R.string.time, "time")
        if (checks[3] == "1")
            addTV(R.string.number_of_people, "people")
        if (checks[4] == "1")
            addTV(R.string.price, "price")
        if (checks[5] == "1")
            addTV(R.string.phone, "phone")

        functionalBtn = Button(this)
        functionalBtn.textSize = resources.getDimension(R.dimen.text_size)
        functionalBtn.width = LinearLayout.LayoutParams.MATCH_PARENT
        functionalBtn.height = LinearLayout.LayoutParams.WRAP_CONTENT
        if(login == intent.getStringExtra("owner")){
            Log.d("check", "set edit click")
            functionalBtn.text = getString(R.string.edit)
            functionalBtn.setOnClickListener { editBtnClickListener() }
            notOwner = false
        }
        /*else{
            functionalBtn.text = getString(R.string.registr)
            //functionalBtn.setOnClickListener { registerBtnClickListener() }
        }*/
        layout.addView(functionalBtn)
        val textView = TextView(this)
        base.collection("registers").whereEqualTo("event", intent.getStringExtra("id").toString()).get().addOnSuccessListener {
            if (it.isEmpty) {
                textView.text = "ЗАРЕГЕСТРИРОВАНЫ: пустенько немножко"
            }
            else{
                for (doc in it) {
                    textView.text = "ЗАРЕГЕСТРИРОВАНЫ: ${doc.get("users").toString()}"
                    if (notOwner) {
                        if (login in (doc.get("users") as List<*>)) {
                            Log.d("check", "set unreg click")
                            functionalBtn.text = getString(R.string.un_registr)
                            functionalBtn.setOnClickListener { unRegisterBtnClickListener() }
                        }
                        else{
                            Log.d("check", "set reg click")
                            functionalBtn.text = getString(R.string.registr)
                            functionalBtn.setOnClickListener { registerBtnClickListener() }
                        }
                    }
                }
            }
        }
        textView.textSize = resources.getDimension(R.dimen.text_size)
        layout.addView(textView)
    }

    private fun addTV(source: Int, stringName: String){
        val name = getString(source) + ": " + intent.getStringExtra(stringName)
        val textView = TextView(this)
        textView.text = name
        textView.textSize = resources.getDimension(R.dimen.text_size)
        layout.addView(textView)
    }

    private fun registerBtnClickListener(){
        Log.d("check", "Register click")
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

    private fun unRegisterBtnClickListener(){
        base.collection("registers").whereEqualTo("event", intent.getStringExtra("id").toString()).get().addOnSuccessListener {
            for (doc in it) {
                val users = doc.get("users") as MutableList<String>
                Log.d("check", "$users - $login")
                users.remove(login)
                Log.d("check", "$users - $login")
                Log.d("check", "${doc.id}")
                base.collection("registers").document(doc.id).update("users", users).addOnSuccessListener { Log.d("check", "success deletion") }
            }
        }
    }

    private fun editBtnClickListener(){

    }

}