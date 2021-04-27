package e.roman.greateapp.controllers

import android.content.SharedPreferences
import e.roman.greateapp.R

class SPController(private val sharedPreferences: SharedPreferences) {

    fun put(name: String, value: String) {
        sharedPreferences.edit().putString(
            name,
            value
        ).apply()
    }

    fun put(name: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(name, value).apply()
    }

    fun get(name: String, defaulValue: String): String {
        return sharedPreferences.getString(name, defaulValue).toString()
    }

}