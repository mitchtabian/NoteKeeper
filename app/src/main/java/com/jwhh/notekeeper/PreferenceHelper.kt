package com.jwhh.notekeeper

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.text.Editable


object PreferenceHelper{

    fun defaultPrefs(context: Context): SharedPreferences
            = PreferenceManager.getDefaultSharedPreferences(context)

    fun customPrefs(context: Context, name: String): SharedPreferences
            = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit){
        val editor = this.edit()
        operation(editor) // do the work needed
        editor.apply()
    }

    operator fun SharedPreferences.set(key: String, value: Any) {
        when (value) {
            is Editable -> {
                if(!value.toString().equals("")){
                    edit({ it.putString(key, value.toString()) })
                }

            }
            is String -> {
                if(!value.equals("")){
                    edit({ it.putString(key, value) })
                }
            }
            is Int -> edit({ it.putInt(key, value) })
            is Boolean -> edit({ it.putBoolean(key, value) })
            is Float -> edit({ it.putFloat(key, value) })
            is Long -> edit({ it.putLong(key, value) })
            else -> throw UnsupportedOperationException("Unsupported Operation")
        }
    }

    operator inline fun <reified T> SharedPreferences.get(key: String, defaultValue: T? = null): T? {
        return when (T::class) {
            String::class -> getString(key, defaultValue as? String) as T?
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Unsupported Operation")
        }
    }


    /*
        WANT MORE INFORMATION?
        1) https://kotlinlang.org/docs/reference/lambdas.html
        2) https://kotlinlang.org/docs/reference/keyword-reference.html
        3) https://kotlinlang.org/docs/reference/extensions.html
        4) https://kotlinlang.org/docs/reference/object-declarations.html
        5) https://kotlinlang.org/docs/reference/operator-overloading.html

     */

}













