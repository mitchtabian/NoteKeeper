package com.jwhh.notekeeper

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.preference.PreferenceManager

import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jwhh.notekeeper.PreferenceHelper.get
import com.jwhh.notekeeper.PreferenceHelper.set
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.layout_account_toolbar.*
import java.util.*


class AccountFragment : Fragment(),
        View.OnClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener
{
    private val TAG = "AccountFragment"

    private var isRunning: Boolean = false
    private var selectedImageUri: Uri? = null
    private var permissions: Boolean = false
    private var iItems: IItems? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_account, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        input_phone_number.addTextChangedListener(PhoneNumberFormattingTextWatcher(Locale.getDefault().country))

        initToolbar()
        initWidgetValues()
        initPermissions()
        enablePhotoSelection()
    }

    private fun initPermissions(){
        if (!permissions) {
            verifyPermissions()
        }
    }

    private fun enablePhotoSelection(){
        change_photo.setOnClickListener(this)
        profile_image.setOnClickListener(this)
    }

    private fun initWidgetValues(){
        val prefs: SharedPreferences = PreferenceHelper.defaultPrefs(context!!)

        // if we don't use the "operator" keyword
//        prefs.set(PREFERENCES_NAME, "mitch")

        // Option 1: Specify the type in the declaration
        val name: String? = prefs[PREFERENCES_NAME]
        input_name.setText(name)

        // Option 2: Specify the type indirectly by setting the default value
        val username = prefs[PREFERENCES_USERNAME, ""]
        input_username.setText(username)

        val email: String? = prefs[PREFERENCES_EMAIL]
        input_email_address.setText(email)

        val phoneNumber: String? = prefs[PREFERENCES_PHONE_NUMBER]
        input_phone_number.setText(phoneNumber)

        val gender: String? = prefs[PREFERENCES_GENDER]
        if(gender.equals("")){
            gender_spinner.setSelection(0)
        }
        else{
            val genderArray = resources.getStringArray(R.array.gender_array)
            val genderIndex: Int = genderArray.indexOf(gender)
            gender_spinner.setSelection(genderIndex)
        }

        val profileImageUrl: String? = prefs[PREFERENCES_PROFILE_IMAGE]
        setProfileImage(profileImageUrl)
    }

    fun setProfileImage(url: String?){
        val requestOptions: RequestOptions = RequestOptions()
                .placeholder(R.mipmap.ic_launcher_round)

        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(url)
                .into(profile_image)
    }

    fun setImageUri(imageUri: Uri?) {
        if (imageUri.toString() != "") {
            selectedImageUri = imageUri
            printToLog("getImagePath: got the image uri: " + selectedImageUri)

            val requestOptions: RequestOptions = RequestOptions()
                    .placeholder(R.mipmap.ic_launcher_round)

            Glide.with(this)
                    .setDefaultRequestOptions(requestOptions)
                    .load(selectedImageUri)
                    .into(profile_image)
        }
    }

    private fun savePreferences(){
        view!!.hideKeyboard()

        val prefs: SharedPreferences = PreferenceHelper.defaultPrefs(context!!)

        if(!input_name.text.toString().equals("")){
            val name: String? = input_name.text.toString()
            printToLog("saving name: " + name)
            prefs[PREFERENCES_NAME] = name
        }

        if(!input_username.text.toString().equals("")){
            val username: String? = input_username.text.toString().replace(" ", ".")
            printToLog("saving username: " + username)
            prefs[PREFERENCES_USERNAME] = username
            input_username.setText(username) // fix the username being displayed if necessary
        }

        if(!input_phone_number.text.toString().equals("")){
            val phoneNumber: String = removeNumberFormatting(input_phone_number.text.toString())
            printToLog("saving phone number: " + phoneNumber)
            prefs[PREFERENCES_PHONE_NUMBER] = phoneNumber
        }

        if(!input_email_address.text.toString().equals("")){
            val email: String? = input_email_address.text.toString()
            printToLog("saving email address: " + email)
            prefs[PREFERENCES_EMAIL] = email
        }

        if(!gender_spinner.selectedItem.toString().equals("")){
            val gender: String? = gender_spinner.selectedItem.toString()
            printToLog("saving gender: " + gender)
            prefs[PREFERENCES_GENDER] = gender
        }

        if(selectedImageUri != null){
            prefs[PREFERENCES_PROFILE_IMAGE] = selectedImageUri.toString()
        }
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, key: String?) {
        when(key){
            PREFERENCES_NAME -> updatePreferenceSuccess(PREFERENCES_NAME)
            PREFERENCES_USERNAME -> updatePreferenceSuccess(PREFERENCES_USERNAME)
            PREFERENCES_PHONE_NUMBER -> updatePreferenceSuccess(PREFERENCES_PHONE_NUMBER)
            PREFERENCES_EMAIL -> updatePreferenceSuccess(PREFERENCES_EMAIL)
            PREFERENCES_GENDER -> updatePreferenceSuccess(PREFERENCES_GENDER)
            PREFERENCES_PROFILE_IMAGE -> updatePreferenceSuccess(PREFERENCES_PROFILE_IMAGE)
        }
    }

    fun updatePreferenceSuccess(key: String?){
        showProgressBar()

        // Simulate uploading the new data to server
        if(!isRunning){

            // If this was a real application we would send the updates to server here
            simulateUploadToServer()

            Snackbar.make(view!!, "sending updates to server", Snackbar.LENGTH_SHORT).show()
            printToLog("successfully updated shared preferences. key: " + key)
        }
    }

    private fun simulateUploadToServer(){
        val handler = Handler(Looper.getMainLooper())
        val start: Long = System.currentTimeMillis()
        val runnable: Runnable = object: Runnable{
            override fun run() {
                handler.postDelayed(this, 100)
                isRunning = true
                val now: Long = System.currentTimeMillis()
                val difference: Long = now - start
                if(difference >= 1000){
                    printToLog("update finished")
                    updateFinished()
                    handler.removeCallbacks(this)
                    isRunning = false
                }
            }
        }
        activity!!.runOnUiThread(runnable)
    }

    private fun inflateChangePhotoDialog(){
        if(permissions){
            val dialog = ChangePhotoDialog()
            val fm = activity!!.supportFragmentManager
            dialog.show(fm, getString(R.string.dialog_change_photo))
        }
        else{
            verifyPermissions()
        }

    }

    override fun onClick(widget: View?) {
        when(widget?.id){

            R.id.close -> iItems!!.onBackPressed()

            R.id.save -> savePreferences()

            R.id.profile_image -> inflateChangePhotoDialog()

            R.id.change_photo -> inflateChangePhotoDialog()
        }

    }

    override fun onAttachFragment(childFragment: Fragment?) {
        super.onAttachFragment(childFragment)
        try {
            iItems = (activity as ItemsActivity)
        }catch (e: ClassCastException){
            printToLog(e.message)
        }
    }

    private fun updateFinished(){
        hideProgressBar()
        selectedImageUri = null
    }

    private fun showProgressBar(){
        save.visibility = View.INVISIBLE
        progress_bar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        progress_bar.visibility = View.INVISIBLE
        save.visibility = View.VISIBLE
    }

    fun View.hideKeyboard() {
        printToLog("closing keyboard")
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun initToolbar() {
        close.setOnClickListener(this)
        save.setOnClickListener(this)
    }

    fun verifyPermissions() {
        Log.d(TAG, "verifyPermissions: asking user for permissions.")
        val permissionsArray = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        if (ContextCompat.checkSelfPermission(this.context!!,
                permissionsArray[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.context!!,
                permissionsArray[1]) == PackageManager.PERMISSION_GRANTED){
            permissions = true
        } else {
            ActivityCompat.requestPermissions(
                    activity!!,
                    permissionsArray,
                    PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun removeNumberFormatting(number: String): String{
        val regex = Regex("[^0-9]")
        return regex.replace(number, "")
    }

    override fun onResume() {
        super.onResume()
        PreferenceHelper.defaultPrefs(context!!).registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        PreferenceHelper.defaultPrefs(context!!).unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun printToLog(message: String?){
        Log.d(TAG, message)
    }
}















