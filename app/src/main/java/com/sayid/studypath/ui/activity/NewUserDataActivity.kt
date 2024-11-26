package com.sayid.studypath.ui.activity

//noinspection SuspiciousImport
import android.R
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.transition.Transition
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.sayid.studypath.data.Result
import com.sayid.studypath.databinding.ActivityNewUserDataBinding
import com.sayid.studypath.utils.reduceFileImage
import com.sayid.studypath.utils.saveBitmapToCache
import com.sayid.studypath.utils.showDatePicker
import com.sayid.studypath.utils.showToast
import com.sayid.studypath.utils.uriToFile
import com.sayid.studypath.viewmodel.NewUserDataViewModel
import com.sayid.studypath.viewmodel.factory.ViewModelFactory

class NewUserDataActivity : AppCompatActivity() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: ActivityNewUserDataBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentImageUri: Uri
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this)
    }
    private val newUserDataViewModel: NewUserDataViewModel by viewModels {
        factory
    }
    private val launcherGallery =
        registerForActivityResult(
            ActivityResultContracts.PickVisualMedia(),
        ) { uri: Uri? ->
            if (uri != null) {
                currentImageUri = uri
                showImage()
            } else {
                showToast(this@NewUserDataActivity, "Tidak ada gambar yang dipilih")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNewUserDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        savedInstanceState?.getString(KEY_CURRENT_IMAGE_URI)?.let { uriString ->
            currentImageUri = Uri.parse(uriString)
            showImage()
        }
        initializeForm()
        viewModelObserve()
        setListener()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_CURRENT_IMAGE_URI, currentImageUri.toString())
    }

    private fun showImage() {
        binding.apply {
            Glide
                .with(main.context)
                .load(currentImageUri)
                .circleCrop()
                .into(ivAvatar)
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun viewModelObserve() {
        newUserDataViewModel.userResponse.observe(this) { result ->
            binding.apply {
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            isLoading(true)
                        }

                        is Result.Success -> {
                            val response = result.data.data
                            isLoading(false)
                            Glide
                                .with(this@NewUserDataActivity)
                                .asBitmap()
                                .load(response.avatar)
                                .into(
                                    object : CustomTarget<Bitmap>() {
                                        override fun onResourceReady(
                                            resource: Bitmap,
                                            transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?,
                                        ) {
                                            try {
                                                val uri =
                                                    saveBitmapToCache(
                                                        this@NewUserDataActivity,
                                                        resource,
                                                    )
                                                currentImageUri = uri
                                                showImage()
                                            } catch (e: Exception) {
                                                Log.d(TAG, e.message.toString())
                                            }
                                        }

                                        override fun onLoadCleared(drawable: Drawable?) {}
                                    },
                                )
                            edtName.setText(response.name)
                        }

                        is Result.Error -> {
                            isLoading(false)
                            Log.d(TAG, result.error)
                        }
                    }
                }
            }
        }
        newUserDataViewModel.registerResponse.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        isLoading(true)
                    }

                    is Result.Success -> {
                        Log.d(TAG, result.data.message)
                        isLoading(false)
                        val intent =
                            Intent(this@NewUserDataActivity, QuizConfirmationActivity::class.java)
                        intent.putExtra(QuizConfirmationActivity.HAS_DATA, false)
                        startActivity(intent)
                        finish()
                    }

                    is Result.Error -> {
                        isLoading(false)
                        Log.d(TAG, result.error)
                    }
                }
            }
        }
    }

    private fun setListener() {
        binding.apply {
            btnSave.setOnClickListener {
                val name =
                    edtName.text.toString().trim()
                val dateBirth = edtDateBirth.text.toString().trim()
                val gender =
                    spinnerGender.selectedItem
                        .toString()
                        .first()
                        .toString()
                val avatar = uriToFile(currentImageUri, this@NewUserDataActivity).reduceFileImage()

                if (name.isEmpty()) {
                    edtName.error = "Lengkapi Nama Kamu!"
                }

                val errorMessage =
                    when {
                        spinnerGender.selectedItemPosition == 0 && dateBirth.isEmpty() ->
                            "Lengkapi Jenjang, Tanggal Lahir dan Gender Kamu!"

                        spinnerGender.selectedItemPosition == 0 ->
                            "Lengkapi Gender Kamu!"

                        dateBirth.isEmpty() ->
                            "Lengkapi Tanggal Lahir Kamu!"

                        else -> null
                    }

                errorMessage?.let {
                    showToast(this@NewUserDataActivity, it, false)
                    return@setOnClickListener
                }

                newUserDataViewModel.register(
                    name = name,
                    dateBirth = dateBirth,
                    gender = gender,
                    avatar = avatar,
                )
            }
            ibEdit.setOnClickListener {
                startGallery()
            }
            edtName.addTextChangedListener(afterTextChanged = { editable ->
                val length = editable?.length ?: 0
                if (length == 0) {
                    edtName.error = "Lengkapi Nama Kamu!"
                } else {
                    edtName.error = null
                }
            })
            edtDateBirth.setOnClickListener {
                showDatePicker(supportFragmentManager) { selectedDate ->
                    edtDateBirth.setText(selectedDate)
                }
            }

            edtDateBirth.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    showDatePicker(supportFragmentManager) { selectedDate ->
                        edtDateBirth.setText(selectedDate)
                    }
                }
            }
        }
    }

    private fun initializeForm() {
        val genderItems =
            listOf(
                "Pilih Gender Kamu",
                "Laki Laki",
                "Perempuan",
            )

        val genderAdapter =
            ArrayAdapter(
                this,
                R.layout.simple_spinner_item,
                genderItems,
            )

        genderAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        binding.apply {
            spinnerGender.adapter = genderAdapter
        }
    }

    private fun isLoading(active: Boolean) {
        binding.apply {
            loading.visibility = if (active) View.VISIBLE else View.GONE
            btnSave.isEnabled = !active
            edtName.isClickable = !active
            spinnerGender.isClickable = !active
            ibEdit.isClickable = !active
            edtDateBirth.isFocusable = !active
            edtDateBirth.isClickable = !active
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "NewUserDataActivity"
        private const val KEY_CURRENT_IMAGE_URI = "current_image_uri"
    }
}
