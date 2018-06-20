package com.jwhh.notekeeper


interface ICallback {

    /**
     * A simple callback interface
     * Returns NULL if process was successful
     * Returns @exception if process was NOT successful
     */
    fun done(exception: Exception)
}