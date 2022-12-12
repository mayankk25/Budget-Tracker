package com.example.budgettracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.budgettracker.databinding.AddTransactionFragmentBinding
import com.google.android.material.tabs.TabLayoutMediator

class AddTransactionFragment: Fragment() {

    private val TAG = "AddTransactionFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: AddTransactionFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.add_transaction_fragment, container, false)

        binding.viewPager.adapter = MyTabAdapter(this)
        val tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Income"
                1 -> "Expenses"
                else -> throw RuntimeException("Invalid position: $position")
            }
            tab.icon = when (position) {
                0 -> context?.let { getDrawable(it,R.drawable.ic_baseline_arrow_circle_up_24 ) }
                1 -> context?.let { getDrawable(it,R.drawable.ic_baseline_arrow_circle_down_24 ) }
                else -> throw RuntimeException("Invalid position: $position")
            }
        }
        tabLayoutMediator.attach()

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigate(AddTransactionFragmentDirections.actionAddTransactionFragmentToMainFragment())
        }
        return binding.root
    }

    class MyTabAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return 2
        }
        override fun createFragment(position: Int): Fragment {
            return when(position) {
                0 -> AddIncomeFragment()
                1 -> AddExpenseFragment()
                else -> throw RuntimeException("Invalid position: $position")
            }
        }
    }
}
