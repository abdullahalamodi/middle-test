package com.abdullahalamodi.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.abdullahalamodi.todoapp.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
const val TABS_NUM = 3
class MainFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var fragmentTabsAdapter: FragmentTabsAdapter
    private val tabLayoutArray = arrayOf("Todo", "InProgress", "Done")



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        viewPager = view.findViewById(R.id.view_pager)
        fragmentTabsAdapter = FragmentTabsAdapter(this)
        viewPager.adapter = fragmentTabsAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabLayoutArray[position]
//            tab.setIcon()
        }.attach()

        return view;
    }

    class FragmentTabsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = TABS_NUM
        override fun createFragment(position: Int): Fragment {
            val state = position + 1;
            return TasksListFragment.newInstance(state);
        }
    }




    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}