package com.example.health


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    val pageNumber = 3

    override fun getCount(): Int {
        return pageNumber
    }

    override fun getItem(poistion: Int): Fragment {
        return when (poistion) {
            0 -> ExcersiceFragment()
            1 -> AIFragment()
            2 -> ChatFragment()

            else -> ExcersiceFragment()

        }
    }
    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Health"
            1 -> "Ai"
            2 -> "Chat"

            else ->""
        }
    }
}