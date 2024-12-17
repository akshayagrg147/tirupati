package com.tirupati.vendor.model

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tirupati.vendor.fragmnts.CustomerComplaintTabFragment
import com.tirupati.vendor.fragmnts.OrderStatusTabFragment

class OrderStatusPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2 // Number of tabs
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OrderStatusTabFragment() // First Tab
            else -> CustomerComplaintTabFragment() // Second Tab
        }
    }
}

