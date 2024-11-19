package com.sayid.studypath.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sayid.studypath.databinding.FragmentHomeBinding
import com.sayid.studypath.utils.initializePersonalityCard

class HomeFragment : Fragment() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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

        binding.tvUsername.text = "Sayid Achmad Maulana"
        initializePersonalityCard(requireContext(), binding.viewPager, binding.indicatorLayout)

        binding.apply {
//            btnGetRecommendation.setOnClickListener {
//                if (tvWait.visibility == View.GONE) {
//                    tvRecommendation.visibility = View.VISIBLE
//                    tvWait.visibility = View.VISIBLE
//                } else {
//                    tvRecommendation.visibility = View.GONE
//                    tvWait.visibility = View.GONE
//                }
//            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
