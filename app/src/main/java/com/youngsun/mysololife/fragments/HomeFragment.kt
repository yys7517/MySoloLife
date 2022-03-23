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
import com.youngsun.mysololife.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate( inflater, R.layout.fragment_home , container, false )

        // 꿀팁 탭 클릭 시
        binding.tipTab.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_tipFragment)
        }

        // 자취톡 탭 클릭 시
        binding.talkTab.setOnClickListener {
            it.findNavController().navigate( R.id.action_homeFragment_to_talkFragment )
        }

        // 북마크 탭 클릭 시
        binding.bookmarkTab.setOnClickListener {
            it.findNavController().navigate( R.id.action_homeFragment_to_bookmarkFragment )
        }

        // 스토어 탭 클릭 시
        binding.storeTab.setOnClickListener {
            it.findNavController().navigate( R.id.action_homeFragment_to_storeFragment )
        }
        return binding.root
    }


}