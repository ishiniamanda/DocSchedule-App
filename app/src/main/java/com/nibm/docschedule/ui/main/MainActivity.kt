package com.nibm.docschedule.ui.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.nibm.docschedule.data.model.DoctorsModel
import com.nibm.docschedule.databinding.ActivityMainBinding
import com.nibm.docschedule.ui.adapter.CategoryAdapter
import com.nibm.docschedule.ui.adapter.TopDoctorAdapter
import com.nibm.docschedule.ui.common.BaseActivity
import com.nibm.docschedule.ui.contact.ContactActivity
import com.nibm.docschedule.ui.doctors.TopDoctorsActivity
import com.nibm.docschedule.ui.help.HelpActivity
import com.nibm.docschedule.ui.records.RecordsActivity
import com.nibm.docschedule.ui.auth.SignupActivity
import android.app.AlertDialog

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel = com.nibm.docschedule.ui.main.viewmodel.MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ----------------------------
        // Set the greeting with user's name
        // ----------------------------
        val user = FirebaseAuth.getInstance().currentUser
        val userName = user?.displayName ?: "User"
        binding.textView2.text = "Hi $userName"

        initCategory()
        initTopDoctors()
        setupLogout()
        setupContact()
        setupHelp()
        setupRecords()

        // ----------------------------
        // Setup real-time search
        // ----------------------------
        binding.editTextText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim().lowercase() // lowercase for case-insensitive search
                searchDoctors(query)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // ----------------------------
    // Search Doctors (real-time, case-insensitive)
    // ----------------------------
    private fun searchDoctors(query: String) {
        val ref = FirebaseDatabase.getInstance().getReference("doctors")
        binding.progressBarTopDoctor.visibility = View.VISIBLE

        ref.get().addOnSuccessListener { snapshot ->
            val filteredDoctors = mutableListOf<DoctorsModel>()

            for (doctorSnap in snapshot.children) {
                val doctor = doctorSnap.getValue(DoctorsModel::class.java)
                if (doctor != null) {
                    if (query.isEmpty() || doctor.Name.lowercase().contains(query)) {
                        filteredDoctors.add(doctor)
                    }
                }
            }

            // Update RecyclerView
            binding.recyclerViewTopDoctor.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerViewTopDoctor.adapter = TopDoctorAdapter(filteredDoctors)
            binding.progressBarTopDoctor.visibility = View.GONE
        }
            .addOnFailureListener { error ->
                binding.progressBarTopDoctor.visibility = View.GONE
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun initTopDoctors() {
        binding.apply {
            progressBarTopDoctor.visibility = View.VISIBLE
            viewModel.doctors.observe(this@MainActivity, Observer {
                recyclerViewTopDoctor.layoutManager =
                    LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                recyclerViewTopDoctor.adapter = TopDoctorAdapter(it)
                progressBarTopDoctor.visibility = View.GONE
            })
            viewModel.loadDoctors()

            doctorListTxt.setOnClickListener {
                startActivity(Intent(this@MainActivity, TopDoctorsActivity::class.java))
            }
        }
    }

    private fun initCategory() {
        binding.progressBarCategory.visibility = View.VISIBLE
        viewModel.category.observe(this, Observer {
            binding.viewCategory.layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.viewCategory.adapter = CategoryAdapter(it)

            binding.progressBarCategory.visibility = View.GONE
        })
        viewModel.loadCategory()
    }

    private fun setupLogout() {
        binding.ivLogout.setOnClickListener {

            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes") { _, _ ->

                    FirebaseAuth.getInstance().signOut()

                    val intent = Intent(this, SignupActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun setupContact() {
        binding.ivContact.setOnClickListener {
            startActivity(Intent(this, ContactActivity::class.java))
        }
    }

    private fun setupHelp() {
        binding.layoutHelp.setOnClickListener {
            startActivity(Intent(this, HelpActivity::class.java))
        }
    }

    private fun setupRecords() {
        binding.layoutRecords.setOnClickListener {
            startActivity(Intent(this, RecordsActivity::class.java))
        }
    }
}
