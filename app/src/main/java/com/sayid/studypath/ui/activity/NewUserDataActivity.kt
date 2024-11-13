package com.sayid.studypath.ui.activity

import android.os.Bundle
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
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
