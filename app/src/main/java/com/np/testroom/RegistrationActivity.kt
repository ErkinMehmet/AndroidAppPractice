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

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val editTextName = findViewById<EditText>(R.id.editTextName)
        val editTextAge = findViewById<EditText>(R.id.editTextAge)
        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTextProfession = findViewById<EditText>(R.id.editTextProfession)
        val editTextDescription = findViewById<EditText>(R.id.editTextDescription)
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

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (age == null || age <= 0) {
                Toast.makeText(this, "Please enter a valid age", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val passwordHash = commonFuncs.hashPassword(password)

            val user = User(
                name = name,
                age = age,
                username = username,
                passwordHash = passwordHash,
                profession = profession,
                description = description
            )

            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(applicationContext)
                val userDao = db.userDao()
                userDao.insert(user)
                Toast.makeText(this@RegistrationActivity, "Registration successful", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
