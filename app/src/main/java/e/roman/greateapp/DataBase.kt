package e.roman.greateapp

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class DataBase : FireBaseListener{
    companion object {
        val dataBase = FirebaseFirestore.getInstance()
        fun addUniversities(s: String) {
            val regex = Regex(";")
            val universities = s.split(";")
            var i=0
            while(2 * i + 1 < universities.size) {
                val data : MutableMap<String, Any> = HashMap()
                data["name"] = universities[2 * i + 1]
                dataBase.collection("universities")
                    .document(universities[2 * i])
                    .set(data)
                ++i
            }
        }

        fun addUser(user: User) : Boolean{
            val doc: MutableMap<String, Any> = java.util.HashMap()
            doc["login"] = user.login
            doc["password"] = user.password
            doc["first_name"] = user.firstName
            doc["second_name"] = user.secondName
            doc["third_name"] = user.thirdName
            doc["university"] = user.universityId
            doc["birth_date"] = user.birthDate
            dataBase.collection("users").add(doc)
            return true
        }

        fun checkUniversity(s: String, context : FireBaseListener)/* : String */{
            val regex = s.toRegex()
            var id = "no"
            Log.d("MyLogCheckUniversity", "in")
            val res = dataBase.collection("universities")
                .get().addOnSuccessListener { result ->
                    for(document in result) {
                        if(regex.find(document.getString("name") as CharSequence) != null)
                            if(id != "no") {
                                //id="more" // Больше одного совпадения
                                context.onFailure()
                                break
                            }
                            else {
                                //id = document.id
                                Log.d("MyLogCheckUniversity", document.getString("name"))
                                context.onSuccess(document)
                            }
                    }
                }


            //return id
        }
    }

    override fun onSuccess(document: QueryDocumentSnapshot?) {
        TODO("Not yet implemented")
    }

    override fun onFailure() {
        TODO("Not yet implemented")
    }
}