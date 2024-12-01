package com.sayid.studypath.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sayid.studypath.R

class OnboardingPageAdapter(
    private val context: Context,
    private val illustrations: List<Int>,
    private val contents: Map<String, String>,
) : RecyclerView.Adapter<OnboardingPageAdapter.OnboardingViewHolder>() {
    inner class OnboardingViewHolder(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): OnboardingViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.onboarding_content_item, parent, false)
        return OnboardingViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: OnboardingViewHolder,
        position: Int,
    ) {
        val onboardingIllustration = holder.itemView.findViewById<ImageView>(R.id.iv_illustration)
        onboardingIllustration.setImageResource(illustrations[position])

        val onboardingTitle = holder.itemView.findViewById<TextView>(R.id.tv_onboarding_title)
        onboardingTitle.text = contents.keys.elementAt(position)

        val onboardingDescription =
            holder.itemView.findViewById<TextView>(R.id.tv_onboarding_description)
        onboardingDescription.text = contents.values.elementAt(position)
    }

    override fun getItemCount(): Int = illustrations.size
}
