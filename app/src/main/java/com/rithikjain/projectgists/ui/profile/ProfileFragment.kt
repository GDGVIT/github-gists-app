package com.rithikjain.projectgists.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.rithikjain.projectgists.R
import com.rithikjain.projectgists.ui.auth.AuthActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.android.viewmodel.ext.android.sharedViewModel


class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileViewModel by sharedViewModel<ProfileViewModel>()

        profileToolbar.title = "My Profile"
        profileToolbar.navigationIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_back)
        profileToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val auth = FirebaseAuth.getInstance()
        val imgUrl = auth.currentUser!!.photoUrl
        val name = auth.currentUser!!.displayName
        val email = auth.currentUser!!.email

        Glide
            .with(this)
            .load(imgUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_github_logo)
            .into(profileProfileImage)

        profileName.text = name
        profileEmail.text = email

        logOutBtn.setOnClickListener {
            auth.signOut()

            profileViewModel.deleteLocalGist()

            val intent = Intent(activity, AuthActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}
