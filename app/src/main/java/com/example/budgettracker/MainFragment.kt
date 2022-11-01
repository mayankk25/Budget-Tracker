package com.example.budgettracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.budgettracker.databinding.LoginFragmentBinding
import com.example.budgettracker.databinding.MainFragmentBinding

class MainFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val binding: MainFragmentBinding = DataBindingUtil.inflate(inflater,
            R.layout.main_fragment, container, false)

        return binding.root
    }
}