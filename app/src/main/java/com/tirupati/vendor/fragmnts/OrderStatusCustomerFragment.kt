package com.tirupati.vendor.fragmnts

import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import androidx.viewpager2.widget.ViewPager2
import com.itextpdf.kernel.colors.Color
import com.tirupati.vendor.R
import com.tirupati.vendor.model.OrderStatusPagerAdapter

class OrderStatusCustomerFragment : Fragment(R.layout.fragment_order_status_customer) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)
        val viewPager: ViewPager2 = view.findViewById(R.id.viewPager)

        // Set up the adapter for ViewPager2
        viewPager.adapter = OrderStatusPagerAdapter(requireActivity())
        tabLayout.getTabAt(0)?.select()
        // Linking TabLayout and ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Order Status"
                else -> "Customer Complaint"
            }
        }.attach()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.view.background = ContextCompat.getDrawable(requireContext(), R.drawable.curved_tab_background)

               // tab.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryVariant))

                // Set text color to colorPrimary (White) for better visibility
                tabLayout.setTabTextColors(
                    ContextCompat.getColor(requireContext(), R.color.black),
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // Change background color back to the default when unselected
                tab.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Can handle reselected tab here if needed
            }
        })
        val firstTab = tabLayout.getTabAt(0)
        firstTab?.let {
            // Set background and text colors for the first tab
            it.view.background = ContextCompat.getDrawable(requireContext(), R.drawable.curved_tab_background)
            tabLayout.setTabTextColors(
                ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark), // Unselected text color
                ContextCompat.getColor(requireContext(), R.color.colorPrimary) // Selected text color (White)
            )
        }
    }
}
