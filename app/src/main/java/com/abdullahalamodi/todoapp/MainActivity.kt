package com.abdullahalamodi.todoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.abdullahalamodi.todoapp.fragments.MainFragment
import com.abdullahalamodi.todoapp.fragments.TaskFragment
import com.abdullahalamodi.todoapp.fragments.TasksListFragment
import java.util.*


class MainActivity : AppCompatActivity() , TasksListFragment.Callbacks {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null){
            val fragment = MainFragment.newInstance();
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container,fragment)
                .commit()
        }

    }
    //from tasksListFragment and tasksListAdapter also :)
    override fun onTaskSelected(taskId: UUID) {
        val fragment = TaskFragment.newInstance(taskId);
        addFragment(fragment)
    }


    private fun addFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container,fragment)
            .addToBackStack(null)
            .commit()
    }

}