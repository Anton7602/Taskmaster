package com.robkov.taskmaster

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter (private val itemList: List<Taskholder>, private val context: Context, private var databaseLocation: MutableList<String>?) : RecyclerView.Adapter<CardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.taskholder_card, parent, false)
        return CardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val bindedTaskholder = itemList[position]
        holder.taskName.text = bindedTaskholder.taskName
        holder.cardview.setOnClickListener{
            Log.d("Debug", "Pressed item ${bindedTaskholder.taskName}")
            val intent = Intent(context, MainActivity::class.java)
            if (databaseLocation==null) {
                databaseLocation = mutableListOf<String>()
            }
            databaseLocation!!.add(bindedTaskholder.taskID.toString())
            Log.d("Debug", "databaseLocationCount ${databaseLocation!!.size}. Last key = ${databaseLocation!!.last()}")
            intent.putStringArrayListExtra("DatabaseLocation", ArrayList(databaseLocation))
            intent.putExtra("TaskName", bindedTaskholder.taskName)
            startActivity(context, intent, null)
        }
    }

}

class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val cardview: CardView = itemView.findViewById(R.id.tkh_taskholder_cdv)
    val taskName: TextView = itemView.findViewById(R.id.tkh_taskName_txv)
    val editText: EditText = itemView.findViewById(R.id.tkh_taskNameEdit_edt)
    fun switchEditTextVisibility() {
        editText.visibility = if (editText.visibility==View.VISIBLE) View.INVISIBLE else View.VISIBLE
        if(editText.visibility == View.VISIBLE) {
            editText.requestFocus()
        }
    }
}