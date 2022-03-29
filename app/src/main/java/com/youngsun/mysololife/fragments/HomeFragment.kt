package com.youngsun.mysololife.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.youngsun.mysololife.R
import com.youngsun.mysololife.contentsList.BookmarkRVAdapter
import com.youngsun.mysololife.contentsList.ContentModel
import com.youngsun.mysololife.contentsList.ContentsListActivity
import com.youngsun.mysololife.databinding.FragmentHomeBinding
import com.youngsun.mysololife.utils.FbRef

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding

    val bookmarkIdList = mutableListOf<String>()
    val items = ArrayList<ContentModel>()
    val itemKeyList = ArrayList<String>()

    lateinit var rvAdapter : BookmarkRVAdapter

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

        // 카테코리 클릭 리스너
        binding.categoryAll.setOnClickListener {
            val intent = Intent( context, ContentsListActivity::class.java )
            intent.putExtra("category","contents")
            startActivity(intent)
        }

        rvAdapter = BookmarkRVAdapter(requireContext(), items, itemKeyList, bookmarkIdList)

        val rv : RecyclerView = binding.mainRV
        rv.adapter = rvAdapter

        rv.layoutManager = GridLayoutManager(requireContext(), 2)

        getCategoryData()

        return binding.root
    }

    private fun getCategoryData(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(ContentModel::class.java)

                    items.add(item!!)
                    itemKeyList.add(dataModel.key.toString())
                }
                rvAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FbRef.category_all.addValueEventListener(postListener)

    }

}