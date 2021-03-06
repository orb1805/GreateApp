package e.roman.greateapp.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import e.roman.greateapp.R
import e.roman.greateapp.controllers.DataBase
import e.roman.greateapp.controllers.MemoryController

class FriendsListActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var layout: LinearLayout
    private lateinit var memoryController: MemoryController
    private lateinit var buttons: MutableList<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends_list)

        layout = findViewById(R.id.layout_friends)
        memoryController = MemoryController(getSharedPreferences(getString(R.string.shared_prefs_name), Context.MODE_PRIVATE))
        buttons = mutableListOf()
    }

    override fun onResume() {
        super.onResume()

        for (i in buttons)
            layout.removeView(i)
        buttons.clear()
        DataBase.getFromCollection(getString(R.string.field_users), memoryController.get(getString(R.string.field_login), "--"), ::makeFriendsButtons, ::onFailure)
    }

    private fun makeFriendsButtons(document: DocumentSnapshot) {
        for (friend in document[getString(R.string.field_friends)] as List<*>) {
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

    override fun onClick(view: View?){
        val text = (view as Button).text.toString()
        val intent = Intent(this, PersonViewerActivity::class.java)
        val bundle = Bundle()
        bundle.putString(getString(R.string.field_friend_login), text)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun onFailure() {
        Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show()
    }
}