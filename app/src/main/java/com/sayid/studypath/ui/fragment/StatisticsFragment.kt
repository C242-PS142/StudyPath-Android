package com.sayid.studypath.ui.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartAnimationType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAOptions
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.github.aachartmodel.aainfographics.aachartcreator.aa_toAAOptions
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AADataLabels
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAPie
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
import com.sayid.studypath.databinding.FragmentStatisticsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatisticsFragment : Fragment() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!
    private lateinit var pieChartOptions: AAOptions
    private lateinit var horizontalBarChartOptions: AAOptions
    private var hasAnimated = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.getBoolean(HAS_ANIMATED)?.let {
            hasAnimated = it
            binding.statisticsLayout.alpha = 1.0f
        }
        playAnimation()

        lifecycleScope.launch(Dispatchers.IO) {
            val categories =
                arrayOf(
                    "Keterbukaan Terhadap Pengalaman",
                    "Ketelitian",
                    "Kestabilan Emosi",
                    "Keterbukaan Sosial, Energi dan Antusiasme",
                    "Kesepakatan",
                )

            val dataValues: Array<Any> = arrayOf(56, 71, 64, 36, 59)

            val colorsTheme: Array<Any> =
                arrayOf("#ffc800", "#1cb0f6", "#ff4b4b", "#ff9600", "#2b70c9")

// Pie Chart Model
            val bigFivePieChartsModel: AAChartModel =
                AAChartModel()
                    .chartType(AAChartType.Pie)
                    .backgroundColor("#00000000")
                    .axesTextColor("#FFFFFF")
                    .legendEnabled(false)
                    .animationType(AAChartAnimationType.SwingTo)
                    .animationDuration(1500)
                    .colorsTheme(colorsTheme)
                    .series(
                        arrayOf(
                            AASeriesElement()
                                .name("Persentase")
                                .size("70%")
                                .data(
                                    categories
                                        .zip(dataValues)
                                        .map { arrayOf(it.first, it.second) }
                                        .toTypedArray(),
                                ),
                        ),
                    )

            pieChartOptions = bigFivePieChartsModel.aa_toAAOptions()

            pieChartOptions.plotOptions?.pie(
                AAPie()
                    .allowPointSelect(true)
                    .cursor("pointer")
                    .dataLabels(
                        AADataLabels()
                            .enabled(true)
                            .format("{point.y:.1f}%")
                            .style(
                                AAStyle()
                                    .color("#FFFFFF")
                                    .fontSize(16f),
                            ).distance(15),
                    ),
            )

            val horizontalBarChartModel: AAChartModel =
                AAChartModel()
                    .chartType(AAChartType.Bar)
                    .backgroundColor("#00000000")
                    .animationType(AAChartAnimationType.SwingTo)
                    .animationDuration(1500)
                    .categories(categories)
                    .colorsTheme(colorsTheme)
                    .series(
                        arrayOf(
                            AASeriesElement()
                                .name("Persentase")
                                .data(
                                    arrayOf(
                                        mapOf("y" to 56, "color" to "#1cb0f6"),
                                        mapOf("y" to 71, "color" to "#ff4b4b"),
                                        mapOf("y" to 64, "color" to "#ffc800"),
                                        mapOf("y" to 36, "color" to "#ff9600"),
                                        mapOf("y" to 59, "color" to "#2b70c9"),
                                    ),
                                ),
                        ),
                    )

            horizontalBarChartOptions = horizontalBarChartModel.aa_toAAOptions()
            horizontalBarChartOptions.plotOptions?.bar?.borderRadius(4f)

            withContext(Dispatchers.Main) {
                binding.bigFivePieCharts.aa_drawChartWithChartOptions(pieChartOptions)
                binding.bigFiveBarCharts.aa_drawChartWithChartOptions(horizontalBarChartOptions)
            }
        }
    }

    private fun playAnimation() {
        if (hasAnimated) {
            binding.statisticsLayout.alpha = 1.0f
            return
        } else {
            hasAnimated = true
            val containerAlpha =
                ObjectAnimator.ofFloat(binding.statisticsLayout, View.ALPHA, 0f, 1f).setDuration(750)
            val containerMove =
                ObjectAnimator
                    .ofFloat(binding.statisticsLayout, View.TRANSLATION_Y, 250f, 0f)
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
