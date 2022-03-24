package com.youngsun.mysololife.contentsList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.youngsun.mysololife.R

class ContentsRVAdapter( val context : Context, val items : ArrayList<ContentModel> ) : RecyclerView.Adapter<ContentsRVAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentsRVAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate( R.layout.contents_rv_item, parent, false )


        return ViewHolder(v)
    }

    interface ItemClick {
        fun onClick( view : View, position: Int )
    }

    var itemClick : ItemClick? = null

    override fun onBindViewHolder(holder: ContentsRVAdapter.ViewHolder, position: Int) {
        if( itemClick != null ) {
            holder.itemView.setOnClickListener { v->
                itemClick?.onClick( v, position )
            }
        }
        holder.bindItems( items[position] )
    }

    override fun getItemCount(): Int {
        return items.size
    }



    inner class ViewHolder( itemView : View ) : RecyclerView.ViewHolder(itemView) {
        fun bindItems( item : ContentModel ) {

            val contentTitle : TextView = itemView.findViewById(R.id.textArea)
            val contentImage : ImageView = itemView.findViewById(R.id.imageArea)

            // 제목 로드
            contentTitle.text = item.title

            // 이미지 로드
            Glide
                .with( context )
                .load( item.imageUrl )
                .into( contentImage )



        }
    }
}