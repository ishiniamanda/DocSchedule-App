package com.nibm.docschedule.feature.intro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nibm.docschedule.MainActivity
import com.nibm.docschedule.R
import com.nibm.docschedule.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity() {

    private lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.getStartedBtn.setOnClickListener {
            startActivity(Intent(this@IntroActivity, MainActivity::class.java))
            finish() // Optional: prevent going back to intro
        }
    }
}
