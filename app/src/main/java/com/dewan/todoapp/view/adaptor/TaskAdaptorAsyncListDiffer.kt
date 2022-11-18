package com.dewan.todoapp.view.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dewan.todoapp.R
import com.dewan.todoapp.databinding.CustomTaskListViewBinding
import com.dewan.todoapp.model.local.entity.TaskEntity


class TaskAdaptorAsyncListDiffer() :
    RecyclerView.Adapter<TaskAdaptorAsyncListDiffer.ViewHolder>() {

    private lateinit var taskCallBack: TaskCallBack

    private val diffUtilCallback = object : DiffUtil.ItemCallback<TaskEntity>(){

        override fun areItemsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean {
            return  oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean {
            return oldItem == newItem
        }

    }

    private val asyncListDiffer = AsyncListDiffer(this,diffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: CustomTaskListViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.custom_task_list_view, parent, false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: TaskEntity = asyncListDiffer.currentList[position]

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
        asyncListDiffer.submitList(newList)
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

}