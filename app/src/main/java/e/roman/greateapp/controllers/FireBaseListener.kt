package e.roman.greateapp.controllers

import com.google.firebase.firestore.DocumentSnapshot

interface FireBaseListener {
    fun onSuccess(document: DocumentSnapshot?)
    fun onFailure(msg : String?)
}