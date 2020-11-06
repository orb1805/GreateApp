package e.roman.greateapp

import android.content.Context
import android.content.SharedPreferences
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout

class ExtrasActivity : AppCompatActivity() {

    private lateinit var timeLayout: LinearLayout
    private lateinit var descriptionButton: Button
    private lateinit var dateButton: Button
    private lateinit var timeButton: Button
    private lateinit var peopleButton: Button
    private lateinit var priceButton: Button
    private lateinit var phoneButton: Button
    private lateinit var sp: SharedPreferences
    private lateinit var checked: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extras)

        sp = getSharedPreferences("checked", Context.MODE_PRIVATE)
        timeLayout = findViewById(R.id.layout_time)
        descriptionButton = findViewById(R.id.btn_description)
        dateButton = findViewById(R.id.btn_date)
        timeButton = findViewById(R.id.btn_time)
        peopleButton = findViewById(R.id.btn_people)
        priceButton = findViewById(R.id.btn_price)
        phoneButton = findViewById(R.id.btn_number)

        checked = sp.getString("checks", "0 0 0 0 0 0")!!.split(" ") as MutableList<String>
        descriptionButton.text = if (checked[0] == "0")
            "+"
        else
           "-"
        if (checked[1] == "0")
            dateButton.text = "+"
        else {
            dateButton.text = "-"
            timeLayout.visibility = View.VISIBLE
        }
        if (checked[2] == "0")
            timeButton.text = "+"
        else
            timeButton.text = "-"
        if (checked[3] == "0")
            peopleButton.text = "+"
        else
            timeButton.text = "-"
        if (checked[4] == "0")
            priceButton.text = "+"
        else
            priceButton.text = "-"
        if (checked[5] == "0")
            phoneButton.text = "+"
        else
            phoneButton.text = "-"
    }

    override fun onStart() {
        super.onStart()

        /*sp.edit().putBoolean("description", false).apply()
        sp.edit().putBoolean("date", false).apply()
        sp.edit().putBoolean("time", false).apply()
        sp.edit().putBoolean("people", false).apply()
        sp.edit().putBoolean("price", false).apply()
        sp.edit().putBoolean("phone", false).apply()*/

        descriptionButton.setOnClickListener {
            if (descriptionButton.text == "+") {
                //sp.edit().putBoolean("description", true).apply()
                descriptionButton.text = "-"
                checked[0] = "1"
            }
            else{
                //sp.edit().putBoolean("description", false).apply()
                descriptionButton.text = "+"
                checked[0] = "0"
            }
        }
        dateButton.setOnClickListener {
            if (dateButton.text == "+") {
                //sp.edit().putBoolean("date", true).apply()
                timeLayout.visibility = View.VISIBLE
                dateButton.text = "-"
                checked[1] = "1"
            }
            else{
                //sp.edit().putBoolean("date", false).apply()
                timeLayout.visibility = View.INVISIBLE
                dateButton.text = "+"
                checked[1] = "0"
            }
        }
        timeButton.setOnClickListener {
            if (timeButton.text == "+") {
                //sp.edit().putBoolean("time", true).apply()
                timeButton.text = "-"
                checked[2] = "1"
            }
            else{
                //sp.edit().putBoolean("time", false).apply()
                checked[2] = "1"
            }
        }
        peopleButton.setOnClickListener {
            if (peopleButton.text == "+") {
                //sp.edit().putBoolean("people", true).apply()
                peopleButton.text = "-"
                checked[3] = "1"
            }
            else{
                //sp.edit().putBoolean("people", false).apply()
                peopleButton.text = "+"
                checked[3] = "0"
            }
        }
        priceButton.setOnClickListener {
            if (priceButton.text == "+") {
                //sp.edit().putBoolean("price", true).apply()
                priceButton.text = "-"
                checked[4] = "1"
            }
            else{
                //sp.edit().putBoolean("price", false).apply()
                priceButton.text = "+"
                checked[4] = "0"
            }
        }
        phoneButton.setOnClickListener {
            if (phoneButton.text == "+") {
                //sp.edit().putBoolean("phone", true).apply()
                phoneButton.text = "-"
                checked[5] = "1"
            }
            else{
                //sp.edit().putBoolean("phone", false).apply()
                phoneButton.text = "+"
                checked[5] = "0"
            }
        }
    }

    override fun finish() {
        sp.edit().putString("checks", "${checked[0]} ${checked[1]} ${checked[2]} ${checked[3]} ${checked[4]} ${checked[5]}").apply()
        //sp.edit().putString("checks", "1 1 1 1 1 1").apply()
        super.finish()
    }
}