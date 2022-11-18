package com.dewan.todoapp.view.ui.profile.edit

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.dewan.todoapp.R
import com.dewan.todoapp.viewmodel.profile.edit.EditProfileViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.edit_profile_fragment.*
import org.jetbrains.anko.support.v4.alert
import java.io.File

class EditProfileFragment : Fragment() {

    companion object {
        const val TAG = "EditProfileFragment"
    }

    private lateinit var viewModel: EditProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.edit_profile_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditProfileViewModel::class.java)

        //get the ars
        val args = EditProfileFragmentArgs.fromBundle(arguments!!)
        viewModel.nameField.value = args.name
        viewModel.emailField.value = args.email
        viewModel.biolField.value = args.bio
        viewModel.imageUrl.value = args.imageUrl

        //observe live data
        observe()

        editProfileNewImage.setOnClickListener {
            pickImage()
        }

        fab_submit_profile.setOnClickListener {
            viewModel.nameField.value = editProfileName.text.toString()
            viewModel.biolField.value = editProfileBio.text.toString()
            viewModel.emailField.value = editProfileEmail.text.toString()

            viewModel.editProfile().observe(viewLifecycleOwner, Observer {

            })
        }



    }

    private fun observe(){
        viewModel.nameField.observe(viewLifecycleOwner, Observer {
            editProfileName.setText(it)
        })
        viewModel.emailField.observe(viewLifecycleOwner, Observer {
            editProfileEmail.setText(it)
        })
        viewModel.biolField.observe(viewLifecycleOwner, Observer {
            editProfileBio.setText(it)
        })
        viewModel.imageUrl.observe(viewLifecycleOwner, Observer {
            Glide.with(this)
                .load(it)
                .circleCrop()
                .into(editProfileImage)
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer {
            pb_edit_profile.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.profile.observe(viewLifecycleOwner, Observer {
            val isSuccess = it?.success
            isSuccess?.run {
                if (isSuccess){
                    successDialog()
                }
                else {
                    errorDialog()
                }
            }
        })

    }

    private fun pickImage(){
        ImagePicker.with(this)
            .crop(1f, 1f)               //Crop Square image(Optional)
            .compress(1024)         //Final image size will be less than 1 MB(Optional)
            .maxResultSize(400, 400)  //Final image resolution will be less than 1080 x 1080(Optional)
            .start { resultCode, data ->
                if (resultCode == Activity.RESULT_OK) {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data
                    Glide.with(this)
                        .load(fileUri)
                        .circleCrop()
                        .into(editProfileImage)


                    //You can get File object from intent
                    val file: File? = ImagePicker.getFile(data)
                    viewModel.imageField.value = file

                    //You can also get File Path from intent
                    val filePath:String? = ImagePicker.getFilePath(data)

                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun successDialog(){
        alert {
            isCancelable = false
            title = getString(R.string.alert_success_title)
            message = getString(R.string.alert_edit_success_msg)
            positiveButton("OK"){
                it.dismiss()
                findNavController().navigate(EditProfileFragmentDirections.actionEditProfileFragmentToNavigationHome())
            }
        }.show()
    }

    private fun errorDialog(){
        alert {
            isCancelable = false
            title = getString(R.string.alert_error_title)
            message = getString(R.string.alert_error_msg)
            positiveButton("OK"){
                it.dismiss()
            }
        }.show()
    }


}
