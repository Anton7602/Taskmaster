package com.robkov.taskmaster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var addNewTaskButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private val itemList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindViews()
        setUpRecyclerView()

        addNewTaskButton.setOnClickListener{
            // Add a new item to the dataset
            itemList.add("NewTask")
            val newItemPosition = itemList.size - 1
            recyclerView.adapter!!.notifyItemInserted(newItemPosition)
        }



    }

    private fun bindViews() {
        addNewTaskButton = findViewById(R.id.mna_addNewTask_btn)
        recyclerView = findViewById(R.id.recyclerview)
    }

    private fun setUpRecyclerView() {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        itemList.add("1. Finish the app")
        val adapter = TaskAdapter(itemList)
        recyclerView.adapter = adapter
    }
}