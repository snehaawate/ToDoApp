package com.dewan.todoapp.view.ui.profile


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide

import com.dewan.todoapp.R
import com.dewan.todoapp.viewmodel.profile.ProfileViewModel
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileFragment : Fragment() {

    companion object {
        const val TAG = "ProfileFragment"
    }

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        observer()


    }

    private fun observer(){
        viewModel.loading.observe(viewLifecycleOwner, Observer {
            pb_profile.visibility =  if (it) View.VISIBLE else View.GONE
        })

        viewModel.getUserProfile().observe(viewLifecycleOwner, Observer {
            profileName.text = it.name
            profileEmail.text = it.email
            profileBio.text = it.bio
            Glide.with(this)
                .load(it.profileImage)
                .circleCrop()
                .into(profileImage)

        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.profile_edit_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.navigation_edit_profile ->
                findNavController().navigate(ProfileFragmentDirections.actionNavigationProfileToEditProfileFragment(
                    profileName.text.toString(),
                    profileEmail.text.toString(),
                    profileBio.text.toString(),
                    viewModel.imageUrl.value.toString()
                ))
        }

        return false

    }


}
