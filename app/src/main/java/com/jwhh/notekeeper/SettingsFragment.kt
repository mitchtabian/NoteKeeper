package com.jwhh.notekeeper

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceFragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*

class SettingsFragment: PreferenceFragment() {

    private val TAG = "SettingsFragment"

    private var iItems: IItems? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_main)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = super.onCreateView(inflater, container, savedInstanceState)
        view.setBackgroundColor(ContextCompat.getColor(activity, R.color.white))

        iItems!!.showSettingsAppBar()

        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(activity.findViewById(R.id.settings_toolbar) as Toolbar)
        val actionBar = activity.supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setTitle(R.string.fragment_settings)
        setHasOptionsMenu(true)

        return view
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        // Disable the 3 dots in the action bar
        val menuItem: MenuItem = menu!!.findItem(R.id.action_settings)
        menuItem.setVisible(false)
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if(item!!.itemId == android.R.id.home){
            iItems!!.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            iItems = (context as IItems)
        }catch (e: ClassCastException){
            printToLog(e.message)
        }
    }

    private fun printToLog(message: String?){
        Log.d(TAG, message)
    }
}