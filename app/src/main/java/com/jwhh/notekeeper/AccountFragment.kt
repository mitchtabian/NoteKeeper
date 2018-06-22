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
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.layout_account_toolbar.*
import java.util.*


class AccountFragment : Fragment(),
        View.OnClickListener
{
    private val TAG = "AccountFragment"

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

    fun savePreferences(){

//        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
//        val editor: SharedPreferences.Editor = prefs.edit()
//        if(!input_name.text.toString().equals("")){
//            val name: String? = input_name.text.toString()
//            printToLog("saving name: " + name)
//            editor.putString(PREFERENCES_NAME, name)
//            editor.apply()
//
//        }



    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            iItems = (activity as ItemsActivity)
        }catch (e: ClassCastException){
            printToLog(e.message)
        }
    }

    fun View.hideKeyboard() {
        printToLog("closing keyboard")
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun showProgressBar(){
        save.visibility = View.INVISIBLE
        progress_bar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        progress_bar.visibility = View.INVISIBLE
        save.visibility = View.VISIBLE
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

    private fun printToLog(message: String?){
        Log.d(TAG, message)
    }
}















