package com.dewan.todoapp.view.adaptor

import android.view.View

interface TaskCallBack {

    fun onTaskClick(view: View, position:Int, isLongClick: Boolean)
}