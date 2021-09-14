package com.example.calendarandtasks.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.work.*
import com.example.calendarandtasks.R
import com.example.calendarandtasks.data.firebase.grouptask.GroupTaskRepository
import com.example.calendarandtasks.ui.calendar.CalendarFragment
import com.example.calendarandtasks.ui.calendar.CalendarFragmentDirections
import com.example.calendarandtasks.ui.tasks.TasksInGroupFragmentDirections
import com.example.calendarandtasks.worker.DeleteGroupWorker
import com.example.calendarandtasks.worker.DeleteTaskWorker
import com.example.calendarandtasks.worker.NewTaskWorker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView

class MenuBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var navigationView: NavigationView
    @MenuRes private var menuResId: Int = 0
    private var Id : String = ""
    private var taskId: String = ""
    private var titleGroup : String= ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        menuResId = arguments?.getInt(KEY_MENU_RES_ID,0)?:0
        Id = arguments?.getString(KEY_ID)?:""
        taskId = arguments?.getString(KEY_ID_TASK)?:""
        titleGroup = arguments?.getString("KEY_TITLE_GROUP")?:""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.menu_bottom_sheet_dialog_layout,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationView = view.findViewById(R.id.navigation_view)
        navigationView.inflateMenu(menuResId)
        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu_edit_group->{
                    findNavController().navigate(CalendarFragmentDirections.actionCalendarFragmentToEditGroupFragment(Id))
                }
                R.id.menu_delete_group->{
                    deleteGroup()
                }
                R.id.menu_delete_task->{
                    deleteTask()
                }
            }
            dismiss()
            true
        }
    }

    companion object {

        private const val KEY_MENU_RES_ID = "MenuBottomSheetDialogFragment_menuResId"
        private const val KEY_ID ="key-id"
        private const val KEY_ID_TASK = "key-id-task"

        fun newInstance(@MenuRes menuResId: Int,groupId :String): MenuBottomSheetDialogFragment {
            val fragment = MenuBottomSheetDialogFragment()
            val bundle = Bundle().apply {
                putInt(KEY_MENU_RES_ID, menuResId)
                putString(KEY_ID,groupId)
            }
            fragment.arguments = bundle
            return fragment
        }

        fun newInstance(@MenuRes menuResId: Int,groupId :String,taskId: String,titleGroup: String): MenuBottomSheetDialogFragment {
            val fragment = MenuBottomSheetDialogFragment()
            val bundle = Bundle().apply {
                putInt(KEY_MENU_RES_ID, menuResId)
                putString(KEY_ID,groupId)
                putString(KEY_ID_TASK,taskId)
                putString("KEY_TITLE_GROUP",titleGroup)
            }
            fragment.arguments = bundle
            return fragment
        }

    }

    fun deleteGroup(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val deleteGroup : OneTimeWorkRequest = OneTimeWorkRequestBuilder<DeleteGroupWorker>()
            .setConstraints(constraints)
            .setInputData(
                workDataOf(
                    DeleteGroupWorker.KEY_GROUP_ID to Id
                )
            )
            .build()
        WorkManager.getInstance(requireContext()).let {
            it.beginWith(deleteGroup)
                .enqueue()
            findNavController().navigate(CalendarFragmentDirections.actionGlobalCalendarFragment())

        }
    }

    fun deleteTask(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val deletTask : OneTimeWorkRequest = OneTimeWorkRequestBuilder<DeleteTaskWorker>()
            .setConstraints(constraints)
            .setInputData(
                workDataOf(
                    DeleteTaskWorker.KEY_GROUP_ID to Id,
                    DeleteTaskWorker.KEY_TASK_ID to taskId
                )
            )
            .build()
        WorkManager.getInstance(requireContext()).let {
            it.beginWith(deletTask)
                .enqueue()
            findNavController().navigateUp()
            findNavController().navigate(CalendarFragmentDirections.actionCalendarFragmentToTaskInGroupFragment(Id,titleGroup))
        }
    }
}