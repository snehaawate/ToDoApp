package com.dewan.todoapp.view.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dewan.todoapp.R
import com.dewan.todoapp.databinding.CustomTaskListViewBinding
import com.dewan.todoapp.model.local.entity.TaskEntity
import com.dewan.todoapp.model.remote.response.todo.TaskResponse
import timber.log.Timber

class TaskListAdaptor :
    ListAdapter<TaskEntity,TaskListAdaptor.ViewHolder>(TaskDiffUtilItemCallback()) {

    private lateinit var taskCallBack: TaskCallBack

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: CustomTaskListViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.custom_task_list_view, parent, false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int  = currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: TaskEntity = currentList[position]

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

    class TaskDiffUtilItemCallback : DiffUtil.ItemCallback<TaskEntity>() {

        override fun areItemsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean {
            return  oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean {
            return  oldItem == newItem
        }


    }
}