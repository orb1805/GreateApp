package e.roman.greateapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap

class EventActivity : AppCompatActivity() {

    private lateinit var layout: LinearLayout
    private lateinit var functionalBtn: Button
    private lateinit var base: FirebaseFirestore
    private lateinit var sharedPrefs : SharedPreferences
    private lateinit var login: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        this.sharedPrefs = getSharedPreferences(getString(R.string.shared_prefs_name), Context.MODE_PRIVATE)
        login = sharedPrefs.getString(getString(R.string.field_login), "--").toString()
        var notOwner = true
        base = FirebaseFirestore.getInstance()
        layout = findViewById(R.id.layout_event)
        addTV(R.string.name, getString(R.string.field_name))
        addTV(R.string.owner, getString(R.string.field_owner))
        val checks = intent.getStringExtra(getString(R.string.field_checks))?.split(" ")
        if (checks?.get(0) == "1")
            addTV(R.string.description, getString(R.string.field_description))
        if (checks?.get(1) == "1")
            addTV(R.string.date, getString(R.string.field_date))
        if (checks?.get(2) == "1")
            addTV(R.string.time, getString(R.string.field_time))
        if (checks?.get(3) == "1")
            addTV(R.string.number_of_people, getString(R.string.field_people))
        if (checks?.get(4) == "1")
            addTV(R.string.price, getString(R.string.field_price))
        if (checks?.get(5) == "1")
            addTV(R.string.phone, getString(R.string.field_phone))

        functionalBtn = Button(this)
        functionalBtn.textSize = resources.getDimension(R.dimen.text_size)
        functionalBtn.width = LinearLayout.LayoutParams.MATCH_PARENT
        functionalBtn.height = LinearLayout.LayoutParams.WRAP_CONTENT
        if(login == intent.getStringExtra(getString(R.string.field_owner))){
            Log.d("check", "set edit click")
            functionalBtn.text = getString(R.string.edit)
            functionalBtn.setOnClickListener { editBtnClickListener() }
            notOwner = false
        }
        layout.addView(functionalBtn)
        val textView = TextView(this)
        base.collection(getString(R.string.coll_path_registers)).whereEqualTo(getString(R.string.field_event), intent.getStringExtra(getString(R.string.field_id))?.toString()).get().addOnSuccessListener {
            if (it.isEmpty) {
                textView.text = "ЗАРЕГЕСТРИРОВАНЫ: пустенько немножко"
            }
            else{
                for (doc in it) {
                    //textView.text = "ЗАРЕГЕСТРИРОВАНЫ: ${doc.get(getString(R.string.field_users)).toString()}"
                        textView.text = getString(R.string.registered_list, doc.get(getString(R.string.field_users)).toString())
                    if (notOwner) {
                        if (login in (doc.get(getString(R.string.field_users)) as List<*>)) {
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
        base.collection(getString(R.string.coll_path_registers)).whereEqualTo(getString(R.string.field_event), intent.getStringExtra(getString(R.string.field_id))?.toString()).get().addOnSuccessListener { it ->
            if (it.isEmpty) {
                val document: MutableMap<String, Any?> = HashMap()
                document[getString(R.string.field_event)] = intent.getStringExtra(getString(R.string.field_id))?.toString()
                document[getString(R.string.field_users)] = listOf(sharedPrefs.getString(getString(R.string.field_login), "--").toString())
                base.collection(getString(R.string.coll_path_registers)).add(document)
            }
            else {
                for (doc in it) {
                    val users = doc.get(getString(R.string.field_users)) as MutableList<String>
                    val login = sharedPrefs.getString(getString(R.string.field_id), "--").toString()
                    var flag = true
                    for (i in users) {
                        if (i == login)
                            flag = false
                    }
                    if (flag)
                        base.collection(getString(R.string.field_users)).document(login).get().addOnSuccessListener { it1 ->
                            val friends = it1[getString(R.string.field_friends)] as MutableList<String>
                            for (i in users)
                                if (i !in friends) {
                                    friends.add(i)
                                    base.collection(getString(R.string.field_users)).document(i).get().addOnSuccessListener {
                                        val friends1 = it[getString(R.string.field_friends)] as MutableList<String>
                                        friends1.add(login)
                                        base.collection(getString(R.string.field_users)).document(i).update(getString(R.string.field_friends), friends1)
                                    }
                                }
                            base.collection(getString(R.string.field_users)).document(login).update(getString(R.string.field_friends), friends)
                            users.add(sharedPrefs.getString(getString(R.string.field_login), "--").toString())
                            val document: MutableMap<String, Any?> = HashMap()
                            document[getString(R.string.field_event)] = intent.getStringExtra(getString(R.string.field_id))?.toString()
                            document[getString(R.string.field_users)] = users
                            base.collection(getString(R.string.coll_path_registers)).document(doc.id).set(document)
                        }
                }
            }
        }
    }

    private fun unRegisterBtnClickListener(){
        base.collection(getString(R.string.coll_path_registers)).whereEqualTo(getString(R.string.field_event), intent.getStringExtra(getString(R.string.field_id))?.toString()).get().addOnSuccessListener {
            for (doc in it) {
                val users = doc.get(getString(R.string.field_users)) as MutableList<String>
                Log.d("check", "$users - $login")
                users.remove(login)
                Log.d("check", "$users - $login")
                Log.d("check", doc.id)
                base.collection(getString(R.string.coll_path_registers)).document(doc.id).update(getString(R.string.field_users), users).addOnSuccessListener { Log.d("check", "success deletion") }
            }
        }
    }

    private fun editBtnClickListener(){
        /*val sp = getSharedPreferences(getString(R.string.shared_prefs_checked), Context.MODE_PRIVATE)
        sp.edit().putBoolean(getString(R.string.field_edit), true).apply()
        sp.edit().putString(getString(R.string.field_checks), intent.getStringExtra(getString(R.string.field_checks))?.toString()).apply()*/
        val intent = Intent(this, EventCreatorActivity::class.java)
        //intent.putExtra(getString(R.string.field_checks), che)
        startActivity(intent)
    }

}