package com.np.testroom
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.np.testroom.data.UserRepository
import com.np.testroom.utils.commonFuncs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FacebookAuthProvider
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInCredential
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.ActivityResultCallback


class LoginActivity : AppCompatActivity() {


    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var googleLoginIcon: ImageView
    private lateinit var facebookLoginIcon: ImageView
    private lateinit var appleLoginIcon: ImageView
    private lateinit var loginStatusTextView: TextView
    private lateinit var userRepository: UserRepository

    private val RC_SIGN_IN = 9001
    private lateinit var signInClient: SignInClient
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("user_prefs", MODE_PRIVATE)
    }
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuth = FirebaseAuth.getInstance()
        signInClient = Identity.getSignInClient(this)

        userRepository = UserRepository(this)
        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        googleLoginIcon = findViewById(R.id.googleLoginIcon)
        facebookLoginIcon = findViewById(R.id.facebookLoginIcon)
        appleLoginIcon = findViewById(R.id.appleLoginIcon)
        loginStatusTextView = findViewById(R.id.loginStatusTextView)

        // Check if user is already logged in
        checkIfLoggedIn()
        // Handling the Google Login click (You can add the actual Google login logic here)
        googleLoginIcon.setOnClickListener {
            startGoogleSignIn()
        }

        // Handling the Facebook Login click (You can add the actual Facebook login logic here)
        facebookLoginIcon.setOnClickListener {

        }

        // Handling the Apple Login click
        appleLoginIcon.setOnClickListener {
            loginStatusTextView.text = "Facebook Login clicked"
        }

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Basic validation
            if (username.isNotEmpty() && password.isNotEmpty()) {
                loginUser(username, password)
            } else {
                loginStatusTextView.text = "Please fill in both fields."
            }
        }
    }

    // Function to check if the user is already logged in
    private fun checkIfLoggedIn() {
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            navigateToHome()
        }
    }

    // Function to handle user login
    private fun loginUser(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, OnCompleteListener { task ->
                if (task.isSuccessful) {
                    val editor = sharedPreferences.edit()
                    val user = firebaseAuth.currentUser
                    CoroutineScope(Dispatchers.IO).launch {
                        val userDb = userRepository.getUserByEmail(email)
                        editor.putInt("userId",userDb?.id!!.toInt())
                    }
                    editor.putBoolean("isLoggedIn", true)
                    editor.putString("username", email)
                    editor.apply()

                    // Navigate to home screen
                    navigateToHome()
                } else {
                    // If login fails
                    loginStatusTextView.text = "Connexion échouée. Veuillez entrer de nouveau votre mot de passe et courriel."
                    Toast.makeText(this, "Connexion échouée.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun navigateToRegistrationPage(view: View) {
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
    }
    fun navigateToForgotPwd(view: View) {
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
    }

    private fun startGoogleSignIn() {

    }


}
