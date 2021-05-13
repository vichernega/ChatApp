package com.example.chatapp.view

import android.Manifest
import android.app.Dialog
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.chatapp.R
import com.example.chatapp.`object`.ImageUtilits
import com.example.chatapp.`object`.UserObject
import com.example.chatapp.databinding.DialogChooseImageActionBinding
import com.example.chatapp.utilits.APP_ACTIVITY
import com.example.chatapp.utilits.PERMISSION_CODE_CAMERA_IMAGE
import com.example.chatapp.utilits.PERMISSION_CODE_GALLERY_IMAGE
import com.example.chatapp.utilits.TAG_SET_PROFILE_IMAGE
import com.example.chatapp.viewmodel.ChatViewModel
import com.example.chatapp.viewmodel.SettingsViewModel


class ChooseImageActionDialogFragment: DialogFragment(R.layout.dialog_choose_image_action) {

    private lateinit var binding: DialogChooseImageActionBinding            // viewBinding variable
    private val settingsViewModel: SettingsViewModel by activityViewModels()
    private val chatViewModel: ChatViewModel by activityViewModels()
    var imageUri: Uri? = null

    /**getting permission to use camera*/
    val cameraPermission = registerForActivityResult(RequestPermission()){ granted ->
        when{
            granted -> cameraPicture.run()      // if permission is granted use camera
            else ->
                // handle permission for devices with marshmallow and above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    APP_ACTIVITY.requestPermissions(arrayOf(Manifest.permission.CAMERA),
                        PERMISSION_CODE_CAMERA_IMAGE)
                } else {
                    cameraPicture.run()
                }
        }
    }

    /**create image file from camera*/
    val cameraPicture: Runnable = Runnable {
        ImageUtilits.createImageFile(APP_ACTIVITY)?.also {
            imageUri = FileProvider.getUriForFile(APP_ACTIVITY, "com.example.chatapp.fileprovider", it)
            cameraTakePicture.launch(imageUri)
        }
    }

    /**taking picture with camera*/
    val cameraTakePicture = registerForActivityResult(TakePicture()){ success ->
        if (success){

            if (this.tag == TAG_SET_PROFILE_IMAGE){
                UserObject.image = imageUri.toString()                                   // save image to local User
                Log.d("IMAGE", "IN PROFILE IMAGE CHOOSE")
                imageUri?.let { it -> settingsViewModel.saveUserImage(it) }      // change viewModel liveData
            }
            else {
                Log.d("IMAGE", "IN MESSAGE IMAGE CHOOSE")
                imageUri?.let { it -> chatViewModel.sendImage(it) }      //
            }


            Log.d("IMAGE", "Success")
            Log.d("IMAGE", imageUri.toString())
            Log.d("IMAGE", UserObject.image.toString())
            dismiss()           // close dialog
        } else {
            Log.d("IMAGE", "Task to make a photo is failed")
        }
    }

    /**getting permission to use external storage*/
    val galleryPermission = registerForActivityResult(RequestPermission()) { granted ->
        when{
            granted -> takePictureFromGallery.launch("image/*")         // if permission is granted use gallery
            else ->
                // handle permission for devices with marshmallow and above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    APP_ACTIVITY.requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PERMISSION_CODE_GALLERY_IMAGE)
                } else {
                    takePictureFromGallery.launch("image/*")
                }
        }
    }

    /** get existing picture from gallery*/
    val takePictureFromGallery = registerForActivityResult(GetContent()) {
        if (it != null) {
            if (this.tag == TAG_SET_PROFILE_IMAGE){
                UserObject.image = it.toString()                 // save image to local User
                settingsViewModel.saveUserImage(it)      // change viewModel liveData
                Log.d("IMAGE", "IN PROFILE IMAGE CHOOSE")
            } else {
                Log.d("IMAGE", "IN MESSAGE IMAGE CHOOSE")
                chatViewModel.sendImage(it)      //
            }

            Log.d("IMAGE", "uri val in choose image ---- " + it.toString())
            Log.d("IMAGE", "userImage val in choose image ---- " + UserObject.image.toString())
            dismiss()               //close dialog
        } else {
            Log.d("IMAGE", "Task to get photo from gallery is failed")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            binding = DialogChooseImageActionBinding.inflate(layoutInflater)        // binding initialization

            val builder = AlertDialog.Builder(it)
            builder.setView(binding.root)

            // Take photo on click listener
            binding.takePhotoContainer.setOnClickListener {
                // get camera permission -> make photo
                cameraPermission.launch(Manifest.permission.CAMERA)
            }

            // Get from gallery on click listener
            binding.photoFromGalleryContainer.setOnClickListener {
                // get gallery permission -> get existing photo
                galleryPermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

