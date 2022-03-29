package com.youngsun.mysololife.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.youngsun.mysololife.R
import com.youngsun.mysololife.databinding.FragmentStoreBinding


class StoreFragment : Fragment() {
    private lateinit var binding : FragmentStoreBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate( inflater, R.layout.fragment_store , container, false )

        binding.homeTab.setOnClickListener { it.findNavController().navigate(R.id.action_storeFragment_to_homeFragment)  }
        binding.bookmarkTab.setOnClickListener { it.findNavController().navigate(R.id.action_storeFragment_to_bookmarkFragment)  }
        binding.talkTab.setOnClickListener {  it.findNavController().navigate(R.id.action_storeFragment_to_talkFragment) }
        binding.tipTab.setOnClickListener {  it.findNavController().navigate(R.id.action_storeFragment_to_tipFragment) }

        val webView : WebView = binding.storeWebView
        webView.webViewClient = WebViewClient()

        webView.loadUrl("https://oneroommaking.com/")

        return binding.root
    }


}