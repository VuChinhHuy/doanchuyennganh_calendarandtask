package com.example.calendarandtasks.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.calendarandtasks.MainActivity
import com.example.calendarandtasks.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class RegisterFragment:Fragment() {
    private lateinit var binding : FragmentSignUpBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private  var email  = ""
    private  var displayName  = ""
    private  var password = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnReturn.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
        binding.btnCreate.setOnClickListener {
            validatedInput()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    private fun validatedInput() {
         displayName = binding.displayName.text.toString().trim()
         email = binding.email.text.toString().trim()
         password = binding.password.text.toString().trim()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.email.error = "Invalid email format"
        }
        else if (TextUtils.isEmpty(password)){
            binding.password.error = "Please enter password"
        }
        else if(TextUtils.isEmpty(displayName)){
            binding.displayName.error = "Please enter display name"
        }
        else if (password.length < 6){
            binding.password.error = "Password must be at least 6 characters"
        }
        else{
            firebaseSignUp()
        }
    }

    private fun firebaseSignUp() {
        val db = Firebase.firestore
         firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
             userProfileChangeRequest {
                 this.displayName = displayName
             }
             val docReferencer = db.collection("users").document(it.user?.uid.toString())
             val userNew = hashMapOf(
                 "email" to email,
                 "displayName" to displayName,
             )
             docReferencer.set(userNew).addOnSuccessListener {
                 Toast.makeText(requireContext(),"Create success user",Toast.LENGTH_SHORT).show()
                 activity?.apply {
                     startActivity(Intent(requireContext(),MainActivity::class.java))
                     finish()
                 }
             }
         }
             .addOnFailureListener {e->
                 Toast.makeText(requireContext(),"Create fail user ${e}",Toast.LENGTH_SHORT).show()
             }
    }

}
