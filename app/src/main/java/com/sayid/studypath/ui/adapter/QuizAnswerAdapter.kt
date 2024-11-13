package com.sayid.studypath.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.sayid.studypath.R

class QuizAnswerAdapter(private val items: List<List<*>>) : RecyclerView.Adapter<QuizAnswerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val button: MaterialButton = view.findViewById(R.id.btn_quiz_answer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.quiz_answer_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.button.text = item[0].toString()
        holder.button.setBackgroundColor(ContextCompat.getColor(holder.button.context, item[1] as Int))
    }

    override fun getItemCount(): Int = items.size
}