package com.robkov.taskmaster

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Taskholder(var taskName: String? = null, var subtasks: MutableList<Taskholder>? = null) {
    var taskID: String? = null
}