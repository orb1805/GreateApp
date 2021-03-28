package e.roman.greateapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.google.firebase.firestore.FirebaseFirestore

class FriendsListActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var layout: LinearLayout
    private lateinit var dataBase: FirebaseFirestore
    private lateinit var sharedPrefs : SharedPreferences
    private lateinit var buttons: MutableList<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends_list)

        layout = findViewById(R.id.layout_friends)
        dataBase = FirebaseFirestore.getInstance()
        this.sharedPrefs = getSharedPreferences(getString(R.string.shared_prefs_name), Context.MODE_PRIVATE)
        buttons = mutableListOf()
    }

    override fun onResume() {
        super.onResume()

        for (i in buttons)
            layout.removeView(i)
        buttons.clear()
        dataBase.collection(getString(R.string.field_users)).document(sharedPrefs.getString(getString(R.string.field_login), "--").toString()).get().addOnSuccessListener {
            for (friend in it[getString(R.string.field_friends)] as List<*>){
                buttons.add(Button(layout.context))
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    wallpaperDesiredMinimumHeight / 6
                )
                params.topMargin = 80
                buttons.last().layoutParams = params
                buttons.last().text = friend.toString()
                layout.addView(buttons.last())
                buttons.last().setOnClickListener(this)
            }
        }
    }

    override fun onClick(view: View?){
        val text = (view as Button).text.toString()
        val intent = Intent(this, PersonViewerActivity::class.java)
        val bundle = Bundle()
        bundle.putString(getString(R.string.field_friend_login), text)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}