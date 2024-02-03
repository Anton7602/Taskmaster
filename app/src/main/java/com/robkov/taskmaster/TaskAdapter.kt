package com.robkov.taskmaster

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter (private val itemList: List<String>) : RecyclerView.Adapter<CardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.taskholder_card, parent, false)
        return CardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val itemText = itemList[position]
        holder.taskName.text = itemText
    }

}

class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val taskName: TextView = itemView.findViewById(R.id.tkh_taskName_txv)
    val editText: EditText = itemView.findViewById(R.id.tkh_taskNameEdit_edt)
    fun switchEditTextVisibility() {
        editText.visibility = if (editText.visibility==View.VISIBLE) View.INVISIBLE else View.VISIBLE
        if(editText.visibility == View.VISIBLE) {
            editText.requestFocus()
        }
    }
}