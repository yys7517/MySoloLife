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

            val boardImg : ImageView = itemView.findViewById(R.id.boardRVImg)

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