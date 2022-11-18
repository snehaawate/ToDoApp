package com.dewan.todoapp.model.local.entity

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "task_entity")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "taskId")
    val taskId: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "body")
    val body: String,
    @ColumnInfo(name = "status")
    val status: String,
    @ColumnInfo(name = "userId")
    val userId: Int,
    @ColumnInfo(name = "createdAt")
    val createdAt: String,
    @ColumnInfo(name = "updatedAt")
    val updatedAt: String,
    @ColumnInfo(name = "bg_color")
    var bg_color: Int = 0,
    @ColumnInfo(name = "note", defaultValue = "")
    var note: String = ""

) {

    companion object {

        @BindingAdapter("viewBackground")
        @JvmStatic
        fun TextView.setBgColor(color: Int?){
            if (color != null){
                this.setBackgroundResource(color)
            }
        }
    }
}