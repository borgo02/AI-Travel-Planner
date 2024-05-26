package com.example.aitravelplanner

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.aitravelplanner.data.repository.user.UserRepository
import com.example.aitravelplanner.ui.interests.InterestsFragment
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
    private var showOneTapUI = true
    private val userRepo = UserRepository.getInstance();
    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    private lateinit var signInClient: SignInClient

    private val signInLauncher = registerForActivityResult(StartIntentSenderForResult()) { result ->
        handleSignInResult(result.data)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        signInClient = Identity.getSignInClient(this)

        // Initialize Firebase Auth
        auth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser == null) {
            oneTapSignIn()
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    // [START logout]
    private fun logout() {
        Firebase.auth.signOut()
        updateUI(null)
    }
    // [END logout]

    private fun updateUI(user: FirebaseUser?) {
    }

    private fun handleSignInResult(data: Intent?) {
        // Result returned from launching the Sign In PendingIntent
        try {
            // Google Sign In was successful, authenticate with Firebase
            val credential = signInClient.getSignInCredentialFromIntent(data)

            val rootView: View = findViewById(android.R.id.content)
            Snackbar.make(rootView, "Authentication Succed.", Snackbar.LENGTH_SHORT).show();
            val idToken = credential.googleIdToken
            if (idToken != null) {
                Log.d(TAG, "firebaseAuthWithGoogle: ${credential.id}")
                firebaseAuthWithGoogle(idToken)
            } else {
                // Shouldn't happen.
                Log.d(TAG, "No ID token!")
            }
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Log.w(TAG, "Google sign in failed", e)
            val rootView: View = findViewById(android.R.id.content)
            Snackbar.make(rootView, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
            updateUI(null)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        //showProgressBar()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent: Intent = Intent(this, MainActivity::class.java)
                    lifecycleScope.launch {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        val user = auth.currentUser
                        var dbUser = userRepo.getUserById(user!!.uid, true);
                        if (dbUser != null && dbUser.isInitialized) {
                            //avoid interest selection
                            val b = Bundle()
                            b.putSerializable("user", dbUser) //Your id
                            b.putBoolean("isInit", true) //Your id
                            intent.putExtras(b) //Put your id to your next Intent
                            startActivity(intent)
                            finish()
                        }
                        else
                        {
                            //create user
                            dbUser = com.example.aitravelplanner.data.model.User(user.uid, user.email!!, user.displayName!!, false, null, null)
                            val b = Bundle()
                            b.putSerializable("user", dbUser) //Your id
                            b.putBoolean("isInit", false) //Your id
                            intent.putExtras(b) //Put your id to your next Intent
                            startActivity(intent)
                            finish()
                        }
                        updateUI(user)
                    }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    val rootView: View = findViewById(android.R.id.content)
                    Snackbar.make(rootView, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                    updateUI(null)
                }

                //hideProgressBar()
            }
    }

    public fun signIn(view: View) {
        val signInRequest = GetSignInIntentRequest.builder()
            .setServerClientId(getString(R.string.web_client_id))
            .build()

        signInClient.getSignInIntent(signInRequest)
            .addOnSuccessListener { pendingIntent ->
                launchSignIn(pendingIntent)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Google Sign-in failed", e)
            }
    }

    private fun oneTapSignIn() {
        // Configure One Tap UI
        val oneTapRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.web_client_id))
                    .setFilterByAuthorizedAccounts(true)
                    .build(),
            )
            .build()

        // Display the One Tap UI
        signInClient.beginSignIn(oneTapRequest)
            .addOnSuccessListener { result ->
                launchSignIn(result.pendingIntent)
            }
            .addOnFailureListener { e ->
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
            }
    }

    private fun launchSignIn(pendingIntent: PendingIntent) {
        try {
            val intentSenderRequest = IntentSenderRequest.Builder(pendingIntent)
                .build()
            signInLauncher.launch(intentSenderRequest)
        } catch (e: IntentSender.SendIntentException) {
            Log.e(TAG, "Couldn't start Sign In: ${e.localizedMessage}")
        }
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }
}