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

class LoginActivity : AppCompatActivity() {


    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var googleLoginIcon: ImageView
    private lateinit var facebookLoginIcon: ImageView
    private lateinit var appleLoginIcon: ImageView
    private lateinit var loginStatusTextView: TextView
    private lateinit var userRepository: UserRepository

    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("user_prefs", MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
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
            // Trigger Google Login
            loginStatusTextView.text = "Google Login clicked"
            // Add logic for Google login here
        }

        // Handling the Facebook Login click (You can add the actual Facebook login logic here)
        facebookLoginIcon.setOnClickListener {
            // Trigger Facebook Login
            loginStatusTextView.text = "Facebook Login clicked"
            // Add logic for Facebook login here
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
    private fun loginUser(username: String, password: String) {

        CoroutineScope(Dispatchers.IO).launch {
            val user = userRepository.getUserByUsername(username)
            if (user != null && commonFuncs.checkPassword(password, user.passwordHash)) {
                // Save the login state in SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putBoolean("isLoggedIn", true)
                editor.putString("username", username)
                editor.putInt("userId",user.id.toInt())
                editor.apply()

                // Navigate to the home screen or main activity
                navigateToHome()
            } else {
                loginStatusTextView.text = "Nom d'utilisateur ou mot de passe erroné."
            }

        }
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
}
