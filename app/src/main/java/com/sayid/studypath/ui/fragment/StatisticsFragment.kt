package com.sayid.studypath.ui.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartAlignType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartAnimationType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartFontWeightType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAOptions
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.github.aachartmodel.aainfographics.aachartcreator.aa_toAAOptions
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AADataLabels
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AALabels
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAMarker
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAPie
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AASeries
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
import com.sayid.studypath.R
import com.sayid.studypath.databinding.FragmentStatisticsBinding
import com.sayid.studypath.ui.activity.QuizActivity
import com.sayid.studypath.utils.PredictionResultSingleton
import com.sayid.studypath.utils.QuizAnswerSingleton

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

        binding.btnReQuiz.setOnClickListener {
            showReQuizConfirmationDialog()
        }

        val categories =
            arrayOf(
                "Keterbukaan Terhadap Pengalaman",
                "Ketelitian",
                "Kestabilan Emosi",
                "Keterbukaan Sosial, Energi dan Antusiasme",
                "Kesepakatan",
            )

        val colorsTheme: Array<Any> =
            arrayOf(
                "#ffc800",
                "#1cb0f6",
                "#ff4b4b",
                "#ff9600",
                "#2b70c9",
            )

        PredictionResultSingleton.listPrediction.observe(viewLifecycleOwner) { prediction ->
            prediction?.let {
                val dataValues =
                    listOf(
                        (prediction.keterbukaanTerhadapPengalaman * 100).toInt(),
                        (prediction.ketelitian * 100).toInt(),
                        (prediction.kestabilanEmosi * 100).toInt(),
                        (prediction.keterbukaanSosialEnergiDanAntusiasme * 100).toInt(),
                        (prediction.kesepakatan * 100).toInt(),
                    )

                // Build Radar Chart Model
                val bigFiveRadarChartsModel =
                    AAChartModel()
                        .chartType(AAChartType.Line)
                        .backgroundColor("#00000000")
                        .axesTextColor("#58cc02")
                        .legendEnabled(false)
                        .animationType(AAChartAnimationType.SwingTo)
                        .animationDuration(1500)
                        .categories(categories)
                        .colorsTheme(colorsTheme)
                        .polar(true)
                        .series(
                            arrayOf(
                                AASeriesElement()
                                    .name("Persentase")
                                    .color("#1cb0f6")
                                    .data(
                                        dataValues
                                            .mapIndexed { index, value ->
                                                mapOf("y" to value, "color" to colorsTheme[index])
                                            }.toTypedArray(),
                                    )
                                    .dataLabels(
                                        AADataLabels()
                                            .enabled(true)
                                            .style(
                                                AAStyle()
                                                    .color("#58cc02")
                                                    .fontSize(10f)
                                                    .fontWeight(AAChartFontWeightType.Bold)
                                                    .textOutline("none")
                                            )
                                            .format("{point.y}%")
                                            .distance(-30)
                                    ),
                            ),
                        )

                val radarChartOptions = bigFiveRadarChartsModel.aa_toAAOptions()

                radarChartOptions.xAxis?.apply {
                    labels(
                        AALabels()
                            .style(
                                AAStyle()
                                    .color("#58cc02")
                                    .fontSize(10f)
                                    .fontWeight(AAChartFontWeightType.Bold)
                            )
                            .align(AAChartAlignType.Center)
                    )
                    tickmarkPlacement("on")
                }

                radarChartOptions.yAxis?.apply {
                    gridLineInterpolation("polygon")
                    min(0)
                    max(100)
                    labels(
                        AALabels()
                            .style(
                                AAStyle()
                                    .color("#58cc02")
                                    .fontSize(10f)
                                    .fontWeight(AAChartFontWeightType.Bold)
                            )
                    )
                }

                radarChartOptions.tooltip?.valueSuffix("%")
                radarChartOptions.plotOptions?.series(
                    AASeries()
                        .marker(
                            AAMarker()
                                .enabled(true)
                                .radius(6)
                                .symbol("circle"),
                        ),
                )

                // Build Bar Chart Model
                val horizontalBarChartModel =
                    AAChartModel()
                        .chartType(AAChartType.Bar)
                        .legendEnabled(false)
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
                                        dataValues
                                            .mapIndexed { index, value ->
                                                mapOf("y" to value, "color" to colorsTheme[index])
                                            }.toTypedArray(),
                                    ),
                            ),
                        )

                val horizontalBarChartOptions = horizontalBarChartModel.aa_toAAOptions()
                horizontalBarChartOptions.plotOptions?.bar?.borderRadius(4f)

                // Draw charts
                binding.bigFiveRadarCharts.aa_drawChartWithChartOptions(radarChartOptions)
                binding.bigFiveBarCharts.aa_drawChartWithChartOptions(horizontalBarChartOptions)
            }
        }
    }

    private fun showReQuizConfirmationDialog() {
        val dialog =
            AlertDialog
                .Builder(requireContext())
                .setTitle("Konfirmasi Ulangi Kuis")
                .setMessage("Apakah Kamu yakin ingin memulai ulang kuis?\nData saat ini akan hilang.")
                .setPositiveButton("Ya") { _, _ ->
                    startActivity(Intent(requireActivity(), QuizActivity::class.java))
                    QuizAnswerSingleton.clearAllAnswers()
                    requireActivity().finish()
                }.setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss()
                }.create()

        dialog.window?.setBackgroundDrawableResource(R.drawable.bg_radius_xlarge)
        dialog.show()
    }

    private fun playAnimation() {
        if (hasAnimated) {
            binding.statisticsLayout.alpha = 1.0f
            return
        } else {
            hasAnimated = true
            val containerAlpha =
                ObjectAnimator
                    .ofFloat(binding.statisticsLayout, View.ALPHA, 0f, 1f)
                    .setDuration(750)
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
