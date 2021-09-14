package com.example.calendarandtasks.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.calendarandtasks.R
import com.example.calendarandtasks.data.firebase.Result
import com.example.calendarandtasks.data.firebase.task.TaskViewModel
import com.example.calendarandtasks.data.model.Task
import com.example.calendarandtasks.databinding.FragmentTasksInGroupBinding
import com.example.calendarandtasks.ui.MenuBottomSheetDialogFragment
import com.example.calendarandtasks.ui.calendar.CalendarFragmentDirections
import com.example.calendarandtasks.util.adapter.TasksAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TasksInGroupFragment : Fragment(),TasksAdapter.TasksAdapterListener,PopupMenu.OnMenuItemClickListener {
    private lateinit var binding : FragmentTasksInGroupBinding
    private var adapter = TasksAdapter(this)
    private val args: TasksInGroupFragmentArgs by navArgs()
    private val groupId : String by lazy(LazyThreadSafetyMode.NONE) { args.groupId }
    private val groupTitle: String by lazy (LazyThreadSafetyMode.NONE){ args.groupTitle}
    private val model : TaskViewModel by lazy {
        TaskViewModel()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTasksInGroupBinding.inflate(inflater,container,false)
        binding.rcvTasks.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnReturn.setOnClickListener {
            findNavController().navigate(CalendarFragmentDirections.actionGlobalCalendarFragment())
        }

        binding.apply {
            titleGroup.text = groupTitle
            btnMenu.setOnClickListener {
                val popupMenu : PopupMenu = PopupMenu(activity,btnMenu)
                popupMenu.setOnMenuItemClickListener(this@TasksInGroupFragment)
                popupMenu.inflate(R.menu.task_in_group_menu)
                popupMenu.show()

            }
        }
        val fab = activity?.findViewById<FloatingActionButton>(R.id.fab)
        fab?.setOnClickListener {
            findNavController().navigate(TasksInGroupFragmentDirections.actionTaskInGroupFragmentToAddTaskFragment(groupId))
        }
    }

    override fun onStart() {
        super.onStart()
        loadTask()
    }

    fun loadTask(){
        model.loadTaskInGroup(groupId).observe(requireActivity(), Observer {
            if(it is Result.Success) {
                adapter.submitList(it.data)

            }
        })
    }

    override fun onClicked(cardView: View, task: Task) {
        TODO("Not yet implemented")
        //do nothing

    }

    override fun onTaskLongPressed(cardView: View,task: Task): Boolean {
        MenuBottomSheetDialogFragment
            .newInstance(R.menu.task_bottom_sheet_menu,groupId,task.id,groupTitle)
            .show(parentFragmentManager, null)
        return true
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.task_all_menu ->{

            }
            R.id.task_completed_menu->{

            }
            R.id.task_incomplete_menu->{

            }
        }
        return true
    }
}

