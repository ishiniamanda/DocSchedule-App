package com.nibm.docschedule.ui.records

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.nibm.docschedule.R
import com.nibm.docschedule.data.model.MedicalRecordModel

class AddRecordActivity : AppCompatActivity() {

    private lateinit var etTitle: TextInputEditText
    private lateinit var etType: TextInputEditText
    private lateinit var etDate: TextInputEditText
    private lateinit var etUser: TextInputEditText
    private lateinit var btnSaveRecord: Button
    private lateinit var ivRecordImage: ImageView

    private var imageUri: Uri? = null

    companion object {
        private const val IMAGE_PICK_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_record)

        etTitle = findViewById(R.id.etTitle)
        etType = findViewById(R.id.etType)
        etDate = findViewById(R.id.etDate)
        etUser = findViewById(R.id.etUser)
        btnSaveRecord = findViewById(R.id.btnSaveRecord)
        ivRecordImage = findViewById(R.id.ivRecordImage)

        ivRecordImage.setOnClickListener {
            openGallery()
        }

        btnSaveRecord.setOnClickListener {
            saveRecord()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            ivRecordImage.setImageURI(imageUri)
        }
    }

    private fun saveRecord() {

        val title = etTitle.text.toString().trim()
        val type = etType.text.toString().trim()
        val date = etDate.text.toString().trim()
        val user = etUser.text.toString().trim()

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val recordRef = FirebaseDatabase.getInstance()
            .getReference("MedicalRecords")
            .child(userId)
            .push()

        val record = MedicalRecordModel(
            title,
            type,
            date,
            user,
            imageUri?.toString() ?: ""   // ✅ IMAGE URI SAVED
        )

        recordRef.setValue(record).addOnSuccessListener {
            finish()
        }
    }
}
