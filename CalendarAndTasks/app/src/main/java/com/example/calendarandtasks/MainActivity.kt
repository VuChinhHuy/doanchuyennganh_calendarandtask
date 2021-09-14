package com.example.calendarandtasks

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.example.calendarandtasks.databinding.ActivityMainBinding
import com.example.calendarandtasks.ui.calendar.CalendarFragmentDirections
import com.example.calendarandtasks.ui.profile.ProfileFragmentDirections
import com.example.calendarandtasks.ui.search.SearchFragmentDirections
import com.example.calendarandtasks.ui.tasks.TasksInGroupFragment
import com.example.calendarandtasks.ui.tasks.TasksInGroupFragmentDirections
import com.example.calendarandtasks.ui.tasks.TodayFragmentDirections
import com.example.calendarandtasks.util.contentView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.orhanobut.logger.Logger
import com.orhanobut.logger.AndroidLogAdapter



class MainActivity : AppCompatActivity(),

    NavController.OnDestinationChangedListener {


    private val binding: ActivityMainBinding by contentView(R.layout.activity_main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.addLogAdapter(AndroidLogAdapter())
        setBottomNavigationAndFab()
        val db = Firebase.firestore
        db.collection("groupTask")
            .get()
            .addOnSuccessListener {
                for(doc in it){
                    Logger.e("$doc")
                }
            }
            .addOnFailureListener {
                Logger.e("$it")
            }

    }
    private fun setBottomNavigationAndFab(){
        binding.run {
            findNavController(R.id.nav_host_fragment).addOnDestinationChangedListener(
                this@MainActivity
            )

        }

        binding.btnSearch.apply {
            setOnClickListener {
                navigateToSearch()

            }
        }
        binding.bottomNavView.setOnItemSelectedListener {item ->
            when(item.itemId){
                R.id.menu_calendar -> navigatToCalendar()
                R.id.menu_user -> navigateToProfile()
                R.id.menu_today -> navigateToToDay()
            }
            true
        }

    }


    private fun setBottomAppBarForSearch() {
        hideBottomAppBar()
        binding.fab.hide()
        binding.btnSearch.visibility = View.INVISIBLE
    }
    private fun setBottomAppBarForCalendar(){
        binding.apply {
            fab.setImageState(intArrayOf(-android.R.attr.state_activated), true)
            fab.setImageDrawable(getDrawable(R.drawable.ic_baseline_playlist_add_24))
            btnSearch.visibility = View.VISIBLE
            bottomAppBar.visibility = View.VISIBLE
            fab.contentDescription = getString(R.string.add_group_fab)
            bottomAppBar.performShow()
            bottomNavView.menu.getItem(3).isVisible = true
            fab.show()
            fab.setOnClickListener {
                naviageToAddGroupTask()
            }

        }
    }
    private fun setBottomAppBarForToday(){
        binding.apply {
            btnSearch.visibility = View.VISIBLE
            bottomAppBar.visibility = View.VISIBLE
            fab.contentDescription = getString(R.string.add_list_fab)
            bottomAppBar.performShow()
            fab.hide()
            bottomNavView.menu.getItem(3).isVisible = false
        }
    }
    private fun setBottomAppBarForProfile(){
        binding.apply {
            btnSearch.visibility = View.GONE
            bottomAppBar.visibility = View.VISIBLE
            fab.contentDescription = getString(R.string.edit_profile)
            bottomAppBar.performShow()
            fab.hide()
            bottomNavView.menu.getItem(3).isVisible = false
        }
    }

    private fun setBottomAppBarForTask(){
        binding.apply {
            fab.setImageDrawable(getDrawable(R.drawable.ic_add_task))
            btnSearch.visibility = View.VISIBLE
            bottomAppBar.visibility = View.VISIBLE
            fab.contentDescription = getString(R.string.add_group_fab)
            bottomAppBar.performShow()
            bottomNavView.menu.getItem(3).isVisible = true
            fab.show()
        }
    }
    private fun hideBottomAppBar() {
        binding.run {
            bottomAppBar.performHide()
            // Get a handle on the animator that hides the bottom app bar so we can wait to hide
            // the fab and bottom app bar until after it's exit animation finishes.
            bottomAppBar.animate().setListener(object : AnimatorListenerAdapter() {
                var isCanceled = false
                override fun onAnimationEnd(animation: Animator?) {
                    if (isCanceled) return

                    // Hide the BottomAppBar to avoid it showing above the keyboard
                    // when composing a new email.
                    bottomAppBar.visibility = View.GONE
                    fab.visibility = View.INVISIBLE
                }
                override fun onAnimationCancel(animation: Animator?) {
                    isCanceled = true
                }
            })
        }
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when(destination.id){
            R.id.calendarFragment->{
                setBottomAppBarForCalendar()
            }

            R.id.searchFragment -> setBottomAppBarForSearch()

            R.id.todayFragment -> setBottomAppBarForToday()

            R.id.profileFragment -> setBottomAppBarForProfile()

            R.id.taskInGroupFragment-> setBottomAppBarForTask()

            R.id.addGroupFragment -> setBottomAppBarForSearch()

            R.id.addTaskFragment -> setBottomAppBarForSearch()

            R.id.editGroupFragment -> setBottomAppBarForSearch()
        }

    }

    private fun navigatToCalendar(){
        val directions = CalendarFragmentDirections.actionGlobalCalendarFragment()
        findNavController(R.id.nav_host_fragment).navigate(directions)
    }

    private fun navigateToSearch() {
        // TODO: Set up MaterialSharedAxis transition as exit and reenter transitions.
        val directions = SearchFragmentDirections.actionGlobalSearchFragment()
        findNavController(R.id.nav_host_fragment).navigate(directions)
    }
    private fun navigateToToDay(){
        val direction = TodayFragmentDirections.actionGlobalTodayFragment()
        findNavController(R.id.nav_host_fragment).navigate(direction)
    }
    private fun navigateToProfile(){
        val direction = ProfileFragmentDirections.actionGlobalProfileFragment()
        findNavController(R.id.nav_host_fragment).navigate(direction)
    }
    private fun naviageToAddGroupTask(){
        val direction = CalendarFragmentDirections.actionCalendarFragmentToAddGroupFragment()
        findNavController(R.id.nav_host_fragment).navigate(direction)
    }
//    private fun naviageToAddTask(){
//        val direction = TasksInGroupFragmentDirections.actionTaskInGroupFragmentToAddTaskFragment()
//        findNavController(R.id.nav_host_fragment).navigate(direction)
//    }

}