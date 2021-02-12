package e.roman.greateapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_reg.*
import org.w3c.dom.Text

class PersonViewerActivity : AppCompatActivity() {

    private lateinit var loginTV: TextView
    private lateinit var nameTV: TextView
    private lateinit var surnameTV: TextView
    private lateinit var thirdNameTV: TextView
    private lateinit var base: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_viewer)

        loginTV = findViewById(R.id.tv_login)
        nameTV = findViewById(R.id.tv_name)
        surnameTV = findViewById(R.id.tv_surname)
        thirdNameTV = findViewById(R.id.tv_third_name)
        val friendLogin = intent.getStringExtra("friend_login")
        base = FirebaseFirestore.getInstance()
        base.collection("users").document(friendLogin).get().addOnSuccessListener {
            loginTV.text = friendLogin
            surnameTV.text = it["second_name"].toString()
            nameTV.text = it["first_name"].toString()
            thirdNameTV.text = it["third_name"].toString()
        }
    }
}