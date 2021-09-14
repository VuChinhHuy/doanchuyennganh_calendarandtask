package com.example.calendarandtasks.ui.login

import android.content.Intent

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.fragment.app.commit
import com.example.calendarandtasks.MainActivity
import com.example.calendarandtasks.databinding.ActivityLoginBinding
import com.example.calendarandtasks.R
import com.example.calendarandtasks.util.contentView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger



class LoginActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "LoginActivity"
        private const val RC_SIGN_IN = 9001
    }

    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var auth: FirebaseAuth
    private val binding: ActivityLoginBinding by contentView(R.layout.activity_login)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Logger.addLogAdapter(AndroidLogAdapter())
        supportFragmentManager.beginTransaction()
//                ?.replace(R.id.set_fragment_login,LoginFragment())
//                ?.commit()
        supportFragmentManager.commit {
            setCustomAnimations(
                R.anim.slide_out,
                R.anim.topamination
            )
            replace(R.id.set_fragment_login, WelcomeFragment())

        }

        binding.btnSignUp.setOnClickListener {
            supportFragmentManager.beginTransaction()
//                ?.replace(R.id.set_fragment_login,LoginFragment())
//                ?.commit()
            supportFragmentManager.commit {
                setCustomAnimations(
                    R.anim.slide_out,
                    R.anim.topamination
                )
                replace(R.id.set_fragment_login, RegisterFragment())

            }
        }
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]


        binding.btnLogin.setOnClickListener {
            firebaseLogin()
        }

        binding.fabGoogle.setOnClickListener {
            signIn()
        }

    }

    /**
     * Login with firebase
     */
    private fun firebaseLogin() {
        val email = binding.email.text.toString().trim()
        val password = binding.password.text.toString().trim()
        // validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.email.error = "Invalid email format"
        } else if (TextUtils.isEmpty(password)) {
            binding.password.error = "Please enter password"
        } else {
            //Login
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                Toast.makeText(this, "Login success with", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
                .addOnFailureListener {
                    Toast.makeText(this, "Login fail $it", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    // Login with google
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // Result from google intent
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Logger.d("firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Logger.w("Google sign in failed" + e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Logger.d("signInWithCredential:success")
                    val user = auth.currentUser
                    createIDUserFireStore(user)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Logger.w("signInWithCredential:failure", task.exception)

                }
            }
    }
    // [END auth_with_google]

    private fun createIDUserFireStore(user: FirebaseUser?) {
        val db = Firebase.firestore
        db.collection("users").get().addOnSuccessListener {
            for (document in it) {
                if (document.id == user?.uid)
                    return@addOnSuccessListener
                else {
                    val docReferencer = db.collection("users").document(user?.uid.toString())
                    val userNew = hashMapOf(
                        "email" to user?.email,
                        "displayName" to user?.displayName,
                        "phoneNumber" to user?.phoneNumber,
                        "photoUrl" to user?.photoUrl.toString()
                    )
                    docReferencer.set(userNew).addOnSuccessListener {
                        Toast.makeText(this, "Welcome Calendar and Tasks", Toast.LENGTH_SHORT)
                            .show()
                    }
                        .addOnFailureListener {
                            Toast.makeText(this, "$it", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            }
        }

    }
}