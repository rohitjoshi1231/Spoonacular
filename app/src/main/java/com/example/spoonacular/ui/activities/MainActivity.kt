package com.example.spoonacular.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.spoonacular.R
import com.example.spoonacular.api.ApiInterface
import com.example.spoonacular.api.ApiUtilities
import com.example.spoonacular.databinding.ActivityMainBinding
import com.example.spoonacular.model.GoogleSignInDetails
import com.example.spoonacular.repository.SpoonacularRepository
import com.example.spoonacular.viewmodel.RecipeViewModel
import com.example.spoonacular.viewmodel.RecipeViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "Rohit"
        const val RC_SIGN_IN = 9001
        const val API_KEY = "c9acda6949de4999a95f10ca2962f0cb"
    }

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var binding: ActivityMainBinding
    private lateinit var recipeViewModel: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiInterface = ApiUtilities.getInstance().create(ApiInterface::class.java)
        val spoonacularRepository = SpoonacularRepository(apiInterface)
        recipeViewModel = ViewModelProvider(
            this, RecipeViewModelFactory(spoonacularRepository)
        )[RecipeViewModel::class.java]


        // Check if user is already signed in
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null && auth.currentUser?.uid != null) {
            // User is already signed in, navigate to HomeScreenActivity
            navigateToHomeScreen()
        } else {
            binding.googleLogin.setOnClickListener {
                googleSignIn()
            }

        }


        recipeViewModel.similarRecipe.observe(this) {
            Log.d(TAG, "onCreate: $it")
        }
    }

    private fun navigateToHomeScreen() {
        startActivity(Intent(this, HomeScreenActivity::class.java))
    }

    private fun googleSignIn() {
        val clientId = getString(R.string.default_web_client_id)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(clientId).requestEmail().build()
        val gsoSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = gsoSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                signInGoogle(account = account) {
                    if (it) {
                        startActivity(Intent(this, HomeScreenActivity::class.java))
                        Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: ApiException) {
                // Google Sign In failed, update UI accordingly
                println("Api Exception Welcome Activity: $e")
                Toast.makeText(this, "Error: $e", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun signInGoogle(account: GoogleSignInAccount?, callback: (Boolean) -> Unit) {
        val db = Firebase.firestore
        if (account != null) {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val firebaseUserId = auth.currentUser?.uid
                    if (firebaseUserId != null) {
                        val displayName = user?.displayName
                        val googleId = account.id
                        val fullName = account.displayName
                        val email = account.email
                        val photoUrl = account.photoUrl

                        val signInData = GoogleSignInDetails(
                            firebaseUserId,
                            displayName ?: "",
                            googleId ?: "",
                            fullName ?: "",
                            email ?: "",
                            photoUrl?.toString() ?: "",
                        )

                        // Check if user data already exists
                        db.collection("Login Details").document(firebaseUserId).get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    // User data already exists, no need to add it again
                                    callback(true)
                                } else {
                                    // User data does not exist, add new data
                                    db.collection("Login Details").document(firebaseUserId)
                                        .set(signInData).addOnSuccessListener {
                                            callback(true)
                                        }.addOnFailureListener {
                                            callback(false)
                                        }
                                }
                            }.addOnFailureListener {
                                // Handle potential errors, e.g., network issues
                                callback(false)
                            }
                    }
                } else {
                    // Sign-in failed
                    callback(false)
                }
            }
        } else {
            // Account is null
            callback(false)
        }
    }
}
