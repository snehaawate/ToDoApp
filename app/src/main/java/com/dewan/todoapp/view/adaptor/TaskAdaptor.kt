package com.dewan.todoapp.view.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dewan.todoapp.R
import com.dewan.todoapp.databinding.CustomTaskListViewBinding
import com.dewan.todoapp.model.local.entity.TaskEntity
import com.dewan.todoapp.model.remote.response.todo.TaskResponse
import timber.log.Timber

class TaskAdaptor(private var taskList: List<TaskEntity>) :
    RecyclerView.Adapter<TaskAdaptor.ViewHolder>() {

    private lateinit var taskCallBack: TaskCallBack

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: CustomTaskListViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.custom_task_list_view, parent, false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: TaskEntity = taskList[position]

        when (data.status) {
            "PENDING" -> {
                data.bg_color = R.color.status_pending
            }
            "STARTED" -> {
                data.bg_color = R.color.status_started
            }
            else -> {
                data.bg_color = R.color.status_completed
            }
        }

        holder.taskListBinding.task = data
        holder.setTaskCallBack(taskCallBack)

    }

    fun setTaskCallBack(taskCallBack: TaskCallBack) {
        this.taskCallBack = taskCallBack
    }

    fun setDetail(newList: List<TaskEntity>){
        val diffResult = DiffUtil.calculateDiff(TaskDiffUtil(taskList,newList))
        taskList = newList
        diffResult.dispatchUpdatesTo(this)
    }


    class ViewHolder(binding: CustomTaskListViewBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener, View.OnLongClickListener {
        var taskListBinding: CustomTaskListViewBinding = binding
        private lateinit var taskCallBack: TaskCallBack

        init {
            taskListBinding.root.setOnClickListener(this)
            taskListBinding.root.setOnLongClickListener(this)

        }

        fun setTaskCallBack(taskCallBack: TaskCallBack) {
            this.taskCallBack = taskCallBack

        }

        override fun onClick(v: View?) {
            if (v != null) {
                taskCallBack.onTaskClick(v, adapterPosition, false)
            }

        }

        override fun onLongClick(v: View?): Boolean {
            if (v != null) {
                taskCallBack.onTaskClick(v, adapterPosition, true)
            }
            return false
        }

    }

    class TaskDiffUtil(
        var oldTaskList: List<TaskEntity>,
        var newTaskList: List<TaskEntity>
    ): DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldTaskList[oldItemPosition].id == newTaskList[newItemPosition].id
        }

        override fun getOldListSize(): Int = oldTaskList.size

        override fun getNewListSize(): Int = newTaskList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return  oldTaskList[oldItemPosition] == newTaskList[newItemPosition]
        }

    }
}