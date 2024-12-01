package com.sayid.studypath.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.sayid.studypath.R

class PersonalityCardAdapter(
    private val context: Context,
    private val title: List<Int>,
    private val illustrations: List<Int>,
    private val percentages: List<Int>,
) : RecyclerView.Adapter<PersonalityCardAdapter.CarouselViewHolder>() {
    inner class CarouselViewHolder(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CarouselViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.personality_card_item, parent, false)
        return CarouselViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CarouselViewHolder,
        position: Int,
    ) {
        val personalityTitle = holder.itemView.findViewById<TextView>(R.id.tv_personality_title)
        personalityTitle.text = getString(context, title[position])

        val personalityPercentage =
            holder.itemView.findViewById<TextView>(R.id.tv_percentage)

        @SuppressLint("SetTextI18n")
        personalityPercentage.text = "${percentages[position]}%"

        val personalityIllustration = holder.itemView.findViewById<ImageView>(R.id.iv_illustration)
        personalityIllustration.setImageDrawable(getDrawable(context, illustrations[position]))
    }

    override fun getItemCount(): Int = title.size
}
