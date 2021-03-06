package com.youngsun.mysololife.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.youngsun.mysololife.R
import com.youngsun.mysololife.contentsList.ContentsListActivity
import com.youngsun.mysololife.databinding.FragmentTipBinding


class TipFragment : Fragment() {
    private lateinit var binding : FragmentTipBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate( inflater, R.layout.fragment_tip , container, false )

        // 하단 내비게이션 탭 리스너
        binding.homeTab.setOnClickListener { it.findNavController().navigate(R.id.action_tipFragment_to_homeFragment)  }
        binding.bookmarkTab.setOnClickListener { it.findNavController().navigate(R.id.action_tipFragment_to_bookmarkFragment)  }
        binding.storeTab.setOnClickListener {  it.findNavController().navigate(R.id.action_tipFragment_to_storeFragment) }
        binding.talkTab.setOnClickListener {  it.findNavController().navigate(R.id.action_tipFragment_to_talkFragment) }

        // 카테코리 클릭 리스너
        binding.categoryAll.setOnClickListener {
            val intent = Intent( context, ContentsListActivity::class.java )
            intent.putExtra("category","contents")
            startActivity(intent)
        }



        return binding.root
    }

}