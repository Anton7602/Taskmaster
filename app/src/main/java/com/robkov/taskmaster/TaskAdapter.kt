package com.robkov.taskmaster

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference

class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val cardView: CardView = itemView.findViewById(R.id.tkh_taskholder_cdv)
    private val taskName: TextView = itemView.findViewById(R.id.tkh_taskName_txv)
    private val editText: EditText = itemView.findViewById(R.id.tkh_taskNameEdit_edt)
    private val controlPanel: CardView = itemView.findViewById(R.id.tkh_controlPanel_cdv)
    private val deleteButton: ImageButton = itemView.findViewById(R.id.tkh_remove_btn)
    private val editButton: ImageButton = itemView.findViewById(R.id.tkh_edit_btn)
    private val completeButton: ImageButton = itemView.findViewById(R.id.tkh_complete_btn)
    private var parentAdapter: TaskAdapter? = null
    private var hostingActivity: MainActivity? = null
    private var database: DatabaseReference? = null
    private var databaseLocation: MutableList<String>? = null
    private var linkedTaskholder: Taskholder? = null
    var isExpanded: Boolean = false

    fun initializeCardViewHolder(taskholder: Taskholder, context: Context, taskDatabaseLocation: MutableList<String>?,
                                 activeDatabase: DatabaseReference, activity: MainActivity, adapter: TaskAdapter) {
        Log.d("Debug","Initialized new CardViewHolder" + taskholder.taskID + " " + taskholder.taskName)
        parentAdapter = adapter
        linkedTaskholder = taskholder
        databaseLocation = taskDatabaseLocation
        database = activeDatabase
        hostingActivity = activity
        taskName.text = taskholder.taskName
        if (taskholder.isCompleted) {
            switchStyleToCompleted()
        }

        cardView.setOnLongClickListener{
            if (!isExpanded) {
                parentAdapter!!.collapseAllViewHolders()
            }
            switchExpandedState()
            true
        }

        cardView.setOnClickListener{
            Log.d("Debug", "Pressed item ${linkedTaskholder!!.taskName}")
            val intent = Intent(context, MainActivity::class.java)
            if (databaseLocation==null) {
                databaseLocation = mutableListOf<String>()
            }
            databaseLocation!!.add(linkedTaskholder!!.taskID.toString())
            Log.d("Debug", "databaseLocationCount ${databaseLocation!!.size}. Last key = ${databaseLocation!!.last()}")
            intent.putStringArrayListExtra("DatabaseLocation", ArrayList(databaseLocation))
            intent.putExtra("TaskName", linkedTaskholder!!.taskName)
            startActivity(context, intent, null)
            databaseLocation!!.removeAt(databaseLocation!!.size-1)
        }

        deleteButton.setOnClickListener{
            Log.d("Debug", "Pressed delete on item ${linkedTaskholder?.taskName}")
            if (linkedTaskholder!=null && database!=null) {
                database!!.child(linkedTaskholder!!.taskID.toString()).removeValue()
            }
        }

        editButton.setOnClickListener{
            Log.d("Debug", "Pressed edit on item ${linkedTaskholder?.taskName}")
            controlPanel.visibility = View.GONE
            taskName.visibility = View.INVISIBLE
            editText.setText(taskName.text)
            editText.visibility = if (editText.visibility==View.VISIBLE) View.INVISIBLE else View.VISIBLE
            if (hostingActivity != null && linkedTaskholder!=null) {
                hostingActivity!!.editTask(editText, linkedTaskholder!!, this)
            }
        }

        completeButton.setOnClickListener{
            Log.d("Debug", "Pressed completed on item ${linkedTaskholder?.taskName}")
            if (linkedTaskholder!=null && database!= null) {
                database!!.child(linkedTaskholder!!.taskID.toString()).child("completed").setValue(!linkedTaskholder!!.isCompleted)
            }
        }
    }

    fun taskEditFinished(isCanceled: Boolean) {
        taskName.visibility = View.VISIBLE
        editText.visibility = View.GONE
        if(isCanceled) {
            switchExpandedState()
        }
    }

    fun switchExpandedState() {
        val cardLayoutParams = cardView.layoutParams as ViewGroup.MarginLayoutParams
        val textLayoutParams = taskName.layoutParams as ViewGroup.MarginLayoutParams
        if (isExpanded) {
            isExpanded = false
            cardLayoutParams.setMargins(0, dpToPx(4), 0, dpToPx(4))
            textLayoutParams.setMargins(0, dpToPx(4), 0, dpToPx(4))
        } else {
            isExpanded = true
            cardLayoutParams.setMargins(0, dpToPx(16), 0, dpToPx(16))
            textLayoutParams.setMargins(0, dpToPx(16), 0, dpToPx(16))
        }
        cardView.layoutParams = cardLayoutParams
        taskName.layoutParams = textLayoutParams
        if (!linkedTaskholder!!.isCompleted) {
            controlPanel.visibility = if (isExpanded) View.VISIBLE else View.GONE
        } else {
            completeButton.visibility = if (isExpanded) View.GONE else View.VISIBLE
            deleteButton.visibility = if (isExpanded) View.VISIBLE else View.GONE
        }
    }

    private fun switchStyleToCompleted() {
        completeButton.setImageResource(R.drawable.complete_button_active)
        controlPanel.visibility = View.VISIBLE
        deleteButton.visibility = View.GONE
        editButton.visibility = View.GONE
        taskName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        taskName.setTextColor(hostingActivity?.resources!!.getColor(R.color.inactive_grey, null))
    }

    private fun dpToPx(dp: Int): Int {
        if (hostingActivity!=null) {
            return (dp * hostingActivity!!.resources.displayMetrics.density).toInt()
        }
        return 0
    }

    fun pxToDp(px: Int): Int {
        if (hostingActivity!= null) {
            return (px / hostingActivity!!.resources.displayMetrics.density).toInt()
        }
        return 0
    }
}

class TaskAdapter (private val itemList: List<Taskholder>, private val context: Context,
                   private val databaseLocation: MutableList<String>?, private val database: DatabaseReference,
                   private val hostingActivity: MainActivity) : RecyclerView.Adapter<CardViewHolder>() {
    private val viewHoldersList: MutableList<CardViewHolder> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.taskholder_card, parent, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.initializeCardViewHolder(itemList[position], context, databaseLocation, database, hostingActivity , this)
        viewHoldersList.add(holder)
    }
    fun collapseAllViewHolders() {
        viewHoldersList.forEach { unit ->
            if (unit.isExpanded) {
                unit.switchExpandedState()
            }
        }
    }
}