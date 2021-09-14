package com.example.calendarandtasks.ui.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.calendarandtasks.R
import com.example.calendarandtasks.data.firebase.grouptask.GroupTasksViewModel
import com.example.calendarandtasks.databinding.FragmentCalendarBinding
import com.example.calendarandtasks.data.firebase.Result
import com.example.calendarandtasks.data.model.Group
import com.example.calendarandtasks.ui.MenuBottomSheetDialogFragment
import com.example.calendarandtasks.util.adapter.GroupAdapter

class CalendarFragment : Fragment(),GroupAdapter.GroupAdapterListener {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentCalendarBinding
    private  val adapter = GroupAdapter(this)
    private val model : GroupTasksViewModel by navGraphViewModels<GroupTasksViewModel>(R.id.navigation_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerGroupTask.adapter = adapter
        loadGroup()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false)
        return  binding.root
    }
    fun loadGroup(){
        model.loadGroup().observe(requireActivity(), Observer {
            if(it is Result.Success){
                adapter.submitList(it.data)
            }
        })

    }

    override fun onClicked(cardView: View, group: Group) {
        val direction = CalendarFragmentDirections.actionCalendarFragmentToTaskInGroupFragment(group.id,group.titleGroup)
        findNavController().navigate(direction)
    }

    override fun onGroupLongPressed(group: Group) :Boolean {
        MenuBottomSheetDialogFragment
            .newInstance(R.menu.group_bottom_sheet_menu,group.id)
            .show(parentFragmentManager, null)
        return true
    }
}