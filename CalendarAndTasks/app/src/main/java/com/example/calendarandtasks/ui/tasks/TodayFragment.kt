package com.example.calendarandtasks.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.example.calendarandtasks.R
import com.example.calendarandtasks.data.firebase.Result
import com.example.calendarandtasks.data.firebase.grouptask.GroupTasksViewModel
import com.example.calendarandtasks.data.firebase.task.TaskViewModel
import com.example.calendarandtasks.databinding.TodayFragmentBinding
import com.example.calendarandtasks.util.adapter.TaskTodayAdapter
import java.util.*


class TodayFragment: Fragment() {
    private lateinit var calendar: Calendar
    private lateinit var binding: TodayFragmentBinding
    private var adapter = TaskTodayAdapter()
    private val viewModel : TaskViewModel by navGraphViewModels<TaskViewModel>(R.id.navigation_graph)
    companion object {
        @JvmStatic
        fun newInstance() = TodayFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TodayFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        calendar = Calendar.getInstance()
        val date = calendar.get(Calendar.DATE)
        val month = calendar.getDisplayName(Calendar.MONTH,Calendar.LONG,Locale.getDefault())
        val year = calendar.get(Calendar.YEAR)
        val day = calendar.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG,Locale.getDefault())
        binding.apply {
            dayTask.text = day
            dateTask.text = date.toString()
            monthYearTask.text = "$month, $year"
        }
        binding.recyclerTasks.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        loadTaskToday()
    }
    fun loadTaskToday(){
        viewModel.getTaskToday().observe(requireActivity(), androidx.lifecycle.Observer {
            if(it is Result.Success){
                adapter.submitList(it.data)
            }
        })
    }

}