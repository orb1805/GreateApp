package e.roman.greateapp.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import e.roman.greateapp.R
import e.roman.greateapp.controllers.DataBase
import e.roman.greateapp.controllers.SPController
import java.util.HashMap
import kotlin.math.log

class EventActivity : AppCompatActivity() {

    private lateinit var layout: LinearLayout
    private lateinit var functionalBtn: Button
    private lateinit var spController: SPController
    private lateinit var login: String
    private var notOwner = true
    private lateinit var users: MutableList<String>
    private lateinit var doc: QueryDocumentSnapshot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        this.spController = SPController(
            getSharedPreferences(
                getString(R.string.shared_prefs_name),
                Context.MODE_PRIVATE
            )
        )
        login = spController.get(getString(R.string.field_login), "--")
        layout = findViewById(R.id.layout_event)
        addTV(R.string.name, getString(R.string.field_name))
        addTV(R.string.owner, getString(R.string.field_owner))
        val checks = intent.getStringExtra(getString(R.string.field_checks))?.split(" ")
        if (checks?.get(0) == "1")
            addTV(R.string.description, getString(R.string.field_description))
        if (checks?.get(1) == "1")
            addTV(R.string.date, getString(R.string.field_date))
        if (checks?.get(2) == "1")
            addTV(R.string.time, getString(R.string.field_time))
        if (checks?.get(3) == "1")
            addTV(R.string.number_of_people, getString(R.string.field_people))
        if (checks?.get(4) == "1")
            addTV(R.string.price, getString(R.string.field_price))
        if (checks?.get(5) == "1")
            addTV(R.string.phone, getString(R.string.field_phone))

        functionalBtn = Button(this)
        functionalBtn.textSize = resources.getDimension(R.dimen.text_size)
        functionalBtn.width = LinearLayout.LayoutParams.MATCH_PARENT
        functionalBtn.height = LinearLayout.LayoutParams.WRAP_CONTENT
        if(login == intent.getStringExtra(getString(R.string.field_owner))){
            Log.d("check", "set edit click")
            functionalBtn.text = getString(R.string.edit)
            functionalBtn.setOnClickListener { editBtnClickListener() }
            notOwner = false
        }
        layout.addView(functionalBtn)
        DataBase.getDocumentWithEqual(getString(R.string.coll_path_registers), getString(R.string.field_event), intent.getStringExtra(getString(R.string.field_id)), ::showRegistered, ::onFailure)
    }

    private fun showRegistered(documents: QuerySnapshot) {
        val textView = TextView(this)
        if (documents.isEmpty) {
            textView.text = "ЗАРЕГЕСТРИРОВАНЫ: пустенько немножко"
        }
        else{
            for (doc in documents) {
                textView.text = getString(R.string.registered_list, doc.get(getString(R.string.field_users)).toString())
                if (notOwner) {
                    if (login in (doc.get(getString(R.string.field_users)) as List<*>)) {
                        Log.d("check", "set unreg click")
                        functionalBtn.text = getString(R.string.un_registr)
                        functionalBtn.setOnClickListener { unRegisterBtnClickListener() }
                    }
                    else{
                        Log.d("check", "set reg click")
                        functionalBtn.text = getString(R.string.registr)
                        functionalBtn.setOnClickListener { registerBtnClickListener() }
                    }
                }
            }
        }
        textView.textSize = resources.getDimension(R.dimen.text_size)
        layout.addView(textView)
    }

    private fun addRegisteredUser(documents: QuerySnapshot) {
        if (documents.isEmpty) {
            val document: MutableMap<String, Any?> = HashMap()
            document[getString(R.string.field_event)] =
                intent.getStringExtra(getString(R.string.field_id))?.toString()
            document[getString(R.string.field_users)] =
                listOf(spController.get(getString(R.string.field_login), "--"))
            DataBase.addDocumnet(getString(R.string.coll_path_registers), document)
        } else {
            for (doc in documents) {
                this.doc = doc
                users = doc.get(getString(R.string.field_users)) as MutableList<String>
                val login = spController.get(getString(R.string.field_id), "--")
                var flag = true
                for (i in users) {
                    if (i == login)
                        flag = false
                }
                if (flag)
                    DataBase.getFromCollection(getString(R.string.field_users), login, ::addFriend, ::onFailure)
            }
        }
    }

    private fun addFriend(document: DocumentSnapshot) {
        val friends = document[getString(R.string.field_friends)] as MutableList<String>
        for (i in users)
            if (i !in friends) {
                friends.add(i)
                DataBase.getFromCollection(getString(R.string.field_users), i, ::addUserAsFriend, ::onFailure)
            }
        DataBase.updateDocument(getString(R.string.field_users), login, getString(R.string.field_friends), friends)
        users.add(spController.get(getString(R.string.field_login), "--"))
        val document: MutableMap<String, Any?> = HashMap()
        document[getString(R.string.field_event)] = intent.getStringExtra(
            getString(
                R.string.field_id
            )
        )?.toString()
        document[getString(R.string.field_users)] = users
        DataBase.setDocument(getString(R.string.coll_path_registers), doc.id, document)
    }

    private fun addUserAsFriend(document: DocumentSnapshot) {
        val friends1 =
            document[getString(R.string.field_friends)] as MutableList<String>
        friends1.add(login)
        DataBase.updateDocument(getString(R.string.field_users), document["id"].toString(), getString(R.string.field_friends), friends1)
    }

    private fun removeFromFriends(documents: QuerySnapshot) {
        for (doc in documents) {
            val users = doc.get(getString(R.string.field_users)) as MutableList<String>
            Log.d("check", "$users - $login")
            users.remove(login)
            Log.d("check", "$users - $login")
            Log.d("check", doc.id)
            DataBase.updateDocument(getString(R.string.coll_path_registers), doc.id, getString(R.string.field_users), users)
        }
    }

    private fun addTV(source: Int, stringName: String){
        val name = getString(source) + ": " + intent.getStringExtra(stringName)
        val textView = TextView(this)
        textView.text = name
        textView.textSize = resources.getDimension(R.dimen.text_size)
        layout.addView(textView)
    }

    private fun registerBtnClickListener() {
        DataBase.getDocumentWithEqual(
            getString(R.string.coll_path_registers),
            getString(R.string.field_event),
            intent.getStringExtra(getString(R.string.field_id)).toString(),
            ::addRegisteredUser,
            ::onFailure
        )
    }

    private fun unRegisterBtnClickListener() {
        DataBase.getDocumentWithEqual(getString(R.string.coll_path_registers), getString(R.string.field_event), intent.getStringExtra(getString(R.string.field_id)).toString(), ::removeFromFriends, ::onFailure)
    }

    private fun editBtnClickListener(){
        val intent = Intent(this, EventCreatorActivity::class.java)
        startActivity(intent)
    }

    private fun onFailure() {
        Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show()
    }

}