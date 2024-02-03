package com.robkov.taskmaster

import com.google.firebase.database.IgnoreExtraProperties
import java.sql.Time
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@IgnoreExtraProperties
class Taskholder(val taskName: String?, val listOfSubtasks: MutableList<Taskholder>?) {
    val taskID = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")).toString()
}