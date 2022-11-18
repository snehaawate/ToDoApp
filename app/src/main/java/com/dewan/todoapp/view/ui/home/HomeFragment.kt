package com.dewan.todoapp.view.ui.home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.dewan.todoapp.R
import com.dewan.todoapp.databinding.HomeFragmentBinding
import com.dewan.todoapp.model.local.entity.TaskEntity
import com.dewan.todoapp.view.adaptor.TaskAdaptor
import com.dewan.todoapp.view.adaptor.TaskAdaptorAsyncListDiffer
import com.dewan.todoapp.view.adaptor.TaskCallBack
import com.dewan.todoapp.view.adaptor.TaskListAdaptor
import com.dewan.todoapp.viewmodel.home.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.home_fragment.*
import org.jetbrains.anko.support.v4.alert
import timber.log.Timber


class HomeFragment : Fragment(), TaskCallBack {

    companion object {
        const val TAG = "HomeFragment"
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: HomeFragmentBinding
    private lateinit var recycleView: RecyclerView
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    //private lateinit var mTaskAdaptor: TaskAdaptor
    //private lateinit var mTaskAdaptorAsyncListDiffer: TaskAdaptorAsyncListDiffer
    private lateinit var mTaskListAdaptor: TaskListAdaptor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.home_fragment,container,false)

        //recycle view
        recycleView = binding.taskRecyclerView
        //mTaskAdaptor = TaskAdaptor(listOf())
        //mTaskAdaptorAsyncListDiffer = TaskAdaptorAsyncListDiffer()

        mTaskListAdaptor = TaskListAdaptor()

        mLayoutManager = LinearLayoutManager(context)
        recycleView.apply {
            layoutManager = mLayoutManager
            adapter = mTaskListAdaptor
        }
        mTaskListAdaptor.setTaskCallBack(this)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        observer()

    }

    override fun onTaskClick(view: View, position: Int, isLongClick: Boolean) {

        if (isLongClick ){
            Timber.e("Position: $position is a long click")
        }
        else {
            val data = viewModel.taskListFromDb.value?.get(position)
            findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToTaskDetailFragment(
                data?.createdAt.toString(),
                data?.title.toString(),
                data?.body.toString(),
                data?.status.toString(),
                data?.userId.toString(),
                data?.bg_color.toString(),
                data?.id.toString(),
                data?.taskId.toString(),
                data?.note.toString()
            ))
            Timber.e("Position: $position is a single click")
        }
    }

    private fun observer(){
        viewModel.isError.observe(viewLifecycleOwner, Observer {
            errorDialog(it)
        })

        viewModel.progress.observe(viewLifecycleOwner, Observer {
            pb_home.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.taskListFromDb.observe(viewLifecycleOwner, Observer {
            mTaskListAdaptor.submitList(it)

        })

        viewModel.errorMsgInt.observe(viewLifecycleOwner, Observer {
            it?.let {
                showSnackBarWithResourceId(it)
            }
        })

        viewModel.errorMsgString.observe(viewLifecycleOwner, Observer {
            it?.let {
                showSnackBarWithString(it)
            }

        })
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

    private fun showSnackBarWithResourceId(resId: Int){
        Snackbar.make(requireView(),getString(resId), Snackbar.LENGTH_INDEFINITE)
            .apply {
                setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red))
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    .setAction(getString(R.string.sncakbar_close)) {

                    }
                    .show()
            }
    }

    private fun showSnackBarWithString(msg: String){
        Snackbar.make(requireView(),msg, Snackbar.LENGTH_INDEFINITE)
            .apply {
                setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red))
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    .setAction(getString(R.string.sncakbar_close)) {

                    }
                    .show()
            }
    }


}
