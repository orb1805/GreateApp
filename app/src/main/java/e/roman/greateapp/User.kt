package e.roman.greateapp

import android.content.ContentValues.TAG
import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class User {
    private var login : String
    //private var surname : String
    private var date_of_birth : String
    private var university : String
    private var password : String
    //TODO: добавить почту

    constructor(name : String, date_of_birth : String, university : String, password : String){
        this.login = name
        //this.surname = surname
        this.date_of_birth = date_of_birth
        this.university = university
        this.password = password
    }

    fun add_to_db() : Boolean {
        var db = FirebaseFirestore.getInstance()
        /*var myRef = db.getReference("user")
        myRef.setValue(this.name)*/
        val user: MutableMap<String, Any> = HashMap()
        var success = false
        user["login"] = this.login
        //user["surname"] = this.surname
        user["date_of_birth"] = this.date_of_birth
        user["university"] = this.university
        user["password"] = this.password
        val q = suc()
        db.collection("users").add(user).addOnSuccessListener{ q.putA(true) }.addOnFailureListener{ q.putA(false) }
        return q.a
    }

    class suc{
        public var a : Boolean = true
        fun putA(a : Boolean){
            this.a = a;
        }
    }
}