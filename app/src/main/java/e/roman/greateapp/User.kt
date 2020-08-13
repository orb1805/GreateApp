package e.roman.greateapp

import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class User(private val login : String, private val password : String,
           private val firstName : String, private val secondName: String,
           private val thirdName : String, private val university : String,
           private val birthDate : String) {

    private val dataBase: FirebaseFirestore = FirebaseFirestore.getInstance()
    //private var surname : String
    //TODO: добавить почту
    fun addToDataBase() : Boolean {
        /*var myRef = db.getReference("user")
        myRef.setValue(this.name)*/
        val user: MutableMap<String, Any> = HashMap()
        user["login"] = this.login
        user["password"] = this.password
        user["first_name"] = this.firstName
        user["second_name"] = this.secondName
        user["third_name"] = this.thirdName
        user["university"] = this.university
        user["birth_date"] = this.birthDate
        val query = DataBaseQuerySuccess()
        dataBase.collection("users").add(user)
            .addOnSuccessListener{ query.isSuccess = true }
            .addOnFailureListener{ query.isSuccess = false }
        return query.isSuccess
    }
}