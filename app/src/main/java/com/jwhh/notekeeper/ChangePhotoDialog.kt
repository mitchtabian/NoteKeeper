package com.jwhh.notekeeper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ChangePhotoDialog: DialogFragment() {

    private val TAG = "ChangePhotoDialog"

    interface OnPhotoReceivedListener {
        fun setImageUri(imageUri: Uri?)
    }

    private var onPhotoReceived: OnPhotoReceivedListener? = null
    private var mCurrentPhotoPath: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_changephoto, container, false)

        //Initialize the textview for choosing an image from memory
        val selectPhoto = view.findViewById(R.id.dialogChoosePhoto) as TextView
        selectPhoto.setOnClickListener {
            Log.d(TAG, "onClick: accessing phones memory.")
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PICKFILE_REQUEST_CODE)
        }

        //Initialize the textview for choosing an image from memory
        val takePhoto = view.findViewById(R.id.dialogOpenCamera) as TextView
        takePhoto.setOnClickListener {
            Log.d(TAG, "onClick: starting camera")
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if (cameraIntent.resolveActivity(activity!!.packageManager) != null) {
                // Create the File where the photo should go
                var photoFile: File? = null
                try {
                    photoFile = createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Log.d(TAG, "onClick: error: " + ex.message)
                }

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    val photoURI = FileProvider.getUriForFile(activity!!,
                            "com.jwhh.notekeeper.fileprovider",
                            photoFile)
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
                }
            }
        }


        return view
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d(TAG,"PhotoReceivedListener: " + onPhotoReceived)
        /*
            Results when selecting new image from phone memory
         */
        if (requestCode == PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data!!.data
            Log.d(TAG, "onActivityResult: image: " + selectedImageUri!!)

            //send the bitmap and fragment to the interface
            onPhotoReceived!!.setImageUri(selectedImageUri)
            dialog.dismiss()

        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            Log.d(TAG, "onActivityResult: image uri: " + mCurrentPhotoPath!!)
            onPhotoReceived!!.setImageUri(Uri.fromFile(File(mCurrentPhotoPath!!)))
            dialog.dismiss()
        }
        else{
            dialog.dismiss()
        }
    }

    override fun onAttachFragment(childFragment: Fragment?) {
        super.onAttachFragment(childFragment)
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            onPhotoReceived = context as OnPhotoReceivedListener
        } catch (e: ClassCastException) {
            Log.e(TAG, "onAttach: ClassCastException", e.cause)
        }
    }
}












