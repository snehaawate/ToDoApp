package com.dewan.todoapp.view.ui.task.edit

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.dewan.todoapp.R
import com.dewan.todoapp.viewmodel.task.EditTaskViewModel
import kotlinx.android.synthetic.main.edit_task_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.anko.alert
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import org.jetbrains.anko.support.v4.alert


class EditTaskFragment : Fragment() {

    companion object {
        const val TAG = "EditTaskFragment"
    }

    private lateinit var viewModel: EditTaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.edit_task_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditTaskViewModel::class.java)
        resources.getStringArray(R.array.task_status_list).toCollection(viewModel.taskList)

        val args = EditTaskFragmentArgs.fromBundle(requireArguments())
        viewModel.id.value = args.id
        viewModel.taskId.value = args.taskId
        viewModel.title.value = args.taskTitle
        viewModel.body.value = args.taskBody
        viewModel.note.value = args.note
        viewModel.status.value = args.taskStatus


        fab_editTask.setOnClickListener {
            viewModel.status.value = edit_spinner_task.selectedItem.toString()
            viewModel.title.value = edit_title.text.toString()
            viewModel.body.value = edit_body.text.toString()
            viewModel.note.value = edit_note.text.toString()
            viewModel.editTask()
        }


        observeData()


    }

    private fun observeData(){
        viewModel.id.observe(viewLifecycleOwner, Observer {

        })

        viewModel.title.observe(viewLifecycleOwner, Observer {
            edit_title.setText(it)

        })

        viewModel.body.observe(viewLifecycleOwner, Observer {
            edit_body.setText(it)

        })

        viewModel.note.observe(viewLifecycleOwner, Observer {
            edit_note.setText(it)

        })

        viewModel.status.observe(viewLifecycleOwner, Observer {
            viewModel.getIndexFromTaskList()
            edit_spinner_task.setSelection(viewModel.index.value!!)
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            pb_editTask.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.isSuccess.observe(viewLifecycleOwner, Observer {
            if (it){
                successDialog()
            }
            else {
                unSuccessFulDialog()
            }
        })

        viewModel.isError.observe(viewLifecycleOwner, Observer {
            errorDialog(it)
        })
    }

    private fun successDialog(){
        alert {
            isCancelable = false
            title = getString(R.string.alert_success_title)
            message = getString(R.string.alert_edit_success_msg)
            positiveButton("OK"){
                it.dismiss()
                findNavController().navigate(EditTaskFragmentDirections.actionEditTaskFragmentToNavigationHome())
            }
        }.show()
    }

    private fun unSuccessFulDialog(){
        alert {
            title = getString(R.string.title_un_successful_dialog)
            message = getString(R.string.msg_add_post_un_successful)
            isCancelable = false
            positiveButton(getString(R.string.btn_ok)){dialog->
                dialog.dismiss()
            }
        }.show()
    }

    private fun errorDialog(errorMsg: String){
        alert {
            title = getString(R.string.title_error_dialog)
            message = errorMsg
            isCancelable = false
            positiveButton(getString(R.string.btn_ok)){dialog->
                dialog.dismiss()
            }
        }.show()

    }


}
