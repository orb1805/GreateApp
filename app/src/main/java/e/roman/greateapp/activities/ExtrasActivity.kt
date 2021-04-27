package e.roman.greateapp.activities

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import e.roman.greateapp.R

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

        sp = getSharedPreferences(getString(R.string.shared_prefs_checked), Context.MODE_PRIVATE)
        timeLayout = findViewById(R.id.layout_time)
        descriptionButton = findViewById(R.id.btn_description)
        dateButton = findViewById(R.id.btn_date)
        timeButton = findViewById(R.id.btn_time)
        peopleButton = findViewById(R.id.btn_people)
        priceButton = findViewById(R.id.btn_price)
        phoneButton = findViewById(R.id.btn_number)

        checked = sp.getString(getString(R.string.field_checks), "0 0 0 0 0 0")!!.split(" ") as MutableList<String>
        descriptionButton.text = if (checked[0] == "0") getString(R.string.extra_plus)
        else getString(R.string.extra_minus)
        if (checked[1] == "0")
            dateButton.text = getString(R.string.extra_plus)
        else {
            dateButton.text = getString(R.string.extra_minus)
            timeLayout.visibility = View.VISIBLE
        }
        timeButton.text = if (checked[2] == "0") getString(R.string.extra_plus)
        else getString(R.string.extra_minus)
        peopleButton.text = if (checked[3] == "0") getString(R.string.extra_plus)
        else getString(R.string.extra_minus)
        priceButton.text = if (checked[4] == "0") getString(R.string.extra_plus)
        else getString(R.string.extra_minus)
        phoneButton.text = if (checked[5] == "0") getString(R.string.extra_plus)
        else getString(R.string.extra_minus)
    }

    override fun onStart() {
        super.onStart()

        descriptionButton.setOnClickListener {
            if (descriptionButton.text == getString(R.string.extra_plus)) {
                descriptionButton.text = getString(R.string.extra_minus)
                checked[0] = "1"
            }
            else{
                descriptionButton.text = getString(R.string.extra_plus)
                checked[0] = "0"
            }
        }
        dateButton.setOnClickListener {
            if (dateButton.text == getString(R.string.extra_plus)) {
                timeLayout.visibility = View.VISIBLE
                dateButton.text = getString(R.string.extra_minus)
                checked[1] = "1"
            }
            else{
                timeLayout.visibility = View.INVISIBLE
                dateButton.text = getString(R.string.extra_plus)
                checked[1] = "0"
            }
        }
        timeButton.setOnClickListener {
            if (timeButton.text == getString(R.string.extra_plus)) {
                timeButton.text = getString(R.string.extra_minus)
                checked[2] = "1"
            }
            else{
                checked[2] = "1"
            }
        }
        peopleButton.setOnClickListener {
            if (peopleButton.text == getString(R.string.extra_plus)) {
                peopleButton.text = getString(R.string.extra_minus)
                checked[3] = "1"
            }
            else{
                peopleButton.text = getString(R.string.extra_plus)
                checked[3] = "0"
            }
        }
        priceButton.setOnClickListener {
            if (priceButton.text == getString(R.string.extra_plus)) {
                priceButton.text = getString(R.string.extra_minus)
                checked[4] = "1"
            }
            else{
                priceButton.text = getString(R.string.extra_plus)
                checked[4] = "0"
            }
        }
        phoneButton.setOnClickListener {
            if (phoneButton.text == getString(R.string.extra_plus)) {
                phoneButton.text = getString(R.string.extra_minus)
                checked[5] = "1"
            }
            else{
                phoneButton.text = getString(R.string.extra_plus)
                checked[5] = "0"
            }
        }
    }

    override fun finish() {
        sp.edit().putString(getString(R.string.field_checks), "${checked[0]} ${checked[1]} ${checked[2]} ${checked[3]} ${checked[4]} ${checked[5]}").apply()
        super.finish()
    }
}