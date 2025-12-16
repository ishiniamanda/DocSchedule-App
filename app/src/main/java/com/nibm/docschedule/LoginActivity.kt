package com.nibm.docschedule

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.nibm.docschedule.databinding.ActivityLoginBinding
import com.nibm.docschedule.ui.intro.IntroActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {

            val email = binding.etLoginUsername.text.toString().trim()
            val password = binding.etLoginPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // LOGIN USING FIREBASE AUTH
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                        // GO TO INTRO SCREEN
                        startActivity(Intent(this, IntroActivity::class.java))
                        finish()

                    } else {
                        Toast.makeText(
                            this,
                            "Invalid email or password",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        // GO BACK TO SIGNUP
        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}
