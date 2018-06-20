package com.jwhh.notekeeper

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceScreen
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.*
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_account_toolbar.*


class SettingsFragment : PreferenceFragment(),
        Preference.OnPreferenceClickListener,
        Preference.OnPreferenceChangeListener
{

    private val TAG = "SettingsFragment"

    private var iItems: IItems? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_main)

        // Set Preference Change Listeners
        val accountPreference: Preference = preferenceManager.findPreference(getString(R.string.key_account_settings))
        accountPreference.setOnPreferenceClickListener {onPreferenceClick(it)}

        val galleryNamePreference: Preference = preferenceManager.findPreference(getString(R.string.key_gallery_name))
        galleryNamePreference.setOnPreferenceChangeListener(this)

        val uploadWifiPreference: Preference = preferenceManager.findPreference(getString(R.string.key_upload_over_wifi))
        uploadWifiPreference.setOnPreferenceChangeListener(this)

        val notificationsRingtonePreference: Preference = preferenceManager.findPreference(getString(R.string.key_notifications_new_message_ringtone))
        notificationsRingtonePreference.setOnPreferenceChangeListener(this)

        val vibratePreference: Preference = preferenceManager.findPreference(getString(R.string.key_vibrate))
        vibratePreference.setOnPreferenceChangeListener(this)

        val backupFrequencyPreference: Preference = preferenceManager.findPreference(getString(R.string.key_backup_frequency))
        backupFrequencyPreference.setOnPreferenceChangeListener(this)
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

    override fun onPreferenceChange(preference: Preference?, key: Any?): Boolean {
        printToLog("Preference change detected")
        when(key){
            getString(R.string.key_gallery_name) -> updatePreferenceSuccess(getString(R.string.key_gallery_name))
            getString(R.string.key_upload_over_wifi) -> updatePreferenceSuccess(getString(R.string.key_upload_over_wifi))
            getString(R.string.key_notifications_new_message_ringtone) -> updatePreferenceSuccess(getString(R.string.key_notifications_new_message_ringtone))
            getString(R.string.key_vibrate) -> updatePreferenceSuccess(getString(R.string.key_vibrate))
            getString(R.string.key_backup_frequency) -> updatePreferenceSuccess(getString(R.string.key_backup_frequency))
        }

        return true // Update the state of the preference with the new value
    }

    fun updatePreferenceSuccess(key: String?){

        // If this was a real application we would send the updates to server here
        uploadPreferencesToServer()

        printToLog("successfully updated preferences. key: " + key)

    }

    private fun uploadPreferencesToServer(){
        // Code for uploading updated preferences to server
    }

    override fun onPreferenceClick(preference: Preference?): Boolean {

        if(preference!!.key.equals(getString(R.string.key_account_settings))){
            iItems!!.inflateAccountFragment()
        }

        return true
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


















