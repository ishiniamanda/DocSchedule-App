package com.nibm.docschedule.ui.intro

import android.content.Intent
import android.os.Bundle
import com.nibm.docschedule.ui.common.BaseActivity
import com.nibm.docschedule.databinding.ActivityIntroBinding
import com.nibm.docschedule.ui.main.MainActivity

class IntroActivity : BaseActivity() {

    private lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.getStartedBtn.setOnClickListener {
            val intent = Intent(this@IntroActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }


    }
}
