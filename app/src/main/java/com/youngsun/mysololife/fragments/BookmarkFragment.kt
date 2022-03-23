package com.youngsun.mysololife.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.youngsun.mysololife.R
import com.youngsun.mysololife.databinding.FragmentBookmarkBinding

class BookmarkFragment : Fragment() {
    private lateinit var binding : FragmentBookmarkBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate( inflater, R.layout.fragment_bookmark , container, false )

        binding.homeTab.setOnClickListener { it.findNavController().navigate(R.id.action_bookmarkFragment_to_homeFragment) }
        binding.tipTab.setOnClickListener {  it.findNavController().navigate(R.id.action_bookmarkFragment_to_tipFragment)}
        binding.talkTab.setOnClickListener {  it.findNavController().navigate(R.id.action_bookmarkFragment_to_talkFragment)}
        binding.storeTab.setOnClickListener {  it.findNavController().navigate(R.id.action_bookmarkFragment_to_storeFragment)}

        return binding.root
    }


}