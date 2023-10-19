package com.example.cryptoo.Adapter

import android.app.Fragment
import android.app.FragmentManager
import android.content.Context
import androidx.fragment.app.FragmentPagerAdapter

class ViewpagerAdapter (private val context : Context, fm : androidx.fragment.app.FragmentManager?, val list : ArrayList<androidx.fragment.app.Fragment>) : FragmentPagerAdapter(fm!!) {
    val tab_titles = arrayOf("Chats" , "Status" , "Call")

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return list[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tab_titles[position]
    }


}