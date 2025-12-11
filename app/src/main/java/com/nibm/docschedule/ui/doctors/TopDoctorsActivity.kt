package com.nibm.docschedule.ui.doctors

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.nibm.docschedule.ui.common.BaseActivity
import com.nibm.docschedule.ui.adapter.TopDoctorAdapter2
import com.nibm.docschedule.ui.main.viewmodel.MainViewModel
import com.nibm.docschedule.databinding.ActivityTopDoctorsBinding

class TopDoctorsActivity : BaseActivity() {
    private lateinit var binding: ActivityTopDoctorsBinding
    private val viewModel= MainViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding= ActivityTopDoctorsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTopDoctors()

    }

    private fun initTopDoctors() {
        binding.apply {
            progressBarTopDoctor.visibility= View.VISIBLE
            viewModel.doctors.observe(this@TopDoctorsActivity, Observer {
                viewTopDoctorList.layoutManager = LinearLayoutManager(
                    this@TopDoctorsActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                viewTopDoctorList.adapter = TopDoctorAdapter2(it)
                progressBarTopDoctor.visibility = View.GONE
            })
            viewModel.loadDoctors()

            backBtn.setOnClickListener {
                finish()
            }

        }
    }
}