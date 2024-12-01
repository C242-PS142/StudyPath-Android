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
import com.sayid.studypath.ui.activity.QuizActivity
import com.sayid.studypath.utils.PredictionResultSingleton

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

        val categories = arrayOf(
            "Keterbukaan Terhadap Pengalaman",
            "Ketelitian",
            "Kestabilan Emosi",
            "Keterbukaan Sosial, Energi dan Antusiasme",
            "Kesepakatan"
        )

        val colorsTheme: Array<Any> = arrayOf(
            "#ffc800", "#1cb0f6", "#ff4b4b", "#ff9600", "#2b70c9"
        )

        PredictionResultSingleton.listPrediction.observe(viewLifecycleOwner) { prediction ->
            val dataValues = listOf(
                (prediction.keterbukaanTerhadapPengalaman * 100).toInt(),
                (prediction.ketelitian * 100).toInt(),
                (prediction.kestabilanEmosi * 100).toInt(),
                (prediction.keterbukaanSosialEnergiDanAntusiasme * 100).toInt(),
                (prediction.kesepakatan * 100).toInt(),
            )

            // Build Pie Chart Model
            val bigFivePieChartsModel = AAChartModel()
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
                                    .toTypedArray()
                            )
                    )
                )

            val pieChartOptions = bigFivePieChartsModel.aa_toAAOptions()
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
                                    .fontSize(16f)
                            )
                            .distance(15)
                    )
            )

            // Build Bar Chart Model
            val horizontalBarChartModel = AAChartModel()
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
                                dataValues.mapIndexed { index, value ->
                                    mapOf("y" to value, "color" to colorsTheme[index])
                                }.toTypedArray()
                            )
                    )
                )

            val horizontalBarChartOptions = horizontalBarChartModel.aa_toAAOptions()
            horizontalBarChartOptions.plotOptions?.bar?.borderRadius(4f)

            // Draw charts
            binding.bigFivePieCharts.aa_drawChartWithChartOptions(pieChartOptions)
            binding.bigFiveBarCharts.aa_drawChartWithChartOptions(horizontalBarChartOptions)
        }
    }

    private fun showReQuizConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi Ulangi Kuis")
            .setMessage("Apakah Kamu yakin ingin memulai ulang kuis?\nData saat ini akan hilang.")
            .setPositiveButton("Ya") { _, _ ->
                startActivity(Intent(requireActivity(), QuizActivity::class.java))
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun playAnimation() {
        if (hasAnimated) {
            binding.statisticsLayout.alpha = 1.0f
            return
        } else {
            hasAnimated = true
            val containerAlpha =
                ObjectAnimator.ofFloat(binding.statisticsLayout, View.ALPHA, 0f, 1f)
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
