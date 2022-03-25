package com.youngsun.mysololife.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.youngsun.mysololife.R
import com.youngsun.mysololife.contentsList.BookmarkRVAdapter
import com.youngsun.mysololife.contentsList.ContentModel
import com.youngsun.mysololife.databinding.FragmentBookmarkBinding
import com.youngsun.mysololife.utils.FbAuth
import com.youngsun.mysololife.utils.FbRef

class BookmarkFragment : Fragment() {
    private lateinit var binding: FragmentBookmarkBinding
    private val TAG = BookmarkFragment::class.java.simpleName

    private lateinit var rvAdapter: BookmarkRVAdapter

    private val items = ArrayList<ContentModel>()       // 게시글 리스트
    private val itemKeyList = ArrayList<String>()       // 게시글 Key 리스트

    private val bookmarkIdList = mutableListOf<String>()    // 북마크 된 게시글 key 리스트

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bookmark, container, false)

        binding.homeTab.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_homeFragment)
        }
        binding.tipTab.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_tipFragment)
        }
        binding.talkTab.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_talkFragment)
        }
        binding.storeTab.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_storeFragment)
        }

        val rv = binding.recyclerView
        rvAdapter = BookmarkRVAdapter(requireContext(), items, itemKeyList, bookmarkIdList)

        rv.adapter = rvAdapter
        rv.layoutManager = GridLayoutManager(context, 2)

        // 북마크 된 컨텐츠 목록만 보여주기
        // 어떻게 ?

        // 1. 사용자가 북마크한 게시글의 ID(key 값)를 가져옴 -> bookmarkIdList
        getBookmarkId()

        // 2. 전체 카테고리의 게시글을 가져오는데, 북마크한 게시글의 ID랑 일치한 게시글만 가져온다.
        getBookmarkData()

        return binding.root
    }

    // 전체 게시글 중, 북마크 된 key 값만 선별하여 가져오기.
    private fun getBookmarkData() {
        val getContentListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // 1. 게시글 목록을 초기화한다.
                items.clear()
                itemKeyList.clear()

                // 2. 게시글 목록을 가져온다.
                for (dataModel in dataSnapshot.children) {
                    // Log.d(TAG, dataModel.key.toString())  // 게시글의 Key 값을 Log로 확인.
                    val content = dataModel.getValue(ContentModel::class.java)
                    val contentKey = dataModel.key.toString()

                    // 게시글 key 값이 북마크 된 게시글의 key 값 목록에 있으면 담아준다.
                    if (bookmarkIdList.contains(contentKey)) {
                        itemKeyList.add(contentKey!!)
                        items.add(content!!)
                    }

                }

                // 3. 그렇게 북마크 된 게시글로 선별 된 게시글 리스트 items, itemKeyList 를 RVAdapter 에 넘겨준다.
                rvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FbRef.category_all.addValueEventListener(getContentListener)
    }

    // 사용자 북마크 정보 가져오기
    private fun getBookmarkId() {
        val getBookmarkRef = FbRef.bookmarkRef.child(FbAuth.getUid())

        val getBookmarkListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                bookmarkIdList.clear()

                for (dataModel in dataSnapshot.children) {
                    Log.d("getBookmarkData", dataModel.key.toString()) // 컨텐츠 key 값.
                    Log.d("getBookmarkData", dataModel.toString())

                    bookmarkIdList.add(dataModel.key.toString())
                }

                Log.d("Bookmark : ", bookmarkIdList.toString())
                rvAdapter.notifyDataSetChanged()    // 사용자의 북마크 정보를 어댑터에 넘겨준다. 북마크 버튼 최신화

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("GetContentList", "북마크 정보 가져오기 실패.", databaseError.toException())
            }
        }
        getBookmarkRef.addValueEventListener(getBookmarkListener)
    }


}