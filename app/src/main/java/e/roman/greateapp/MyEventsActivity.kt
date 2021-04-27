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
    private lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_events)

        sp = getSharedPreferences(getString(R.string.shared_prefs_checked), Context.MODE_PRIVATE)
        sp.edit().putString(getString(R.string.field_checks), "0 0 0 0 0 0").apply() //0-description 1-date 2-time 3-people 4-price 5-phone
        buttonsRegs = mutableListOf()
        buttonsMy = mutableListOf()
        base = FirebaseFirestore.getInstance()
        sharedPrefs = getSharedPreferences(getString(R.string.shared_prefs_name), Context.MODE_PRIVATE)
        layout = findViewById(R.id.layout_my_events)
        btnAdd = findViewById(R.id.btn_add)
        val sp = getSharedPreferences(getString(R.string.shared_prefs_checked), Context.MODE_PRIVATE)
        btnAdd.setOnClickListener {
            sp.edit().putBoolean(getString(R.string.field_edit), false).apply()
            val intent = Intent(this, EventCreatorActivity::class.java)
            startActivity(intent)
        }
        //TODO: по-моему дальше пизда начинается
        var count = 0
        base.collection(getString(R.string.coll_path_registers)).whereArrayContains(getString(R.string.field_users), sharedPrefs.getString(getString(R.string.field_login), "--").toString()).get().addOnSuccessListener { it1 ->
            for (doc in it1){
                base.collection(getString(R.string.coll_path_events)).document(doc[getString(R.string.field_event)].toString()).get().addOnSuccessListener { it2 ->
                    if(it2[getString(R.string.field_owner)] == sharedPrefs.getString(getString(R.string.field_login), "--").toString()) {
                        buttonsMy.add(Button(layout.context))
                        val params = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            wallpaperDesiredMinimumHeight / 6
                        )
                        params.topMargin = 80
                        buttonsMy.last().layoutParams = params
                        buttonsMy.last().text = it2[getString(R.string.field_name)].toString()
                        buttonsMy.last().setBackgroundColor(Color.BLACK)
                        buttonsMy.last().setTextColor(Color.WHITE)
                        buttonsMy.last().setOnClickListener(this)
                    }
                    else {
                        buttonsRegs.add(Button(layout.context))
                        val params = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            wallpaperDesiredMinimumHeight / 6
                        )
                        params.topMargin = 80
                        buttonsRegs.last().layoutParams = params
                        buttonsRegs.last().text = it2[getString(R.string.field_name)].toString()
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
        base.collection(getString(R.string.coll_path_events)).whereEqualTo(getString(R.string.field_name), text).get().addOnSuccessListener {
            for (doc in it){
                owner = doc[getString(R.string.field_owner)].toString()
                bundle.putString(getString(R.string.field_name), text)
                bundle.putString(getString(R.string.field_owner), owner)
                val checks = doc[getString(R.string.field_checks)].toString().split(" ") //0-description 1-date 2-time 3-people 4-price 5-phone
                bundle.putString(getString(R.string.field_checks), doc[getString(R.string.field_checks)].toString())
                if (checks[0] == "1")
                    bundle.putString(getString(R.string.field_description), doc[getString(R.string.field_description)].toString())
                if (checks[1] == "1")
                    bundle.putString(getString(R.string.field_date), doc[getString(R.string.field_date)].toString())
                if (checks[2] == "1")
                    bundle.putString(getString(R.string.field_time), doc[getString(R.string.field_time)].toString())
                if (checks[3] == "1")
                    bundle.putString(getString(R.string.field_people), doc[getString(R.string.field_people)].toString())
                if (checks[4] == "1")
                    bundle.putString(getString(R.string.field_price), doc[getString(R.string.price)].toString())
                if (checks[5] == "1")
                    bundle.putString(getString(R.string.field_phone), doc[getString(R.string.field_phone)].toString())
                bundle.putString(getString(R.string.field_id), doc.id)
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