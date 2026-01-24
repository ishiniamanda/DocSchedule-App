package com.nibm.docschedule.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.nibm.docschedule.ui.auth.SignupActivity
import com.nibm.docschedule.databinding.ActivityLoginBinding
import com.nibm.docschedule.ui.intro.IntroActivity
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import com.nibm.docschedule.worker.AppointmentReminderWorker


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                101
            )
        }


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


                        if (task.isSuccessful) {

                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                            val workRequest =
                                OneTimeWorkRequestBuilder<AppointmentReminderWorker>()
                                    .setInitialDelay(10, TimeUnit.SECONDS) // TEST: 10 seconds
                                    .build()

                            WorkManager.getInstance(this).enqueue(workRequest)

                            startActivity(Intent(this, IntroActivity::class.java))
                            finish()
                        }



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