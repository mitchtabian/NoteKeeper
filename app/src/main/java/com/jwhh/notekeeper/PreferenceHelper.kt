package com.jwhh.notekeeper

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.text.Editable
import kotlin.reflect.KProperty


object PreferenceHelper{

    fun defaultPrefs(context: Context): SharedPreferences
            = PreferenceManager.getDefaultSharedPreferences(context)

    fun customPrefs(context: Context, name: String): SharedPreferences
            = context.getSharedPreferences(name, Context.MODE_PRIVATE)


}













