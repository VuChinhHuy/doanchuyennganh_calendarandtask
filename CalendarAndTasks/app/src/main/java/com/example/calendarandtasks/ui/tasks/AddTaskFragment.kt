package com.example.calendarandtasks.ui.tasks

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavArgument
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.work.*
import com.example.calendarandtasks.R
import com.example.calendarandtasks.data.firebase.grouptask.GroupTasksViewModel
import com.example.calendarandtasks.data.model.DetailsTask
import com.example.calendarandtasks.databinding.AddTasksLayoutBinding
import com.example.calendarandtasks.ui.calendar.CalendarFragmentDirections
import com.example.calendarandtasks.worker.NewTaskWorker
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.orhanobut.logger.Logger
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddTaskFragment: Fragment(),DetailsTaskDialogFragment.Listener{
    private lateinit var binding : AddTasksLayoutBinding
    private lateinit var calendar: Calendar
    private val formatDate = SimpleDateFormat("dd/MM/yyyy",Locale.getDefault())
    private val formatTime = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val args : AddTaskFragmentArgs by navArgs()
    private val groupId : String by lazy(LazyThreadSafetyMode.NONE){
        args.groupId
    }
    private var array = ArrayList<DetailsTask>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddTasksLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        binding.apply {
            pickTimeStart.setOnClickListener {
                val picker =
                    MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(calendar.get(Calendar.HOUR))
                        .setTitleText("Select time")
                        .setMinute(calendar.get(Calendar.MINUTE))
                        .build()
                picker.show(childFragmentManager,"Pick time start")
                picker.addOnPositiveButtonClickListener {
                    timeStart.text = "${picker.hour}:${picker.minute}"

                }
            }

            pickDateStart.setOnClickListener {
                val datePicker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build()
                datePicker.show(childFragmentManager,"Pick date start")
                datePicker.addOnPositiveButtonClickListener {
                    calendar.timeInMillis = datePicker.selection!!
                    dateEnd.text = "${formatDate.format(calendar.time)}"
                }
            }

            pickTimeEnd.setOnClickListener {
                val picker =
                    MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(calendar.get(Calendar.HOUR))
                        .setTitleText("Select time")
                        .setMinute(calendar.get(Calendar.MINUTE))
                        .build()
                picker.show(childFragmentManager,"Pick time end")
                picker.addOnPositiveButtonClickListener {
                    timeEnd.text = "${picker.hour}:${picker.minute}"
                }
            }

            pickDateEnd.setOnClickListener {
                val datePickerEnd =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build()
                datePickerEnd.show(childFragmentManager,"Pick date end")
                    datePickerEnd.addOnPositiveButtonClickListener {
                        calendar.timeInMillis = datePickerEnd.selection!!
                        dateEnd.text = "${formatDate.format(calendar.time)}"
                        //dateEnd.text = "${calendar.get(Calendar.DATE)}/${calendar.get(Calendar.MONTH)}/${calendar.get(Calendar.YEAR)}"
                    }
            }

            btnAdd.setOnClickListener {
                val title = binding.titleAddTask.text.toString()
                if(TextUtils.isEmpty(binding.titleAddTask.text.toString().trim())){
                    binding.titleAddTask.error = "Please enter title"
                }else if(title.length<3){
                    binding.titleAddTask.error = "Title must be at least 3 characters"
                }
                else{
                    addTask()
                }
            }
            btnReturn.setOnClickListener {
                findNavController().navigateUp()
            }
            addDetails.setOnClickListener {
//               val dialog = DetailsTaskDialogFragment()
//                dialog.show(childFragmentManager,null)
                if(array.isEmpty()){
                    DetailsTaskDialogFragment.newInstance().show(childFragmentManager,null)
                }
                else
                    DetailsTaskDialogFragment.newInstance(array).show(childFragmentManager,null)
            }
        }
    }


    override fun onStart() {
        super.onStart()
        calendar = Calendar.getInstance()
        val date = formatDate.format(calendar.time)
        val time = formatTime.format(calendar.time)
        binding.run {
            timeStart.text = "$time"
            timeEnd.text = "$time"
            dateStart.text = "$date"
            dateEnd.text ="$date"
        }
    }

    fun addTask(){
        val arr = Array<String>(array.size){""}
        array.map {
            arr.set(array.indexOf(it),it.details)
        }
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val newTask : OneTimeWorkRequest = OneTimeWorkRequestBuilder<NewTaskWorker>()
            .setConstraints(constraints)
            .setInputData(
                workDataOf(
                    NewTaskWorker.KEY_TITLE to binding.titleAddTask.text.toString(),
                    NewTaskWorker.KEY_GROUP_ID to groupId,
                    NewTaskWorker.KEY_NOTE to binding.noteAddTask.text.toString(),
                    NewTaskWorker.KEY_TIME_START to "${binding.timeStart.text}",
                    NewTaskWorker.KEY_DATE_START to "${binding.dateStart.text}",
                    NewTaskWorker.KEY_DATE_END to "${binding.dateEnd.text}",
                    NewTaskWorker.KEY_TIME_END to "${binding.timeEnd.text}",
                    NewTaskWorker.KEY_LOCATION to binding.location.text.toString(),
                    NewTaskWorker.KEY_NOTIFICATION to binding.chipNotification.isChecked,
                    NewTaskWorker.KEY_DETAILS to arr
                )
            )
            .build()
        WorkManager.getInstance(requireContext()).let {
            it.beginWith(newTask)
                .enqueue()
            findNavController().navigateUp()
        }

    }

    override fun onBtnClick(detail: ArrayList<DetailsTask>) {
        array = detail
    }


}

