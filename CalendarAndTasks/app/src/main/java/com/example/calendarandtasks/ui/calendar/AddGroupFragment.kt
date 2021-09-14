package com.example.calendarandtasks.ui.calendar

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Color.*
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.work.*
import com.example.calendarandtasks.R
import com.example.calendarandtasks.databinding.AddGroupTasksLayoutBinding
import com.example.calendarandtasks.util.IconButton
import com.example.calendarandtasks.worker.NewGroupWorker
import com.google.android.material.chip.Chip
import com.orhanobut.logger.Logger

class AddGroupFragment: Fragment() {
    private lateinit var binding : AddGroupTasksLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.add_group_tasks_layout, container, false)
        return binding.root
    }

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnReturn.setOnClickListener {
            findNavController().navigateUp()
        }
        val title = binding.viewTransition.title
        binding.color.apply {
            val color = mutableSetOf<Int>(
                R.color.red_400,
                R.color.pink_500,
                R.color.purple_500,
                R.color.deepPurple_500,
                R.color.indigo_500,
                R.color.lightBlue_500,
                R.color.cyan_500,
                R.color.yellow_500,
                R.color.orange_500,
                R.color.brown_500
                )
            for (i in color) {
                var chip = IconButton(context)
                chip.apply {
                    setChipBackgroundColorResource(i)
                    setChipIconResource(R.drawable.color)
                    isClickable = true
                    isCheckable = true
                    addView(chip)
                    setOnCloseIconClickListener{
                        binding.color.removeView(chip as View)

                    }
                    setOnCheckedChangeListener { buttonView, isChecked ->
                        chipStrokeWidth = 0F

                    }
                }
            }

            setOnCheckedChangeListener { group, checkedId ->
                val chip: Chip? = group.findViewById(checkedId)
                if (chip?.isChecked == true) {
                    chip.chipStrokeWidth = 5.0F
                    chip.setChipStrokeColorResource(R.color.white_50)
                    binding.viewTransition.cardView.setCardBackgroundColor(chip.chipBackgroundColor)
                }
            }
        }
        binding.icon.apply {
            val iconList = mutableSetOf<Int>(
                R.drawable.ic_list,
                R.drawable.ic_balloons,
                R.drawable.ic_book,
                R.drawable.ic_analytics,
                R.drawable.ic_computer,
                R.drawable.ic_suitcase,
                R.drawable.ic_diet,
                R.drawable.ic_exercise,
                R.drawable.ic_football_player,
                R.drawable.ic_stethoscope,
                R.drawable.ic_working,
            )
            for(i in iconList){
                val chip = IconButton(context)
                chip.apply {
                    isClickable = true
                    isCheckable = true
                    chip.height = 120
                    chip.width = 120
                    text = null
                    setChipIconResource(i)
                    addView(chip)
                    setOnClickListener {
                        binding.viewTransition.image.setImageResource(i)
                        binding.viewTransition.image.setTag(i)
                    }
                    setOnCheckedChangeListener { buttonView, isChecked ->
                        chipStrokeWidth = 0F
                    }
                }

            }
            setOnCheckedChangeListener { group, checkedId ->
                val chip: Chip? = group.findViewById(checkedId)
                if (chip?.isChecked == true) {
                    chip.chipStrokeWidth = 5.0F
                    chip.setChipStrokeColorResource(R.color.white_50)
                }
            }
        }
        binding.titleAdd.apply {
            addTextChangedListener(object: TextWatcher {

                override fun afterTextChanged(s: Editable) {}

                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {
                    title.text = s
                }
            })
        }

        binding.btnAdd.apply {

            setOnClickListener {
                if(TextUtils.isEmpty(binding.titleAdd.text.toString().trim())){
                    binding.titleAdd.error = "Please enter title"
                }else if(title.length()<3){
                    binding.titleAdd.error = "Title must be at least 3 characters"
                }
                else{
                    addGroup()
                }
            }
        }
    }

    private fun addGroup() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val newGroup :OneTimeWorkRequest = OneTimeWorkRequestBuilder<NewGroupWorker>()
            .setConstraints(constraints)
            .setInputData(
                workDataOf(
                    NewGroupWorker.KEY_TITLE to binding.titleAdd.text.toString(),
                    NewGroupWorker.KEY_COUNT to 0,
                    NewGroupWorker.KEY_COLOR to binding.viewTransition.cardView.cardBackgroundColor.defaultColor,
                    NewGroupWorker.KEY_ICON to binding.viewTransition.image.tag
                )

            )
            .build()
        WorkManager.getInstance(requireContext()).let {
            it.beginWith(newGroup)
                .enqueue()
            findNavController().navigate(CalendarFragmentDirections.actionGlobalCalendarFragment())
            }
        }
    }



