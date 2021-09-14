package com.example.calendarandtasks.ui.tasks

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calendarandtasks.data.model.DetailsTask
import com.example.calendarandtasks.databinding.DialogFragmentDetailsTaskBinding
import com.example.calendarandtasks.util.adapter.DetailAddTaskAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.orhanobut.logger.Logger
import java.lang.ClassCastException


class DetailsTaskDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding : DialogFragmentDetailsTaskBinding
    private  var array : ArrayList<DetailsTask> = ArrayList()
    private lateinit var arrayAdapter: DetailAddTaskAdapter

    private lateinit var listener : Listener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arrayArgrument = arguments?.getStringArrayList("detail")
        arrayArgrument?.map {
            val detail = DetailsTask(it)
            array.add(detail)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentDetailsTaskBinding.inflate(inflater,container,false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCreateDialog(savedInstanceState)
        onAttachToParentFragment(parentFragment)
        arrayAdapter = DetailAddTaskAdapter(array)
        binding.recDetails.adapter = arrayAdapter
        binding.textField.setEndIconOnClickListener {
            val detail = binding.inputDetail.text.toString().trim()
            if (!TextUtils.isEmpty(detail)){
                array.add(DetailsTask(detail))
                arrayAdapter.notifyDataSetChanged()
            }
        }
        binding.btnDone.setOnClickListener {
            //viewModel.sendArray(array)
            listener.onBtnClick(array)
            dismiss()
        }

    }
    fun onAttachToParentFragment(fragment: Fragment?){
        try{
            listener = (fragment as Listener)

        }catch (e : ClassCastException){
            Logger.e("$e")
        }
    }

    interface Listener{
        fun onBtnClick(detail: ArrayList<DetailsTask>)
    }





    companion object{
        fun newInstance(details : ArrayList<DetailsTask>): DetailsTaskDialogFragment {
            val fragment = DetailsTaskDialogFragment()
            val bundle = Bundle().apply {
                val arr = ArrayList<String>()
                details.map {
                    arr.add(it.details)
                }
                putStringArrayList("detail",arr)

            }
            fragment.arguments = bundle
            return fragment
        }
        fun newInstance() = DetailsTaskDialogFragment()

    }
}