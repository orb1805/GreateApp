package e.roman.greateapp

import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class User {
    val login : String
    val password : String
    val firstName : String
    val secondName: String
    val thirdName : String
    val universityId : String
    val birthDate : String

    constructor(login : String, password : String, firstName : String, secondName: String,
                thirdName : String, universityId : String, birthDate : String) {
        this.login = login
        this.password = password
        this.firstName = firstName
        this.secondName = secondName
        this.thirdName = thirdName
        this.universityId = universityId
        this.birthDate = birthDate
    }
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
        user["university"] = this.universityId
        user["birth_date"] = this.birthDate
        val query = DataBaseQuerySuccess()
        dataBase.collection("users").add(user)
            .addOnSuccessListener{ query.isSuccess = true }
            .addOnFailureListener{ query.isSuccess = false }
        return query.isSuccess
    }
}