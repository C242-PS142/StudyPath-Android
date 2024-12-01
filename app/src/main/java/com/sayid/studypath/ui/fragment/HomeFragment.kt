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
import com.sayid.studypath.databinding.FragmentHomeBinding
import com.sayid.studypath.utils.PredictionResultSingleton
import com.sayid.studypath.utils.initializePersonalityCard
import com.sayid.studypath.viewmodel.QuizActivityViewModel
import com.sayid.studypath.viewmodel.factory.ViewModelFactory

class HomeFragment : Fragment() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var hasAnimated = false

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
                    it
                )
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
