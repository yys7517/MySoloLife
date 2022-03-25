package com.youngsun.mysololife.board

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.youngsun.mysololife.R
import com.youngsun.mysololife.contentsList.ContentShowActivity
import com.youngsun.mysololife.utils.FbRef

// 게시글 RV Adapter
class BoardRVAdapter(
    val context : Context,
    val items : ArrayList<BoardModel>,
    val keyList : ArrayList<String> ) : RecyclerView.Adapter< BoardRVAdapter.ViewHolder > () {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardRVAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate( R.layout.board_rv_item, parent, false)

        return ViewHolder( view )
    }

    override fun onBindViewHolder(holder: BoardRVAdapter.ViewHolder, position: Int) {
        holder.bindItems( items[position] , keyList[position] )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder( itemView : View ) : RecyclerView.ViewHolder(itemView) {
        fun bindItems( item : BoardModel, key : String ) {
            val boardTitle : TextView = itemView.findViewById(R.id.boardRVTitle)
            val boardContents : TextView = itemView.findViewById(R.id.boardRVContents)
            val boardWriteTime : TextView = itemView.findViewById(R.id.boardRVTime)

            boardTitle.text = item.title
            boardContents.text = item.contents
            boardWriteTime.text = item.time

            // 업로드 된 이미지가 있다면 ?
            val boardImageView : ImageView = itemView.findViewById(R.id.boardRVImg)

            // Firebase Storage 의 게시글 key 값에 따라 게시글 이미지가 있는지 확인 해보자.
            val boardImgRef = FbRef.storageRef.child("${key}.png")

            boardImgRef.downloadUrl.addOnSuccessListener {
                // 게시글의 key 값에 따른 이미지가 다운로드 되었다면 ?

                // 1. 이미지 뷰가 보이게 하자.
                boardImageView.visibility = View.VISIBLE

                // 2. 이미지를 다운로드해서 Glide를 통해 ImageView에 적용한다.
                Glide
                    .with(context)
                    .load( it )
                    .into(boardImageView)

            }.addOnFailureListener {
                // 이미지가 없다면
                // 아무행위도 하지 않는다.
            }

            // 게시글 클릭 시 - 게시글 보여주기 Activity
            itemView.setOnClickListener {
                val intent = Intent( context, BoardShowActivity::class.java )

                // 게시글 보여주기 Activity 기능

                // 첫 번째 방법 - item의 title, contents, time 등등 Intent 로 전달하기
//                intent.putExtra("title", item.title)
//                intent.putExtra("contents", item.contents)
//                intent.putExtra("time", item.time)

                // 두 번째 방법 - Firebase 에 있는 board 에 게시글 key 값으로 받아오는 방법.
                intent.putExtra("key", key )

                context.startActivity( intent )
            }
        }
    }
}