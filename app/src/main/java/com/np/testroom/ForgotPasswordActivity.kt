package com.np.testroom
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var resetPasswordButton: Button
    private lateinit var resultTextView: TextView
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        emailEditText = findViewById(R.id.emailEditText)
        resetPasswordButton = findViewById(R.id.resetPasswordButton)
        resultTextView = findViewById(R.id.resultForgotPwdTextView)
        firebaseAuth = FirebaseAuth.getInstance()

        // Handle reset password button click
        resetPasswordButton.setOnClickListener {
            val email = emailEditText.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this, "Veuillez saisir votre courriel.", Toast.LENGTH_SHORT).show()
            } else {
                sendPasswordResetEmail(email)
            }
        }

        val buttonBackToLogin: Button = findViewById(R.id.buttonBackToLogin)
        buttonBackToLogin.setOnClickListener {
            retourner()
        }
    }

    // Send password reset email
    private fun sendPasswordResetEmail(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    resultTextView.text = "Le courriel de changement de mot de passe a été envoyé. Veuillez vérifier votre boîte de réception."
                    Toast.makeText(this, "Courriel envoyé.", Toast.LENGTH_SHORT).show()
                } else {
                    resultTextView.text = "Erreur: ${task.exception?.message}"
                }
            }
    }

    private fun retourner(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }


}
