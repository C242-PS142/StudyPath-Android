package com.sayid.studypath.ui.activity

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.sayid.studypath.databinding.ActivityNewUserDataBinding

class NewUserDataActivity : AppCompatActivity() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: ActivityNewUserDataBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNewUserDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dropdownGradeItems =
            listOf(
                "Pilih Jenjang Anda Saat Ini",
                "Sekolah Menengah Pertama",
                "Madrasah Tsanawiyah",
                "Sekolah Menengah Atas",
                "Sekolah Menengah Kejuruan",
            )

        val dropdownAgeItems =
            listOf(
                "Rentang Usia Anda Saat Ini",
                "10 Tahun Ke Bawah",
                "10-12 Tahun",
                "13-15 Tahun",
                "16-18 Tahun",
                "18 Tahun Ke Atas",
            )

        val gradeAdapter =
            ArrayAdapter(
                this,
                R.layout.simple_spinner_item,
                dropdownGradeItems,
            )

        gradeAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        val ageAdapter =
            ArrayAdapter(
                this,
                R.layout.simple_spinner_item,
                dropdownAgeItems,
            )

        ageAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        binding.apply {
            btnSave.setOnClickListener {
                startActivity(Intent(this@NewUserDataActivity, MainActivity::class.java))
                finish()
            }
            spinnerGrade.adapter = gradeAdapter
            spinnerAge.adapter = ageAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
