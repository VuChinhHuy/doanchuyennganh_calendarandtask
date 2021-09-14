package com.example.calendarandtasks.ui.calendar

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.work.*
import com.example.calendarandtasks.R
import com.example.calendarandtasks.data.firebase.grouptask.GroupTasksViewModel
import com.example.calendarandtasks.data.firebase.Result
import com.example.calendarandtasks.data.model.Group
import com.example.calendarandtasks.databinding.FragmentEditGroupBinding
import com.example.calendarandtasks.util.IconButton
import com.example.calendarandtasks.worker.EditGroupWorker
import com.google.android.material.chip.Chip

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditGroupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditGroupFragment : Fragment() {

    private lateinit var binding : FragmentEditGroupBinding
    private val args : EditGroupFragmentArgs by navArgs()
    private val groupId : String by lazy(LazyThreadSafetyMode.NONE) { args.groupId }

    private val model : GroupTasksViewModel by navGraphViewModels<GroupTasksViewModel>(R.id.navigation_graph)



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditGroupBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewEditGroup.btnReturn.setOnClickListener {
            findNavController().navigateUp()
        }
        val title = binding.viewEditGroup.viewTransition.title
        binding.viewEditGroup.color.apply {
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
                        binding.viewEditGroup.color.removeView(chip as View)

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
                    binding.viewEditGroup.viewTransition.cardView.setCardBackgroundColor(chip.chipBackgroundColor)
                }
            }
        }
        binding.viewEditGroup.icon.apply {
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
                        binding.viewEditGroup.viewTransition.image.setImageResource(i)
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
        binding.viewEditGroup.titleAdd.apply {
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
        binding.viewEditGroup.btnAdd.setOnClickListener {
            saveEditGroup()
        }
    }

    override fun onStart() {
        super.onStart()
        loadGroup()
    }
    fun loadGroup(){
        binding.viewEditGroup.title.text = "Edit Group"
        binding.viewEditGroup.btnAdd.text = "Done"
        model.getOneGroup(groupId).observe(requireActivity(), Observer {
            if(it is Result.Success) {
                val group : Group = it.data
                binding.viewEditGroup.viewTransition.group = group
            }
        })

    }
    fun saveEditGroup(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val newGroup : OneTimeWorkRequest = OneTimeWorkRequestBuilder<EditGroupWorker>()
            .setConstraints(constraints)
            .setInputData(
                workDataOf(
                    EditGroupWorker.KEY_GROUP_ID to groupId,
                    EditGroupWorker.KEY_TITLE to binding.viewEditGroup.viewTransition.title.text.toString(),
                    EditGroupWorker.KEY_COUNT to binding.viewEditGroup.viewTransition.countTasks.text.toString().toInt(),
                    EditGroupWorker.KEY_COLOR to binding.viewEditGroup.viewTransition.cardView.cardBackgroundColor.defaultColor

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