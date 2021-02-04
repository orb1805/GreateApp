package e.roman.greateapp

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class DataBase/* : FireBaseListener*/{
    interface UniversityCallback {
        fun onCallback(universityId: String)
    }


    companion object {
        val dataBase = FirebaseFirestore.getInstance()
        var universityId = ""

        fun addUniversities(s: String) {
            val regex = Regex(";")
            val universities = s.split(";")
            var i = 0
            while (2 * i + 1 < universities.size) {
                val data: MutableMap<String, Any> = HashMap()
                data["name"] = universities[2 * i + 1]
                dataBase.collection("universities")
                    .document(universities[2 * i])
                    .set(data)
                ++i
            }
        }

        //fun addUser(user: User): Boolean {
        fun addUser(login : String, password : String, firstName : String, secondName : String, thirdName : String, universityId : String, birthDate : String, context: FireBaseListener) : Boolean{
            dataBase.collection("users").document(login).get().addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.d("Check", "Success not null")
                    context.onFailure("Already registered")
                } else {
                    Log.d("Check", "Success null")
                    val doc: MutableMap<String, Any> = java.util.HashMap()
                    doc["password"] = password.toMD5()
                    doc["first_name"] = firstName
                    doc["second_name"] = secondName
                    doc["third_name"] = thirdName
                    doc["university"] = universityId
                    doc["birth_date"] = birthDate
                    doc["friends"] = listOf<String>()
                    dataBase.collection("users").document(login).set(doc)
                    context.onSuccess(null)
                }
            } .addOnFailureListener{ context.onFailure("Error") }
        /*val doc: MutableMap<String, Any> = java.util.HashMap()
            //doc["login"] = user.login
            /*doc["password"] = user.password.toMD5()
            doc["first_name"] = user.firstName
            doc["second_name"] = user.secondName
            doc["third_name"] = user.thirdName
            doc["university"] = user.universityId
            doc["birth_date"] = user.birthDate*/
            doc["password"] = password.toMD5()
            doc["first_name"] = firstName
            doc["second_name"] = secondName
            doc["third_name"] = thirdName
            doc["university"] = universityId
            doc["birth_date"] = birthDate
            dataBase.collection("users").document(login).set(doc)//.addOnSuccessListener { context.onSuccess(null) }.addOnFailureListener{ context.onFailure() }*/
            return true
        }


        fun checkUniversity(s: String, callback: UniversityCallback) {
           val regex = s.toRegex()
            universityId = "no"
            Log.d("MyLogCheckUniversity", "in")
            val res = dataBase.collection("universities")
                .get().addOnSuccessListener { result ->
                    for (document in result) {
                        if (regex.find(document.getString("name") as CharSequence) != null)
                            if (universityId != "no") {
                                universityId = "more" // Больше одного совпадения
                                break
                            } else {
                                universityId = document.id
                                Log.d("MyLogCheckUniversity", document?.getString("name"))
                            }
                    }
                    callback.onCallback(universityId)
                }
        }
    }
/*
    override fun onSuccess(document: DocumentSnapshot?) {
        TODO("Not yet implemented")
    }

    override fun onFailure() {
        TODO("Not yet implemented")
    }
 */
}