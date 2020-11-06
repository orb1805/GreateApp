package e.roman.greateapp

import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginBottom
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class EventCreatorActivity : AppCompatActivity(), FireBaseListener {

    private lateinit var btnAdd: Button
    private lateinit var extraButton: Button
    private lateinit var etName: EditText
    /*private lateinit var etDescription: EditText
    private lateinit var calendar: CalendarView*/
    private lateinit var base: FirebaseFirestore
    private lateinit var sharedPrefs: SharedPreferences
    private var isChanged = false
    private lateinit var extrasActivity: ExtrasActivity
    private lateinit var checked: MutableMap<String, Boolean>
    private lateinit var layout: LinearLayout
    private lateinit var sp: SharedPreferences

    private lateinit var etdescription: EditText
    private lateinit var etdate: EditText
    private lateinit var ettime: EditText
    private lateinit var etpeople: EditText
    private lateinit var etprice: EditText
    private lateinit var etphone: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_creator)

        /*btnAdd = findViewById(R.id.btn_add)
        extraButton = findViewById(R.id.btn_extra)*/
        sp = getSharedPreferences("checked", Context.MODE_PRIVATE)
        layout = findViewById(R.id.layout)
        btnAdd = Button(this)
        extraButton = Button(this)
        btnAdd.width = LinearLayout.LayoutParams.MATCH_PARENT
        btnAdd.height = LinearLayout.LayoutParams.WRAP_CONTENT
        extraButton.width = LinearLayout.LayoutParams.MATCH_PARENT
        extraButton.height = LinearLayout.LayoutParams.WRAP_CONTENT
        etName = findViewById(R.id.et_name)
        extraButton.text = "ДОПОЛНИТЕЛЬНО"
        btnAdd.text = "ДОБАВИТЬ"
        layout.addView(extraButton)
        layout.addView(btnAdd)
        /*etDescription = findViewById(R.id.et_description)
        calendar = findViewById(R.id.calendar)*/
        base = FirebaseFirestore.getInstance()
        sharedPrefs = getSharedPreferences("file", Context.MODE_PRIVATE)
        checked = mutableMapOf()
        //sp.edit().putBoolean("description", false).apply()
        checked["description"] = false
        //sp.edit().putBoolean("date", false).apply()
        checked["date"] = false
        //sp.edit().putBoolean("time", false).apply()
        checked["time"] = false
        //sp.edit().putBoolean("people", false).apply()
        checked["people"] = false
        //sp.edit().putBoolean("price", false).apply()
        checked["price"] = false
        //sp.edit().putBoolean("phone", false).apply()
        checked["phone"] = false
        sp.edit().putString("checks", "0 0 0 0 0 0").apply() //0-description 1-date 2-time 3-people 4-price 5-phone
    }

    override fun onResume() {
        super.onResume()

        btnAdd.setOnClickListener {
            val name = etName.text.toString()
            //val description = etDescription.text.toString()
            //TODO: проверка на заполненость всех полей
            if (name.isNotEmpty()/* && description.isNotEmpty()*/) {
                val doc: MutableMap<String, Any> = HashMap()
                doc["name"] = name
                doc["owner"] = sharedPrefs.getString("login", "--").toString()

                if (checked["description"]!!) {
                    doc["description"] = etdescription.text.toString()
                }
                if (checked["date"]!!) {
                    doc["date"] = etdate.text.toString()
                }
                if (checked["time"]!!) {
                    doc["time"] = ettime.text.toString()
                }
                if (checked["people"]!!) {
                    doc["people"] = etpeople.text.toString()
                }
                if (checked["price"]!!) {
                    doc["price"] = etprice.text.toString()
                }
                if (checked["phone"]!!) {
                    doc["phone"] = etphone.text.toString()
                }
                doc["checks"] = sp.getString("checks", "0 0 0 0 0 0")!!

                base.collection("events").add(doc).addOnSuccessListener { this.onSuccess(null) }
                    .addOnFailureListener {
                        this.onFailure(
                            "Adding failed"
                        )
                    }
                sharedPrefs.edit().putString("new name", name).apply()
            }
            else this.onFailure("Заполните все поля")
        }
        extraButton.setOnClickListener {
            isChanged = true
            extrasActivity = ExtrasActivity()
            startActivity(Intent(this,  extrasActivity::class.java))
        }
    }

    override fun onRestart() {
        super.onRestart()

        val checked1 = sp.getString("checks", "0 0 0 0 0 0")!!.split(" ")
        layout.removeView(btnAdd)
        layout.removeView(extraButton)
        if (checked1[0] == "1") {
            if (!checked["description"]!!) {
                checked["description"] = true
                var la = TextInputLayout(this)
                etdescription = EditText(this)
                etdescription.hint = "ОПИСАНИЕ"
                etdescription.width = LinearLayout.LayoutParams.MATCH_PARENT
                etdescription.height = LinearLayout.LayoutParams.WRAP_CONTENT
                la.addView(etdescription)
                layout.addView(la)
            }
        }
        if (checked1[1] == "1") {
            if (!checked["date"]!!) {
                checked["date"] = true
                var la = TextInputLayout(this)
                etdate = EditText(this)
                etdate.hint = "Дата"
                etdate.width = LinearLayout.LayoutParams.MATCH_PARENT
                etdate.height = LinearLayout.LayoutParams.WRAP_CONTENT
                la.addView(etdate)
                layout.addView(la)
            }
        }
        if (checked1[2] == "1") {
            if (!checked["time"]!!) {
                checked["time"] = true
                var la = TextInputLayout(this)
                ettime = EditText(this)
                ettime.hint = "ВРЕМЯ"
                ettime.width = LinearLayout.LayoutParams.MATCH_PARENT
                ettime.height = LinearLayout.LayoutParams.WRAP_CONTENT
                la.addView(ettime)
                layout.addView(la)
            }
        }
        if (checked1[3] == "1") {
            if (!checked["people"]!!) {
                checked["people"] = true
                var la = TextInputLayout(this)
                etpeople = EditText(this)
                etpeople.hint = "КОЛИЧЕСТВО ЛЮДЕЙ"
                etpeople.width = LinearLayout.LayoutParams.MATCH_PARENT
                etpeople.height = LinearLayout.LayoutParams.WRAP_CONTENT
                la.addView(etpeople)
                layout.addView(la)
            }
        }
        if (checked1[4] == "1") {
            if (!checked["price"]!!) {
                checked["price"] = true
                var la = TextInputLayout(this)
                etprice = EditText(this)
                etprice.hint = "ЦЕНА"
                etprice.width = LinearLayout.LayoutParams.MATCH_PARENT
                etprice.height = LinearLayout.LayoutParams.WRAP_CONTENT
                la.addView(etprice)
                layout.addView(la)
            }
        }
        if (checked1[5] == "1") {
            if (!checked["phone"]!!) {
                checked["phone"] = true
                var la = TextInputLayout(this)
                etphone = EditText(this)
                etphone.hint = "ТЕЛЕФОН"
                etphone.width = LinearLayout.LayoutParams.MATCH_PARENT
                etphone.height = LinearLayout.LayoutParams.WRAP_CONTENT
                la.addView(etphone)
                layout.addView(la)
            }
        }
        layout.addView(extraButton)
        layout.addView(btnAdd)
    }

    override fun onSuccess(document: DocumentSnapshot?) {
        finish()
    }

    override fun onFailure(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}