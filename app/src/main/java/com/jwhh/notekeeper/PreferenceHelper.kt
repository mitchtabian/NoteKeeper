package com.jwhh.notekeeper

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import kotlin.reflect.KProperty

/**
 * For more information on the Singleton pattern with Kotlin:
 * Course name: Android Apps with Kotlin: Build Your First App (Author = Jim Wilson)
 * Video: 5.6: "Singletons and Data Classes"
 */
object PreferenceHelper{

    fun defaultPrefs(context: Context): SharedPreferences
            = PreferenceManager.getDefaultSharedPreferences(context)

    // Probably best to not use this. getDefaultSharedPreferences will avoid typos
    // and confusion caused by multiple files
    fun customPrefs(context: Context, name: String): SharedPreferences
            = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    /*
        Create an extension function for the 'edit' method in SharedPreferences
        https://kotlinlang.org/docs/reference/extensions.html
        https://kotlinlang.org/docs/reference/inline-functions.html
     */
    fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit){
        val editor = this.edit()
        operation(editor) // do the work needed
        editor.apply()
    }

    /**
     * puts a key value pair in shared prefs if doesn't exists, otherwise updates value on given [key]
     * Operators: https://kotlinlang.org/docs/reference/operator-overloading.html#operator-overloading
     */
    operator fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is String? -> edit({ it.putString(key, value) })
            is Int -> edit({ it.putInt(key, value) })
            is Boolean -> edit({ it.putBoolean(key, value) })
            is Float -> edit({ it.putFloat(key, value) })
            is Long -> edit({ it.putLong(key, value) })
            else -> throw UnsupportedOperationException("Unsupported Operation")
        }
    }

    /**
     * finds value on given key.
     * [T] is the type of value
     * @param defaultValue default value saved in SharedPrefs - will take null for strings, false for bool and -1 for numeric values if [defaultValue] is not specified
     * Operators: https://kotlinlang.org/docs/reference/operator-overloading.html#operator-overloading
     * https://kotlinlang.org/docs/reference/inline-functions.html#inline-properties
     */
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

}












