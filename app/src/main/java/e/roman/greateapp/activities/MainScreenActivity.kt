package e.roman.greateapp.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import e.roman.greateapp.R
import e.roman.greateapp.controllers.DataBase

class MainScreenActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btnPerson: Button
    private lateinit var btnMyEvents: Button
    private lateinit var layout: LinearLayout

    private lateinit var buttons: MutableList<Button>
    private lateinit var namesStr: String
    private lateinit var names: MutableList<String>
    private lateinit var text: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        btnPerson = findViewById(R.id.btn_person)
        btnMyEvents = findViewById(R.id.btn_my_events)
        layout = findViewById(R.id.layout)
        buttons = mutableListOf()
        namesStr = intent.getStringExtra(getString(R.string.field_names))
        if (namesStr != "") {
            names = namesStr.split(getString(R.string.delimiter)) as MutableList<String>
            names.removeAt(0)
            this.updateList()
        }
    }

    override fun onRestart() {
        super.onRestart()
        this.updateList()
    }

    private fun updateList() {
        if (buttons.isEmpty()) {
            for (i in names.indices) {
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
        } else {
            val sharedPrefs =
                getSharedPreferences(getString(R.string.shared_prefs_name), Context.MODE_PRIVATE)
            val newName = sharedPrefs.getString("new name", "--")
            if (newName != "--") {
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

    override fun onClick(view: View?) {
        text = (view as Button).text.toString()
        DataBase.getDocumentWithEqual(
            getString(R.string.coll_path_events),
            getString(R.string.field_name),
            text,
            ::startEventView,
            ::onFailure
        )
    }

    private fun startEventView(documents: QuerySnapshot) {
        var owner = "--"
        val intent = Intent(this, EventActivity::class.java)
        val bundle = Bundle()
        for (doc in documents) {
            owner = doc[getString(R.string.field_owner)].toString()
            bundle.putString(getString(R.string.field_name), text)
            bundle.putString(getString(R.string.field_owner), owner)
            bundle.putString(
                getString(R.string.coll_path_universities),
                doc[getString(R.string.coll_path_universities)].toString()
            )
            val checks = doc[getString(R.string.field_checks)].toString()
                .split(" ") //0-description 1-date 2-time 3-people 4-price 5-phone
            bundle.putString(
                getString(R.string.field_checks),
                doc[getString(R.string.field_checks)].toString()
            )
            if (checks[0] == "1")
                bundle.putString(
                    getString(R.string.field_description),
                    doc[getString(R.string.field_description)].toString()
                )
            if (checks[1] == "1")
                bundle.putString(
                    getString(R.string.field_date),
                    doc[getString(R.string.field_date)].toString()
                )
            if (checks[2] == "1")
                bundle.putString(
                    getString(R.string.field_time),
                    doc[getString(R.string.field_time)].toString()
                )
            if (checks[3] == "1")
                bundle.putString(
                    getString(R.string.field_people),
                    doc[getString(R.string.field_people)].toString()
                )
            if (checks[4] == "1")
                bundle.putString(
                    getString(R.string.field_price),
                    doc[getString(R.string.field_price)].toString()
                )
            if (checks[5] == "1")
                bundle.putString(
                    getString(R.string.field_phone),
                    doc[getString(R.string.field_phone)].toString()
                )
            bundle.putString(getString(R.string.field_id), doc.id)
            intent.putExtras(bundle)
            startActivity(intent)
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

    private fun onFailure() {
        Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show()
    }
}