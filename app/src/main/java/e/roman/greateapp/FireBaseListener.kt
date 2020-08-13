package e.roman.greateapp

import com.google.firebase.firestore.QueryDocumentSnapshot

interface FireBaseListener {
    fun onSuccess(document : QueryDocumentSnapshot)
    fun onFailure()
    //test
}