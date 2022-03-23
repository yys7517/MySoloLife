package com.youngsun.mysololife.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.youngsun.mysololife.R
import com.youngsun.mysololife.databinding.FragmentTalkBinding


class TalkFragment : Fragment() {
    private lateinit var binding : FragmentTalkBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate( inflater, R.layout.fragment_talk , container, false )

        binding.homeTab.setOnClickListener { it.findNavController().navigate(R.id.action_talkFragment_to_homeFragment)  }
        binding.bookmarkTab.setOnClickListener { it.findNavController().navigate(R.id.action_talkFragment_to_bookmarkFragment)  }
        binding.storeTab.setOnClickListener {  it.findNavController().navigate(R.id.action_talkFragment_to_storeFragment) }
        binding.tipTab.setOnClickListener {  it.findNavController().navigate(R.id.action_talkFragment_to_tipFragment) }

        return binding.root
    }


}