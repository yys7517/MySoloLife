package com.youngsun.mysololife.contentsList

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.youngsun.mysololife.R
import com.youngsun.mysololife.utils.FbAuth
import com.youngsun.mysololife.utils.FbRef

// items -> 게시글 목록
// keyList -> 게시글 key 목록
// bookmarkList -> 사용자의 북마크된 게시글 key 목록
class BookmarkRVAdapter( val context : Context,
                         val items : ArrayList<ContentModel>,
                         val keyList : ArrayList<String>,
                         val bookmarkList : MutableList<String> ) : RecyclerView.Adapter<BookmarkRVAdapter.ViewHolder>()
{
    private val TAG = BookmarkRVAdapter::class.java.simpleName
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkRVAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate( R.layout.contents_rv_item, parent, false )


        return ViewHolder(v)
    }

//    interface ItemClick {
//        fun onClick( view : View, position: Int )
//    }
//
//    var itemClick : ItemClick? = null

    override fun onBindViewHolder(holder: BookmarkRVAdapter.ViewHolder, position: Int) {
//        if( itemClick != null ) {
//            holder.itemView.setOnClickListener { v->
//                itemClick?.onClick( v, position )
//            }
//        }
        holder.bindItems( items[position], keyList[position] )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder( itemView : View ) : RecyclerView.ViewHolder(itemView) {
        // item -> 게시글
        // key -> 게시글 key 값
        fun bindItems( item : ContentModel, key : String ) {

            val contentTitle : TextView = itemView.findViewById(R.id.textArea)
            val contentImage : ImageView = itemView.findViewById(R.id.imageArea)
            val bookmarkArea = itemView.findViewById<ImageView>(R.id.bookmarkArea)

            // 제목 로드
            contentTitle.text = item.title

            // 이미지 로드
            Glide
                .with( context )
                .load( item.imageUrl )
                .into( contentImage )

            // 북마크 정보 불러오기
            if( bookmarkList.contains(key) ) {
                bookmarkArea.setImageResource(R.drawable.bookmark_color)
            }
            else {
                bookmarkArea.setImageResource(R.drawable.bookmark_white)
            }

            // RV 클릭 리스너
            itemView.setOnClickListener {
                // Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show()
                val intent =Intent(context, ContentShowActivity::class.java)
                intent.putExtra("webUrl", item.webUrl)
                itemView.context.startActivity( intent )
            }

            // RV 북마크 이미지 클릭 리스너
            bookmarkArea.setOnClickListener {

                // Toast.makeText(context, key, Toast.LENGTH_SHORT).show()
                Log.d(TAG, FbAuth.getUid() )     // uid
                Log.d(TAG, key )                 // contents key

                // 1. 북마크에 저장되어 있는 게시물일 때 눌렸다면 ? -> 북마크 목록에서 삭제
                if( bookmarkList.contains(key) ) {

                    FbRef.bookmarkRef
                        .child( FbAuth.getUid() )
                        .child(key)
                        .removeValue()

                    // *** bookmarkList.remove( key ) ***
                    // Firebase 에서 값을 지워주므로, ContentsListActivity 에서 파라미터로 받아온 bookmarkList 도 서버에 따라 동기화 되므로,
                    // 따로 bookmarkList 에서 지워주지 않아도 된다.

                    Toast.makeText( context, "북마크에서 삭제되었습니다.",Toast.LENGTH_SHORT).show()

                    // bookmarkArea.setImageResource(R.drawable.bookmark_white)
                    // 바깥 코드 -> 북마크 정보 불러오기 부분에서 매 번 북마크 이미지 최신화가 된다.
                }
                // 2. 북마크에 저장되어 있는 게시물이 아닐 때 눌렸다면 ? -> 북마크 등록
                else {

                    FbRef.bookmarkRef
                        .child( FbAuth.getUid() )
                        .child(key)
                        .setValue( BookmarkModel(true) )

                    Toast.makeText( context, "북마크에 저장되었습니다.",Toast.LENGTH_SHORT).show()

                    // bookmarkArea.setImageResource(R.drawable.bookmark_color)
                    // 바깥 코드 -> 북마크 정보 불러오기 부분에서 매 번 북마크 이미지 최신화가 된다.
                }











            }


        }
    }
}