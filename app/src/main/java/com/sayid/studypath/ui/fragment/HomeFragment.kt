package com.sayid.studypath.ui.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.sayid.studypath.R
import com.sayid.studypath.data.Result
import com.sayid.studypath.databinding.FragmentHomeBinding
import com.sayid.studypath.utils.PredictionResultSingleton
import com.sayid.studypath.utils.UserLoginDataSingleton
import com.sayid.studypath.utils.initializePersonalityCard
import com.sayid.studypath.utils.showToast
import com.sayid.studypath.viewmodel.MainViewModel
import com.sayid.studypath.viewmodel.SettingsViewModel
import com.sayid.studypath.viewmodel.factory.ViewModelFactory

class HomeFragment : Fragment() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var hasAnimated = false

    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(requireContext())
    }

    private val settingsViewModel: SettingsViewModel by viewModels {
        factory
    }

    private val mainViewModel: MainViewModel by viewModels {
        factory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.getBoolean(HAS_ANIMATED)?.let {
            hasAnimated = it
        }
        playAnimation()
        binding.tvUsername.text = "Sayid Achmad Maulana"

        PredictionResultSingleton.listPrediction.observe(viewLifecycleOwner) { prediction ->
            prediction?.let {
                initializePersonalityCard(
                    requireContext(),
                    binding.viewPager,
                    binding.indicatorLayout,
                    it,
                )
            }
        }

        binding.cardGetRecommendation.setOnClickListener {
            mainViewModel.getRecommendation()
        }

        binding.apply {
            val msg = "Tanya dulu Rekomendasi Hari ini!"
            btnShowOpenness.setOnClickListener { showToast(requireContext(), msg) }
            btnShowAgreeableness.setOnClickListener { showToast(requireContext(), msg) }
            btnShowNeurotism.setOnClickListener { showToast(requireContext(), msg) }
            btnShowConscientiousness.setOnClickListener { showToast(requireContext(), msg) }
            btnShowExtraversion.setOnClickListener { showToast(requireContext(), msg) }
        }

        mainViewModel.recommendationResponse.observe(viewLifecycleOwner) { result ->
            binding.apply {
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            loadingGetRecommendation?.visibility = View.VISIBLE
                            cardGetRecommendation.visibility = View.INVISIBLE
                            showToast(requireContext(), "Sedang Memuat Rekomendasi...")
                        }

                        is Result.Success -> {
                            val data = result.data.data.recommendation

                            btnShowOpenness.setOnClickListener {
                                val dialog =
                                    RecommendationDialogFragment.showDialog(
                                        data.openness.judul,
                                        data.openness.deskripsi,
                                        data.openness.rekomendasi,
                                        R.drawable.icon_keterbukaan,
                                    )

                                dialog.show(parentFragmentManager, "Recommendation")
                            }

                            btnShowAgreeableness.setOnClickListener {
                                val dialog =
                                    RecommendationDialogFragment.showDialog(
                                        data.agreeableness.judul,
                                        data.agreeableness.deskripsi,
                                        data.agreeableness.rekomendasi,
                                        R.drawable.icon_kesepakatan_2,
                                    )

                                dialog.show(parentFragmentManager, "Recommendation")
                            }

                            btnShowNeurotism.setOnClickListener {
                                val dialog =
                                    RecommendationDialogFragment.showDialog(
                                        data.neuroticism.judul,
                                        data.neuroticism.deskripsi,
                                        data.neuroticism.rekomendasi,
                                        R.drawable.icon_kestabilan,
                                    )

                                dialog.show(parentFragmentManager, "Recommendation")
                            }

                            btnShowConscientiousness.setOnClickListener {
                                val dialog =
                                    RecommendationDialogFragment.showDialog(
                                        data.conscientiousness.judul,
                                        data.conscientiousness.deskripsi,
                                        data.conscientiousness.rekomendasi,
                                        R.drawable.icon_ketelitian,
                                    )

                                dialog.show(parentFragmentManager, "Recommendation")
                            }

                            btnShowExtraversion.setOnClickListener {
                                val dialog =
                                    RecommendationDialogFragment.showDialog(
                                        data.extroversion.judul,
                                        data.extroversion.deskripsi,
                                        data.extroversion.rekomendasi,
                                        R.drawable.icon_sosial,
                                    )

                                dialog.show(parentFragmentManager, "Recommendation")
                            }

                            loadingGetRecommendation?.visibility = View.GONE
                            cardGetRecommendation.visibility = View.VISIBLE
                        }

                        is Result.Error -> {
                            loadingGetRecommendation?.visibility = View.GONE
                            cardGetRecommendation.visibility = View.VISIBLE
                            showToast(requireContext(), "Gagal Mendapatkan Data Rekomendasi")
                        }
                    }
                }
            }
        }

        var fetchOnceMore = false

        UserLoginDataSingleton.userLoginData.observe(viewLifecycleOwner) { result ->
            binding.apply {
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            loading?.visibility = View.VISIBLE
                            tvUsername.visibility = View.INVISIBLE
                            ivAvatar.visibility = View.INVISIBLE
                            welcomeMessage?.visibility = View.INVISIBLE
                        }

                        is Result.Success -> {
                            val userData = result.data

                            Glide
                                .with(this@HomeFragment)
                                .load(userData.avatar)
                                .placeholder(R.drawable.undraw_male_avatar)
                                .circleCrop()
                                .into(ivAvatar)

                            tvUsername.text = userData.name
                            loading?.visibility = View.GONE
                            tvUsername.visibility = View.VISIBLE
                            ivAvatar.visibility = View.VISIBLE
                            welcomeMessage?.visibility = View.VISIBLE
                        }

                        is Result.Error -> {
                            loading?.visibility = View.GONE
                            tvUsername.visibility = View.INVISIBLE
                            ivAvatar.visibility = View.INVISIBLE
                            welcomeMessage?.visibility = View.INVISIBLE

                            if (!fetchOnceMore) {
                                settingsViewModel.getUserData()
                                fetchOnceMore = true
                            } else {
                                showToast(requireContext(), "Gagal Memuat Data Pengguna")
                            }
                            Log.d("RESULT", result.error)
                        }
                    }
                }
            }
        }
    }

    private fun playAnimation() {
        if (hasAnimated) {
            binding.homeLayout.alpha = 1.0f
            return
        } else {
            hasAnimated = true
            val containerAlpha =
                ObjectAnimator.ofFloat(binding.homeLayout, View.ALPHA, 0f, 1f).setDuration(750)
            val containerMove =
                ObjectAnimator
                    .ofFloat(binding.homeLayout, View.TRANSLATION_Y, 250f, 0f)
                    .setDuration(750)
            AnimatorSet().apply {
                playTogether(containerAlpha, containerMove)
                start()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(HAS_ANIMATED, hasAnimated)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val HAS_ANIMATED = "has_animated"
    }
}
