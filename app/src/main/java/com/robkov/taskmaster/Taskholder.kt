package com.robkov.taskmaster

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Taskholder(var taskName: String? = null, var subtasks: HashMap<String, Taskholder>? = null) {
    var taskID: String? = null
    var isCompleted: Boolean = false

    override fun equals(other: Any?): Boolean {
        if (other !is Taskholder ) return false
        var otherTaskholder = other as Taskholder
        return (taskID == otherTaskholder.taskID)
    }
}