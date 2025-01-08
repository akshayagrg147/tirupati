package com.tirupati.vendor.model

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tirupati.vendor.fragmnts.CustomerComplaintTabFragment
import com.tirupati.vendor.fragmnts.OrderStatusTabFragment

class OrderStatusPagerAdapter(fragmentActivity: FragmentActivity, private val onNavigate: (Int, Bundle?) -> Unit ) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2 // Number of tabs
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OrderStatusTabFragment().apply {
                setOnNavigateListener(onNavigate)
            }
            else -> CustomerComplaintTabFragment() // Second Tab
        }
    }
}

