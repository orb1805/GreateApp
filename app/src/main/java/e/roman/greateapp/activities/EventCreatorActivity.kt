package e.roman.greateapp.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.DocumentSnapshot
import e.roman.greateapp.controllers.FireBaseListener
import e.roman.greateapp.R
import e.roman.greateapp.controllers.DataBase
import e.roman.greateapp.controllers.MemoryController
import java.util.*

class EventCreatorActivity : AppCompatActivity(), FireBaseListener {

    private lateinit var btnAdd: Button
    private lateinit var extraButton: Button
    private lateinit var etName: EditText
    private lateinit var memoryController: MemoryController
    private var isChanged = false
    private lateinit var extrasActivity: ExtrasActivity
    private lateinit var checked: MutableMap<String, Boolean>
    private lateinit var layout: LinearLayout
    private lateinit var memoryControllerChecked: MemoryController

    private lateinit var etdescription: EditText
    private lateinit var etdate: EditText
    private lateinit var ettime: EditText
    private lateinit var etpeople: EditText
    private lateinit var etprice: EditText
    private lateinit var etphone: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_creator)

        //sp = getSharedPreferences(getString(R.string.shared_prefs_checked), Context.MODE_PRIVATE)
        memoryControllerChecked = MemoryController(getSharedPreferences(getString(R.string.shared_prefs_checked), Context.MODE_PRIVATE))
        layout = findViewById(R.id.layout)
        btnAdd = Button(this)
        extraButton = Button(this)
        btnAdd.width = LinearLayout.LayoutParams.MATCH_PARENT
        btnAdd.height = LinearLayout.LayoutParams.WRAP_CONTENT
        extraButton.width = LinearLayout.LayoutParams.MATCH_PARENT
        extraButton.height = LinearLayout.LayoutParams.WRAP_CONTENT
        etName = findViewById(R.id.et_name)
        extraButton.text = getString(R.string.extra)
        btnAdd.text = getString(R.string.add)//"ДОБАВИТЬ"
        layout.addView(extraButton)
        layout.addView(btnAdd)
        memoryController = MemoryController(getSharedPreferences(getString(R.string.shared_prefs_name), Context.MODE_PRIVATE))
        checked = mutableMapOf()
        if (/*sp.getBoolean(getString(R.string.field_edit), false)*/ memoryControllerChecked.get(getString(R.string.field_edit), false)) {
            updateLayout()
        } else {
            checked[getString(R.string.field_description)] = false
            checked[getString(R.string.field_date)] = false
            checked[getString(R.string.field_time)] = false
            checked[getString(R.string.field_people)] = false
            checked[getString(R.string.field_price)] = false
            checked[getString(R.string.field_phone)] = false
            //sp.edit().putString(getString(R.string.field_checks), "0 0 0 0 0 0").apply() //0-description 1-date 2-time 3-people 4-price 5-phone
        }

        btnAdd.setOnClickListener {
            val name = etName.text.toString()
            //TODO: проверка на заполненость всех полей
            if (name.isNotEmpty()) {
                val doc: MutableMap<String, Any?> = HashMap()
                doc[getString(R.string.field_name)] = name
                //doc[getString(R.string.field_owner)] = sharedPrefs.getString(getString(R.string.field_login), "--").toString()
                doc[getString(R.string.field_owner)] = memoryController.get(getString(R.string.field_login), "--")

                if (checked[getString(R.string.field_description)]!!) {
                    doc[getString(R.string.field_description)] = etdescription.text.toString()
                }
                if (checked[getString(R.string.field_date)]!!) {
                    doc[getString(R.string.field_date)] = etdate.text.toString()
                }
                if (checked[getString(R.string.field_time)]!!) {
                    doc[getString(R.string.field_time)] = ettime.text.toString()
                }
                if (checked[getString(R.string.field_people)]!!) {
                    doc[getString(R.string.field_people)] = etpeople.text.toString()
                }
                if (checked[getString(R.string.field_price)]!!) {
                    doc[getString(R.string.field_price)] = etprice.text.toString()
                }
                if (checked[getString(R.string.field_phone)]!!) {
                    doc[getString(R.string.field_phone)] = etphone.text.toString()
                }
                doc[getString(R.string.field_checks)] = memoryControllerChecked.get(getString(R.string.field_checks), "0 0 0 0 0 0")
                DataBase.addDocumnet(getString(R.string.coll_path_events), doc)
                memoryController.put("new name", name)
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

        updateLayout()
    }

    override fun onSuccess(document: DocumentSnapshot?) {
        //sp.edit().putString(getString(R.string.field_checks), "0 0 0 0 0 0").apply() //0-description 1-date 2-time 3-people 4-price 5-phone
        memoryControllerChecked.put(getString(R.string.field_checks), "0 0 0 0 0 0")
        finish()
    }

    override fun onFailure(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun updateLayout() {
        val checked1 = memoryControllerChecked.get(getString(R.string.field_checks), "0 0 0 0 0 0").split(" ")
        Log.d("checks-test", checked1.toString())
        layout.removeView(btnAdd)
        layout.removeView(extraButton)
        Log.d("checks-test", "buttons removed")
        if (checked1[0] == "1") {
            if (!checked[getString(R.string.field_description)]!!) {
                checked[getString(R.string.field_description)] = true
                var la = TextInputLayout(this)
                etdescription = EditText(this)
                etdescription.hint = getString(R.string.description)//"ОПИСАНИЕ"
                etdescription.width = LinearLayout.LayoutParams.MATCH_PARENT
                etdescription.height = LinearLayout.LayoutParams.WRAP_CONTENT
                la.addView(etdescription)
                layout.addView(la)
            }
        }
        if (checked1[1] == "1") {
            if (!checked[getString(R.string.field_date)]!!) {
                checked[getString(R.string.field_date)] = true
                var la = TextInputLayout(this)
                etdate = EditText(this)
                etdate.hint = getString(R.string.date)//"ДАТА"
                etdate.width = LinearLayout.LayoutParams.MATCH_PARENT
                etdate.height = LinearLayout.LayoutParams.WRAP_CONTENT
                la.addView(etdate)
                layout.addView(la)
            }
        }
        if (checked1[2] == "1") {
            if (!checked[getString(R.string.field_time)]!!) {
                checked[getString(R.string.field_time)] = true
                var la = TextInputLayout(this)
                ettime = EditText(this)
                ettime.hint = getString(R.string.time)//"ВРЕМЯ"
                ettime.width = LinearLayout.LayoutParams.MATCH_PARENT
                ettime.height = LinearLayout.LayoutParams.WRAP_CONTENT
                la.addView(ettime)
                layout.addView(la)
            }
        }
        if (checked1[3] == "1") {
            if (!checked[getString(R.string.field_people)]!!) {
                checked[getString(R.string.field_people)] = true
                var la = TextInputLayout(this)
                etpeople = EditText(this)
                etpeople.hint = getString(R.string.number_of_people)//"КОЛИЧЕСТВО ЛЮДЕЙ"
                etpeople.width = LinearLayout.LayoutParams.MATCH_PARENT
                etpeople.height = LinearLayout.LayoutParams.WRAP_CONTENT
                la.addView(etpeople)
                layout.addView(la)
            }
        }
        if (checked1[4] == "1") {
            if (!checked[getString(R.string.field_price)]!!) {
                checked[getString(R.string.field_price)] = true
                var la = TextInputLayout(this)
                etprice = EditText(this)
                etprice.hint = getString(R.string.price)//"ЦЕНА"
                etprice.width = LinearLayout.LayoutParams.MATCH_PARENT
                etprice.height = LinearLayout.LayoutParams.WRAP_CONTENT
                la.addView(etprice)
                layout.addView(la)
            }
        }
        if (checked1[5] == "1") {
            if (!checked[getString(R.string.field_phone)]!!) {
                checked[getString(R.string.field_phone)] = true
                var la = TextInputLayout(this)
                etphone = EditText(this)
                etphone.hint = getString(R.string.phone)//"ТЕЛЕФОН"
                etphone.width = LinearLayout.LayoutParams.MATCH_PARENT
                etphone.height = LinearLayout.LayoutParams.WRAP_CONTENT
                la.addView(etphone)
                layout.addView(la)
            }
        }
        layout.addView(extraButton)
        layout.addView(btnAdd)
    }
}