package com.nibm.docschedule

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
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

            val email = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // CREATE USER IN FIREBASE AUTH
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        // SAVE USER DATA IN DATABASE (OPTIONAL)
                        val uid = auth.currentUser!!.uid
                        val userMap = HashMap<String, Any>()
                        userMap["email"] = email

                        FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(uid)
                            .setValue(userMap)

                        Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show()

                        // GO TO LOGIN
                        startActivity(Intent(this, LoginActivity::class.java))
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

        // ALREADY HAVE ACCOUNT → LOGIN
        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
