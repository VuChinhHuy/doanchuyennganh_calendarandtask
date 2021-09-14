package com.example.calendarandtasks.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.calendarandtasks.R
import com.example.calendarandtasks.databinding.FragmentProfileBinding
import com.example.calendarandtasks.ui.login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment:Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var user :FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        user = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val currentUser = user.currentUser
        if (currentUser!= null){
            binding.fullName.text = currentUser.displayName
            binding.email.text = currentUser.email
            binding.numberPhone.text = currentUser.phoneNumber
            val avtUser = if(currentUser.photoUrl != null) currentUser.photoUrl else R.mipmap.ic_launcher
            Glide.with(this).load(avtUser).into(binding.imageAvt)
        }
        binding.btnLogout.setOnClickListener{
            GoogleSignIn.getClient(requireActivity(), GoogleSignInOptions.Builder(
                    GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut().addOnSuccessListener {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(requireActivity(),LoginActivity::class.java))
                    activity?.finish()
                }.addOnFailureListener {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(requireActivity(),LoginActivity::class.java))
                    activity?.finish()
                }
        }
        binding.btnEditProfile.setOnClickListener{
            Toast.makeText(requireContext(),"This is developing",Toast.LENGTH_SHORT).show()
        }

    }
}