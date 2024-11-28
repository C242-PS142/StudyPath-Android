package com.sayid.studypath.ui.activity

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sayid.studypath.R
import com.sayid.studypath.databinding.ActivityMainBinding
import com.sayid.studypath.viewmodel.MainViewModel
import com.sayid.studypath.viewmodel.factory.ViewModelFactory

class MainActivity : AppCompatActivity() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this)
    }
    private val mainViewModel: MainViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.isDarkTheme.observe(this) { isDarkTheme ->
            val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            val desiredMode = if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO

            if ((isDarkTheme && currentNightMode != Configuration.UI_MODE_NIGHT_YES) ||
                (!isDarkTheme && currentNightMode != Configuration.UI_MODE_NIGHT_NO)
            ) {
                AppCompatDelegate.setDefaultNightMode(desiredMode)
            }
        }

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
