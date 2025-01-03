package com.np.testroom
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.np.testroom.R
import com.np.testroom.data.User
import com.np.testroom.data.AppDatabase
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import org.mindrot.jbcrypt.BCrypt
import com.np.testroom.utils.commonFuncs
import com.google.firebase.auth.FirebaseAuth

class RegistrationActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        firebaseAuth = FirebaseAuth.getInstance()

        val editTextName = findViewById<EditText>(R.id.editTextName)
        val editTextAge = findViewById<EditText>(R.id.editTextAge)
        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTextProfession = findViewById<EditText>(R.id.editTextProfession)
        val editTextDescription = findViewById<EditText>(R.id.editTextDescription)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)

        val buttonBackToLogin: Button = findViewById(R.id.buttonBackToLogin)
        buttonBackToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        buttonRegister.setOnClickListener {
            val name = editTextName.text.toString()
            val age = editTextAge.text.toString().toIntOrNull()
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()
            val profession = editTextProfession.text.toString()
            val description = editTextDescription.text.toString()
            val email = editTextEmail.text.toString()

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (age == null || age <= 0) {
                Toast.makeText(this, "Please enter a valid age", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                editTextPassword.error = "Password is required"
                return@setOnClickListener
            } else {
                editTextPassword.error = null
            }

            if (TextUtils.isEmpty(email)) {
                editTextEmail.error = "Email is required"
                return@setOnClickListener
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.error = "Invalid email format"
                return@setOnClickListener
            } else {
                editTextEmail.error = null
            }

            val passwordHash = commonFuncs.hashPassword(password)

            // Register user using Firebase Authentication
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Registration successful, now save additional user info to the local database
                        val user = User(
                            name = name,
                            age = age,
                            username = username,
                            email = email,
                            passwordHash = password,  // Ideally, you would store a hashed password locally too, but Firebase already handles secure password storage.
                            profession = profession,
                            description = description
                        )

                        // Save the additional user info to the Room database
                        lifecycleScope.launch {
                            val db = AppDatabase.getDatabase(applicationContext)
                            val userDao = db.userDao()
                            userDao.insert(user)

                            Toast.makeText(this@RegistrationActivity, "Registration successful", Toast.LENGTH_SHORT).show()

                            // Redirect to login screen after successful registration
                            val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        // If Firebase registration fails, show error
                        Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
