package com.nibm.docschedule.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.nibm.docschedule.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnSignup.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            // -----------------------------
            // 1️⃣ Validation
            // -----------------------------
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (username.length < 3) {
                Toast.makeText(this, "Username must be at least 3 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // -----------------------------
            // 2️⃣ Create user in Firebase Auth
            // -----------------------------
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val user = auth.currentUser

                        // -----------------------------
                        // 3️⃣ Set Firebase Auth displayName
                        // -----------------------------
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build()
                        user?.updateProfile(profileUpdates)

                        // -----------------------------
                        // 4️⃣ Save user in Realtime Database
                        // -----------------------------
                        val uid = user!!.uid
                        val userMap = HashMap<String, Any>()
                        userMap["username"] = username
                        userMap["email"] = email

                        FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(uid)
                            .setValue(userMap)

                        Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show()

                        // Go to IntroActivity
                        startActivity(
                            Intent(this, com.nibm.docschedule.ui.intro.IntroActivity::class.java)
                        )
                        finish()

                    } else {
                        Toast.makeText(
                            this,
                            task.exception?.message ?: "Signup failed",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        // Already have account → Login
        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
