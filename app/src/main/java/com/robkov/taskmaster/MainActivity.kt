package com.robkov.taskmaster

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.database.getValue

class MainActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var addNewTaskButton: FloatingActionButton
    private lateinit var acceptNewTaskButton: FloatingActionButton
    private lateinit var cancelNewTaskButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var newTaskCardView: CardView
    private lateinit var taskNameEditText: EditText
    private val itemList = mutableListOf<Taskholder>()
    private val taskholderList = mutableListOf<Taskholder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindViews()
        setUpRecyclerView()

        database.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val taskholder = snapshot.getValue<Taskholder>()
                if (taskholder!=null) {
                    taskholder.taskID = snapshot.key
                    itemList.add(taskholder)
                    recyclerView.adapter!!.notifyItemInserted(itemList.size - 1)
                    Log.d("Debug","onChildAddedTriggered:" + snapshot.key + " " + taskholder.taskName)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        addNewTaskButton.setOnClickListener{
            switchButtonsVisibility()
            taskNameEditText.showKeyboard()
        }

        cancelNewTaskButton.setOnClickListener{
            switchButtonsVisibility()
            taskNameEditText.hideKeyboard()
            taskNameEditText.setText("")
        }

        acceptNewTaskButton.setOnClickListener{
            //itemList.add(taskNameEditText.text.toString())
            //recyclerView.adapter!!.notifyItemInserted(itemList.size - 1)
            database.push().setValue(Taskholder(taskNameEditText.text.toString(), mutableListOf<Taskholder>()))
            taskNameEditText.hideKeyboard()
            taskNameEditText.setText("")
            switchButtonsVisibility()
        }
    }

    private fun switchButtonsVisibility() {
        if (addNewTaskButton.visibility != View.VISIBLE) {
            addNewTaskButton.visibility = View.VISIBLE
            acceptNewTaskButton.visibility = View.INVISIBLE
            cancelNewTaskButton.visibility = View.INVISIBLE
            newTaskCardView.visibility = View.INVISIBLE
        } else {
            addNewTaskButton.visibility = View.INVISIBLE
            acceptNewTaskButton.visibility = View.VISIBLE
            cancelNewTaskButton.visibility = View.VISIBLE
            newTaskCardView.visibility = View.VISIBLE
        }
    }

    private fun EditText.showKeyboard() {
        requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun EditText.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }

    private fun bindViews() {
        val test = intent.getStringExtra("PressedItem")
        database = Firebase.database.getReference("Tasks")
        if (test!=null) {
            Log.d("DEBUG", "Child name: $test")
            //database = database.child(test)
        }
        addNewTaskButton = findViewById(R.id.mna_addNewTask_btn)
        acceptNewTaskButton = findViewById(R.id.mna_acceptNewTask_btn)
        cancelNewTaskButton = findViewById(R.id.mna_cancelNewTask_btn)
        newTaskCardView = findViewById(R.id.mna_taskholder_cdv)
        taskNameEditText = findViewById(R.id.mna_taskNameEdit_edt)
        recyclerView = findViewById(R.id.recyclerview)
    }

    private fun setUpRecyclerView() {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = TaskAdapter(itemList, this)
        recyclerView.adapter = adapter
    }
}