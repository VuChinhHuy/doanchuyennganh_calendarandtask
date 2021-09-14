package com.example.calendarandtasks.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.example.calendarandtasks.R
import com.example.calendarandtasks.databinding.FragmentWelcomeBinding

class WelcomeFragment:Fragment() {
    private lateinit var binding : FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWelcomeBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.topView.animation =  AnimationUtils.loadAnimation(requireContext(), R.anim.topamination)
        binding.imageStart.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.body)
        binding.txtAppName.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.body)
        binding.txtSlogan.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.body)
        binding.btnStart.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.body)
        binding.btnStart.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
    }
}