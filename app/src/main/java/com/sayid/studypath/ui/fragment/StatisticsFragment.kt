package com.sayid.studypath.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.sayid.studypath.databinding.FragmentStatisticsBinding

class StatisticsFragment : Fragment() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bigFivePieChartsView = binding.bigFivePieCharts
        val bigFiveRadarChartsView = binding.bigFiveRadarCharts

        val bigFivePieChartsModel: AAChartModel = AAChartModel()
            .chartType(AAChartType.Pie)
            .dataLabelsEnabled(true)
            .backgroundColor("#00000000")
            .colorsTheme(arrayOf("#16C79A", "#CBB53B", "#11698E", "#EE7D52", "AC56C1"))
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Big Five Personality")
                        .size("100%")
                        .data(
                            arrayOf(
                                arrayOf("Openness", 0.56315989),
                                arrayOf("Conscientiousness", 0.71610975),
                                arrayOf("Neuroticism", 0.64705192),
                                arrayOf("Extroversion", 0.35946574),
                                arrayOf("Agreeableness", 0.58775013)
                            )
                        )
                )
            )

        val bigFiveRadarChartsModel: AAChartModel = AAChartModel()
            .chartType(AAChartType.Area)
            .backgroundColor("#00000000")
            .categories(
                arrayOf(
                    "Openness",
                    "Conscientiousness",
                    "Neuroticism",
                    "Extroversion",
                    "Agreeableness"
                )
            )
            .colorsTheme(arrayOf("#16C79A"))
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Score")
                        .data(arrayOf(0.56315989, 0.71610975, 0.64705192, 0.35946574, 0.58775013))
                )
            )
            .polar(true)

        bigFivePieChartsView.aa_drawChartWithChartModel(bigFivePieChartsModel)
        bigFiveRadarChartsView.aa_drawChartWithChartModel(bigFiveRadarChartsModel)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
