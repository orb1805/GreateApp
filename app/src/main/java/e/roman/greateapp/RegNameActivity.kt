package e.roman.greateapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegNameActivity : AppCompatActivity() {

    private lateinit var firstName : TextInputEditText
    private lateinit var secondName : TextInputEditText
    private lateinit var thirdName : TextInputEditText
    private lateinit var firstNameLay : TextInputLayout
    private lateinit var secondNameLay : TextInputLayout
    private lateinit var thirdNameLay : TextInputLayout
    private lateinit var isMan : RadioButton
    private lateinit var isWoman : RadioButton
    private lateinit var nextBtn : Button
    private lateinit var birthDate : TextInputEditText
    private lateinit var sharedPrefs : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg_name)

        firstName = findViewById(R.id.textInputFirstName)
        secondName = findViewById(R.id.textInputSecondName)
        thirdName = findViewById(R.id.textInputThirdName)
        firstNameLay = findViewById(R.id.textInputFirstNameLay)
        secondNameLay = findViewById(R.id.textInputSecondNameLay)
        thirdNameLay = findViewById(R.id.textInputThirdNameLay)
        isMan = findViewById(R.id.radioButtonM)
        isWoman = findViewById(R.id.radioButtonW)
        nextBtn = findViewById(R.id.next_btn)
        birthDate = findViewById(R.id.textInputBirthDate)
        sharedPrefs = getSharedPreferences("file", Context.MODE_PRIVATE)

        isMan.setOnClickListener {
            isWoman.isChecked = false
        }
        isWoman.setOnClickListener {
            isMan.isChecked = false
        }
        nextBtn.setOnClickListener {
            if (firstName.text.toString().isNotEmpty() && secondName.text.toString().isNotEmpty() && thirdName.text.toString().isNotEmpty() && birthDate.text.toString().isNotEmpty()) {
                sharedPrefs.edit().putString("first_name", firstName.text.toString()).apply()
                sharedPrefs.edit().putString("second_name", secondName.text.toString()).apply()
                sharedPrefs.edit().putString("third_name", thirdName.text.toString()).apply()
                sharedPrefs.edit().putInt("gender", if(isMan.isChecked) 1 else if(isWoman.isChecked) 0 else -1).apply()
                sharedPrefs.edit().putString("birth_date",  birthDate.text.toString()).apply()
                startActivity(Intent(this, RegLogActivity::class.java))
            }
            else
                Toast.makeText(this, "Input all fields", Toast.LENGTH_SHORT).show()
        }
    }
}