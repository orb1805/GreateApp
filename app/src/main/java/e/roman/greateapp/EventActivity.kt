package e.roman.greateapp

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EventActivity : AppCompatActivity() {

    private lateinit var nameTV: TextView
    private lateinit var descrTV: TextView
    private lateinit var ownerTV: TextView
    private lateinit var dateTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        /*nameTV = findViewById(R.id.txt_name)
        descrTV = findViewById(R.id.txt_descr)
        ownerTV = findViewById(R.id.txt_owner)
        dateTV = findViewById(R.id.txt_date)*/

        val name = intent.getStringExtra("name")
        /*val descr = intent.getStringExtra("description")
        val owner = intent.getStringExtra("owner")
        val date = intent.getStringExtra("date")*/

        nameTV.text = name
        /*descrTV.text = descr
        ownerTV.text = owner
        //val formatter = SimpleDateFormat("dd/MM/YYYY")
        dateTV.text = date.toLong().toString()*/
    }
}