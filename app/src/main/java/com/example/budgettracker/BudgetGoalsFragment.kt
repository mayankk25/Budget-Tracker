package com.example.budgettracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.budgettracker.databinding.AddTransactionFragmentBinding
import com.example.budgettracker.databinding.BudgetGoalsFragmentBinding

class BudgetGoalsFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: BudgetGoalsFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.budget_goals_fragment, container, false)



        return binding.root
    }
}