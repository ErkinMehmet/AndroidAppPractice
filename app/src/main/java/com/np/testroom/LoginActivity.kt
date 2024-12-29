package com.np.testroom
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var loginStatusTextView: TextView

    // SharedPreferences to store login state
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("user_prefs", MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        loginStatusTextView = findViewById(R.id.loginStatusTextView)

        // Check if user is already logged in
        checkIfLoggedIn()

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
        // You can replace this logic with real authentication (e.g., API or Firebase)
        if (username == "user" && password == "password") {
            // Save the login state in SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putBoolean("isLoggedIn", true)
            editor.putString("username", username)
            editor.apply()

            // Navigate to the home screen or main activity
            navigateToHome()
        } else {
            loginStatusTextView.text = "Invalid username or password."
        }
    }

    // Navigate to the Home screen after successful login
    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish() // Finish the login activity so the user can't navigate back
    }
}
