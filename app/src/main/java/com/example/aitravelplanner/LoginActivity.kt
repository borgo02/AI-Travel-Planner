package com.example.aitravelplanner

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.aitravelplanner.data.model.User
import com.example.aitravelplanner.data.repository.user.UserRepository
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
    private val userRepo = UserRepository.getInstance();
    private lateinit var auth: FirebaseAuth
    private lateinit var signInClient: SignInClient

    private lateinit var progressBar: ProgressBar

    private val signInLauncher = registerForActivityResult(StartIntentSenderForResult()) { result ->
        handleSignInResult(result.data)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        signInClient = Identity.getSignInClient(this)

        progressBar = findViewById(R.id.progressBarLogin)
        // Initialize Firebase Auth
        auth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser == null) {
            oneTapSignIn()
        }
        else
        {
            val intent: Intent = Intent(this, MainActivity::class.java)
            lifecycleScope.launch {
                progressBar.visibility = View.VISIBLE
                val dbUser = userRepo.getUserById(currentUser.uid, true);
                handleUserInitialization(dbUser, currentUser, intent)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
    }

    // [START logout]
    private fun logout() {
        Firebase.auth.signOut()
    }
    // [END logout]

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
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        progressBar.visibility = View.VISIBLE
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent: Intent = Intent(this, MainActivity::class.java)
                    lifecycleScope.launch {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        val user = auth.currentUser
                        val dbUser = userRepo.getUserById(user!!.uid, true);
                        handleUserInitialization(dbUser, user, intent)
                    }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    val rootView: View = findViewById(android.R.id.content)
                    Snackbar.make(rootView, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                }
            }
    }

    private fun handleUserInitialization(dbUser: User?, user: FirebaseUser, intent: Intent)
    {
        if (dbUser != null && dbUser.isInitialized) {
            //avoid interest selection
            val b = Bundle()
            b.putSerializable("user", dbUser) //Your id
            dbUser.likedTravels!!.clear()
            b.putBoolean("isInit", true) //Your id
            intent.putExtras(b) //Put your id to your next Intent
            progressBar.visibility = View.GONE
            startActivity(intent)
            finish()
        }
        else
        {
            //create user
            val newUser = User(user.uid, user.email!!, user.displayName!!, false, null, null)
            val b = Bundle()
            b.putSerializable("user", newUser) //Your id
            b.putBoolean("isInit", false) //Your id
            intent.putExtras(b) //Put your id to your next Intent
            progressBar.visibility = View.GONE
            startActivity(intent)
            finish()
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