package com.example.newtodolist.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "category") var category: String,
    @ColumnInfo(name = "point_color") var pointColor: String,
    @ColumnInfo(name = "created_at") var createdAt: Date,
    @ColumnInfo(name = "due_date") var dueDate: Date,
    )

