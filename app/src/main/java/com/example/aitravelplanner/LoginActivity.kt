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

/** Activity che si occupa della visualizzazione e gestione del login tramite account Google.
 *
 * Questa è la prima schermata che viene visualizzata dall'utente al momento dell'apertura dell'applicazione.
 */
class LoginActivity : AppCompatActivity() {
    private val userRepo = UserRepository.getInstance()
    private lateinit var auth: FirebaseAuth
    private lateinit var signInClient: SignInClient
    private lateinit var progressBar: ProgressBar

    // Inizializza il launcher per il risultato dell'Intent di accesso
    private val signInLauncher = registerForActivityResult(StartIntentSenderForResult()) { result ->
        handleSignInResult(result.data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        signInClient = Identity.getSignInClient(this)

        progressBar = findViewById(R.id.progressBarLogin)
        // Inizializza Firebase Auth
        auth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser == null) {
            oneTapSignIn()
        } else {
            val intent: Intent = Intent(this, MainActivity::class.java)
            lifecycleScope.launch {
                progressBar.visibility = View.VISIBLE
                val dbUser = userRepo.getUserById(currentUser.uid, true)
                handleUserInitialization(dbUser, currentUser, intent)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Controlla se l'utente è autenticato e aggiorna l'UI di conseguenza
        val currentUser = auth.currentUser
    }

    /**
     * Gestisce il risultato del tentativo di accesso con Google.
     * Viene chiamata dopo che l'utente ha completato il flusso di accesso di Google.
     * Se l'accesso è riuscito, autentica l'utente con Firebase utilizzando il token ID di Google.
     * Se l'accesso fallisce, mostra un messaggio di errore.
     *
     */
    private fun handleSignInResult(data: Intent?) {
        // Risultato restituito dal lancio del PendingIntent di accesso
        try {
            // Accesso Google riuscito, autenticazione con Firebase
            val credential = signInClient.getSignInCredentialFromIntent(data)
            val rootView: View = findViewById(android.R.id.content)
            Snackbar.make(rootView, "Autenticazione riuscita.", Snackbar.LENGTH_SHORT).show()
            val idToken = credential.googleIdToken
            if (idToken != null) {
                Log.d(TAG, "firebaseAuthWithGoogle: ${credential.id}")
                firebaseAuthWithGoogle(idToken)
            } else {
                // Non dovrebbe accadere
                Log.d(TAG, "Nessun token ID!")
            }
        } catch (e: ApiException) {
            // Accesso Google fallito, aggiorna l'UI di conseguenza
            Log.w(TAG, "Accesso Google fallito", e)
            val rootView: View = findViewById(android.R.id.content)
            Snackbar.make(rootView, "Autenticazione fallita.", Snackbar.LENGTH_SHORT).show()
        }
    }

    /**
     * Autentica l'utente con Firebase utilizzando il token ID di Google.
     * Viene chiamata dopo che l'utente ha completato con successo l'accesso con Google.
     * Se l'autenticazione è riuscita, avvia MainActivity e aggiorna l'UI con le informazioni dell'utente.
     * Se l'autenticazione fallisce, mostra un messaggio di errore.
     *
     */
    private fun firebaseAuthWithGoogle(idToken: String) {
        progressBar.visibility = View.VISIBLE
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val intent: Intent = Intent(this, MainActivity::class.java)
                lifecycleScope.launch {
                    // Accesso riuscito, aggiorna l'UI con le informazioni dell'utente autenticato
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    val dbUser = userRepo.getUserById(user!!.uid, true)
                    handleUserInitialization(dbUser, user, intent)
                }
            } else {
                // Se l'accesso fallisce, mostra un messaggio all'utente
                Log.w(TAG, "signInWithCredential:failure", task.exception)
                val rootView: View = findViewById(android.R.id.content)
                Snackbar.make(rootView, "Autenticazione fallita.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Gestisce l'inizializzazione dell'utente dopo l'accesso.
     * Se l'utente esiste nel database ed è già inizializzato, salta la selezione degli interessi.
     * Altrimenti, crea un nuovo utente e richiede la selezione degli interessi.
     *
     */
    private fun handleUserInitialization(dbUser: User?, user: FirebaseUser, intent: Intent) {
        if (dbUser != null && dbUser.isInitialized) {
            // Evita la selezione degli interessi
            val b = Bundle()
            b.putSerializable("user", dbUser)
            dbUser.likedTravels!!.clear()
            b.putBoolean("isInit", true)
            intent.putExtras(b)
            progressBar.visibility = View.GONE
            startActivity(intent)
            finish()
        } else {
            // Crea un nuovo utente
            val newUser = User(user.uid, user.email!!, user.displayName!!, false, null, null)
            val b = Bundle()
            b.putSerializable("user", newUser)
            b.putBoolean("isInit", false)
            intent.putExtras(b)
            progressBar.visibility = View.GONE
            startActivity(intent)
            finish()
        }
    }

    /**
     * Avvia il processo di accesso tramite Google quando l'utente fa clic su un pulsante di accesso.
     * Richiede un intent di accesso a Google e lo lancia per iniziare il processo di autenticazione.
     *
     */
    fun signIn(view: View) {
        val signInRequest = GetSignInIntentRequest.builder()
            .setServerClientId(getString(R.string.web_client_id))
            .build()
        signInClient.getSignInIntent(signInRequest)
            .addOnSuccessListener { pendingIntent ->
                launchSignIn(pendingIntent)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Accesso Google fallito", e)
            }
    }

    /**
     * Avvia il processo di accesso One Tap di Google per l'utente.
     * Configura e mostra l'interfaccia utente di One Tap per l'accesso o la registrazione.
     */
    private fun oneTapSignIn() {
        // Configura l'interfaccia utente One Tap
        val oneTapRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.web_client_id))
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .build()
        // Mostra l'interfaccia utente One Tap
        signInClient.beginSignIn(oneTapRequest)
            .addOnSuccessListener { result ->
                launchSignIn(result.pendingIntent)
            }
            .addOnFailureListener { e ->
                // Nessuna credenziale salvata trovata. Avvia il flusso di registrazione One Tap o
                // non fare nulla e continua a presentare l'interfaccia utente disconnessa.
            }
    }

    /**
     * Avvia l'accesso utilizzando l'intent fornito.
     * Questo metodo viene utilizzato per avviare l'intent per l'accesso One Tap.
     *
     */
    private fun launchSignIn(pendingIntent: PendingIntent) {
        try {
            val intentSenderRequest = IntentSenderRequest.Builder(pendingIntent)
                .build()
            signInLauncher.launch(intentSenderRequest)
        } catch (e: IntentSender.SendIntentException) {
            Log.e(TAG, "Impossibile avviare l'accesso: ${e.localizedMessage}")
        }
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }
}
