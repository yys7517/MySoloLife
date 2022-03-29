package com.youngsun.mysololife.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.youngsun.mysololife.R
import com.youngsun.mysololife.board.BoardModel
import com.youngsun.mysololife.board.BoardRVAdapter
import com.youngsun.mysololife.board.BoardWriteActivity
import com.youngsun.mysololife.contentsList.ContentModel
import com.youngsun.mysololife.databinding.FragmentTalkBinding
import com.youngsun.mysololife.utils.FbRef


// 자취톡 - 자취하는 사람들의 게시판 커뮤니티 프래그먼트
class TalkFragment : Fragment() {
    private lateinit var binding : FragmentTalkBinding

    private val items = ArrayList<BoardModel>()     // 게시글 리스트
    private val itemKeyList = ArrayList<String>()   // 게시글 key 리스트

    private val TAG : String = TalkFragment::class.java.simpleName

    private lateinit var boardRVAdapter : BoardRVAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate( inflater, R.layout.fragment_talk , container, false )

        binding.homeTab.setOnClickListener { it.findNavController().navigate(R.id.action_talkFragment_to_homeFragment)  }
        binding.bookmarkTab.setOnClickListener { it.findNavController().navigate(R.id.action_talkFragment_to_bookmarkFragment)  }
        binding.storeTab.setOnClickListener {  it.findNavController().navigate(R.id.action_talkFragment_to_storeFragment) }
        binding.tipTab.setOnClickListener {  it.findNavController().navigate(R.id.action_talkFragment_to_tipFragment) }

        // 게시판 RecyclerView
        val rv = binding.boardRV
        boardRVAdapter = BoardRVAdapter( requireContext(), items, itemKeyList)
        rv.adapter = boardRVAdapter
        rv.layoutManager = LinearLayoutManager( context )

        // 전체 게시글 받아오기.
        getBoardData()

        setUpSwipeRefresh() // 스와이프 이벤트 생성 메서드 (게시글 목록 새로고침)

        // 글 쓰기 버튼 클릭 시
        binding.fabWrite.setOnClickListener {
            val intent = Intent( context ,BoardWriteActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun getBoardData() {
        val getBoardListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // 1. 게시글 목록을 초기화한다.
                items.clear()
                itemKeyList.clear()

                // 2. 전체 게시글 목록을 가져온다.
                for (dataModel in dataSnapshot.children) {
                    // Log.d(TAG, dataModel.key.toString())  // 게시글의 Key 값을 Log로 확인.
                    val board = dataModel.getValue(BoardModel::class.java)
                    val boardKey = dataModel.key.toString()

                    // 값이 리스트에 있다면 더한다.
                    items.add(board!!)
                    itemKeyList.add(boardKey!!)
                }
                // 최신 작성한 글이 제일 위로 오게 한다.
                items.reverse()
                itemKeyList.reverse()

                // Adapter 의 DataSet이 변경되었다면, 최신화 해준다.
                boardRVAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FbRef.boardRef.addValueEventListener(getBoardListener)
    }

    // 스와이프 이벤트 생성 메서드
    private fun setUpSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false       // 스와이프 표시 제거
            getBoardData()
        }
    }

    // Firebase Storage 에서 이미지를 가져오는 딜레이가 좀 있다.
    // 따라서 게시글이 작성되고 보여지는 Fragment 게시글 목록을 새로고침하는데 4000ms 딜레이를 주었다.
    override fun onResume() {
        super.onResume()
        Handler().postDelayed( {
            getBoardData()
        }, 4000 )

    }
}